package autotests.clients;

import autotests.BaseTest;
import autotests.EndpointConfig;
import autotests.payloads.WingsState;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.message.builder.ObjectMappingPayloadBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Step;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

import java.util.Map;

import static com.consol.citrus.actions.ExecuteSQLAction.Builder.sql;
import static com.consol.citrus.actions.ExecuteSQLQueryAction.Builder.query;
import static com.consol.citrus.container.FinallySequence.Builder.doFinally;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

@ContextConfiguration(classes = {EndpointConfig.class})
public class DuckActionsClient extends BaseTest {

    @Step("запрос на создание утки")
    protected void requestCreateDuck(TestCaseRunner runner, Object duckProps) {
        sendPostRequest(runner, yellowDuckService, "/api/duck/create", duckProps);
    }

    @Step("запрос на обновление утки")
    protected void requestUpdateDuck(TestCaseRunner runner, String color, double height, String id, String material, String sound, WingsState wingsState) {
        Map<String, String> queryParams = Map.of(
                "color", color,
                "height", String.valueOf(height),
                "id", id,
                "material", material,
                "sound", sound,
                "wingsState", wingsState.toString()
        );
        sendPutRequest(runner, yellowDuckService, "/api/duck/update", queryParams);
    }

    @Step("запрос на удаление утки")
    protected void requestDeleteDuck(TestCaseRunner runner) {
        sendDeleteRequest(runner, yellowDuckService, "/api/duck/delete", "id", "${duckId}");
    }

//    ----------

    @Step("запрос на полет утки")
    public void flyDuck(TestCaseRunner runner, String id) {
        runner.$(http().client(yellowDuckService)
                .send()
                .get("/api/duck/action/fly")
                .queryParam("id", id)
        );
    }

    @Step("запрос на плавание утки")
    public void swimDuck(TestCaseRunner runner, String id) {
        runner.$(http().client(yellowDuckService)
                .send()
                .get("/api/duck/action/swim")
                .queryParam("id", id)
        );
    }

    @Step("запрос на получение свойств утки")
    public void getDuckProps(TestCaseRunner runner, String id) {
        runner.$(http().client(yellowDuckService)
                .send()
                .get("/api/duck/action/properties")
                .queryParam("id", id)
        );
    }

    @Step("запрос на кряканье")
    public void quackDuck(TestCaseRunner runner, String id, String repetitionCount, String soundCount) {
        runner.$(http().client(yellowDuckService)
                .send()
                .get("/api/duck/action/quack")
                .queryParam("id", id)
                .queryParam("repetitionCount", repetitionCount)
                .queryParam("soundCount", soundCount)
        );
    }

    @Step("изменение даныых в бд")
    public void databaseUpdate(TestCaseRunner runner, String sql) {
        runner.$(
                sql(db)
                        .statement(sql)
        );
    }

    @Step("валидация данных утки в бд")
    public void databaseValidateDuck(TestCaseRunner runner, String id, String color, double height, String material, String sound, WingsState wingsState) {
        runner.$(
                query(db)
                        .statement("select * from duck where id = " + id)
                        .validate("COLOR", color)
                        .validate("HEIGHT", String.valueOf(height))
                        .validate("MATERIAL", material)
                        .validate("SOUND", sound)
                        .validate("WINGS_STATE", wingsState.toString())
        );
    }

    @Step("валидация данных по одному полю в бд")
    public void databaseQueryAndValidate(TestCaseRunner runner, String sql, String column, String... values) {
        runner.$(
                query(db)
                        .statement(sql)
                        .validate(column, values)
        );
    }

    @Step("получение и зпись id в переменную из бд")
    public void writeIdFromDb(TestCaseRunner runner, String sql) {
        runner.$(
                query(db)
                        .statement(sql)
                        .extract("id", "duckId")
        );
    }

    @Step("очистка бд")
    public void clearDB(TestCaseRunner runner, String duckId) {
        runner.$(
                doFinally()
                        .actions(
                                sql(db)
                                        .statement("delete from duck where id=" + duckId)
                        )
        );
    }

    @Step("валидация ответа с помощью строки")
    public void validateResponseByString(TestCaseRunner runner, int statusCode, String expectedString) {
        runner.$(http()
                .client(yellowDuckService)
                .receive()
                .response()
                .message()
                .statusCode(statusCode)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(expectedString)
        );
    }

    @Step("валидация ответа с помощью json файла")
    public void validateResponseByJson(TestCaseRunner runner, int statusCode, String expectedPayload) {
        runner.$(http()
                .client(yellowDuckService)
                .receive()
                .response()
                .message()
                .statusCode(statusCode)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new ClassPathResource(expectedPayload))
        );
    }

    @Step("валидация ответа с помощью класса")
    public void validateResponseByClass(TestCaseRunner runner, int statusCode, Object expectedPayload) {
        runner.$(http()
                .client(yellowDuckService)
                .receive()
                .response()
                .message()
                .statusCode(statusCode)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new ObjectMappingPayloadBuilder(expectedPayload, new ObjectMapper()))
        );
    }


//    @Step("валидация ответа с помощью jsonschema")
//    public void validateResponseByJsonSchema(TestCaseRunner runner, int statusCode, String schema) {
//        runner.$(http()
//                .client(yellowDuckService)
//                .receive()
//                .response()
//                .message()
//                .statusCode(statusCode)
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .validate(json().schemaValidation(true).schema(schema))
//        );
//    }

}