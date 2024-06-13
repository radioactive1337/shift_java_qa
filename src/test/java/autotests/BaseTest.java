package autotests;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.message.builder.ObjectMappingPayloadBuilder;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import com.consol.citrus.http.client.HttpClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.context.ContextConfiguration;

import java.util.Map;

import static com.consol.citrus.actions.ExecuteSQLAction.Builder.sql;
import static com.consol.citrus.actions.ExecuteSQLQueryAction.Builder.query;
import static com.consol.citrus.container.FinallySequence.Builder.doFinally;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

@ContextConfiguration(classes = {EndpointConfig.class})
public class BaseTest extends TestNGCitrusSpringSupport {

    @Autowired
    protected SingleConnectionDataSource db;

    protected ObjectMapper objectMapper = new ObjectMapper();

    @Step("отправляем HTTP POST запрос")
    protected void sendPostRequest(TestCaseRunner runner, HttpClient url, String endpoint, Object body) {
        runner.$(http().client(url)
                .send()
                .post(endpoint)
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new ObjectMappingPayloadBuilder(body, objectMapper))
        );
    }

    @Step("отправляем HTTP GET запрос")
    protected void sendGetRequest(TestCaseRunner runner, HttpClient url, String endpoint, Map<String, String> queryParams) {
        runner.$(http().client(url)
                .send()
                .get(endpoint + getQueryParams(queryParams))
        );
    }

    @Step("отправляем HTTP PUT запрос")
    protected void sendPutRequest(TestCaseRunner runner, HttpClient url, String endpoint, Map<String, String> queryParams) {
        runner.$(http().client(url)
                .send()
                .put(endpoint + getQueryParams(queryParams))
        );
    }

    @Step("отправляем HTTP DELETE запрос")
    protected void sendDeleteRequest(TestCaseRunner runner, HttpClient url, String endpoint, String qKey, String qValue) {
        runner.$(http().client(url)
                .send()
                .delete(endpoint)
                .queryParam(qKey, qValue)
        );
    }

    /**
     * Преобразует карту параметров запроса в строку запроса URL.
     *
     * @param queryParams карта параметров запроса, где каждый ключ - имя параметра, а каждый значение - значение параметра
     * @return строка запроса URL в формате "?key1=value1&key2=value2&...", или пустая строка, если входная карта null или пустая
     */
    protected static String getQueryParams(Map<String, String> queryParams) {
        if (queryParams == null || queryParams.isEmpty()) {
            return "";
        }

        StringBuilder queryBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : queryParams.entrySet()) {
            queryBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        queryBuilder.deleteCharAt(queryBuilder.length() - 1);
        return "?" + queryBuilder;
    }

    @Step("выполнение SQl запроса")
    protected void executeSqlQuery(TestCaseRunner runner, String sql) {
        runner.$(
                sql(db)
                        .statement(sql)
        );
    }

    @Step("валидация запроса к бд (одно поле, одно значение)")
    protected void validateDatabaseQuery(TestCaseRunner runner, String sql, String column, String value) {
        runner.$(
                query(db)
                        .statement(sql)
                        .validate(column, value)
        );
    }

    @Step("запись в переменную из бд")
    protected void writeIdFromDbToVariable(TestCaseRunner runner, String sql, String colName, String varName) {
        runner.$(
                query(db)
                        .statement(sql)
                        .extract(colName, varName)
        );
    }

    @Step("финальный запрос к бд")
    protected void finallyExecuteSqlQuery(TestCaseRunner runner, String sql) {
        runner.$(
                doFinally()
                        .actions(
                                sql(db)
                                        .statement(sql)
                        )
        );
    }

    @Step("валидация ответа с помощью строки")
    protected void validateResponseByString(TestCaseRunner runner, int statusCode, String expectedString, HttpClient url) {
        runner.$(http()
                .client(url)
                .receive()
                .response()
                .message()
                .statusCode(statusCode)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(expectedString)
        );
    }

    @Step("валидация ответа с помощью json файла")
    protected void validateResponseByJson(TestCaseRunner runner, int statusCode, String expectedPayload, HttpClient url) {
        runner.$(http()
                .client(url)
                .receive()
                .response()
                .message()
                .statusCode(statusCode)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new ClassPathResource(expectedPayload))
        );
    }

    @Step("валидация ответа с помощью класса")
    protected void validateResponseByClass(TestCaseRunner runner, int statusCode, Object expectedPayload, HttpClient url) {
        runner.$(http()
                .client(url)
                .receive()
                .response()
                .message()
                .statusCode(statusCode)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new ObjectMappingPayloadBuilder(expectedPayload, objectMapper))
        );
    }
}
