package autotests.clients;

import autotests.EndpointConfig;
import autotests.payloads.WingsState;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.message.builder.ObjectMappingPayloadBuilder;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import com.consol.citrus.http.client.HttpClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.context.ContextConfiguration;

import static com.consol.citrus.actions.ExecuteSQLAction.Builder.sql;
import static com.consol.citrus.actions.ExecuteSQLQueryAction.Builder.query;
import static com.consol.citrus.container.FinallySequence.Builder.doFinally;
import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

@ContextConfiguration(classes = {EndpointConfig.class})
public class DuckActionsClient extends TestNGCitrusSpringSupport {

    @Autowired
    public HttpClient yellowDuckService;

    @Autowired
    protected SingleConnectionDataSource db;

    @Step("������ �� �������� ����")
    public void createDuck(TestCaseRunner runner, Object body) {
        runner.$(http().client(yellowDuckService)
                .send()
                .post("/api/duck/create")
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new ObjectMappingPayloadBuilder(body, new ObjectMapper()))
        );
    }

    @Step("������ �� ���������� ����")
    public void updateDuck(TestCaseRunner runner, String color, double height, String id, String material, String sound, WingsState wingsState) {
        runner.$(http().client(yellowDuckService)
                .send()
                .put("/api/duck/update")
                .queryParam("color", color)
                .queryParam("height", String.valueOf(height))
                .queryParam("id", id)
                .queryParam("material", material)
                .queryParam("sound", sound)
                .queryParam("wingsState", wingsState.toString())
        );
    }

    @Step("������ �� �������� ����")
    public void deleteDuck(TestCaseRunner runner, String id) {
        runner.$(http().client(yellowDuckService)
                .send()
                .delete("/api/duck/delete")
                .queryParam("id", id)
        );
    }

    @Step("������ �� ����� ����")
    public void flyDuck(TestCaseRunner runner, String id) {
        runner.$(http().client(yellowDuckService)
                .send()
                .get("/api/duck/action/fly")
                .queryParam("id", id)
        );
    }

    @Step("������ �� �������� ����")
    public void swimDuck(TestCaseRunner runner, String id) {
        runner.$(http().client(yellowDuckService)
                .send()
                .get("/api/duck/action/swim")
                .queryParam("id", id)
        );
    }

    @Step("������ �� ��������� ������� ����")
    public void getDuckProps(TestCaseRunner runner, String id) {
        runner.$(http().client(yellowDuckService)
                .send()
                .get("/api/duck/action/properties")
                .queryParam("id", id)
        );
    }

    @Step("������ �� ��������")
    public void quackDuck(TestCaseRunner runner, String id, String repetitionCount, String soundCount) {
        runner.$(http().client(yellowDuckService)
                .send()
                .get("/api/duck/action/quack")
                .queryParam("id", id)
                .queryParam("repetitionCount", repetitionCount)
                .queryParam("soundCount", soundCount)
        );
    }

    @Step("��������� ������ � ��")
    public void databaseUpdate(TestCaseRunner runner, String sql) {
        runner.$(
                sql(db)
                        .statement(sql)
        );
    }

    @Step("��������� � ��������� ������ ���� �� ��")
    public void databaseQueryAndValidateDuck(TestCaseRunner runner, String color, double height, String material, String sound, WingsState wingsState) {
        runner.$(
                query(db)
                        .statement("select * from duck where id = ${duckId}")
                        .validate("COLOR", color)
                        .validate("HEIGHT", String.valueOf(height))
                        .validate("MATERIAL", material)
                        .validate("SOUND", sound)
                        .validate("WINGS_STATE", wingsState.toString())
        );
    }

    @Step("��������� � ��������� ������ �� ������ ���� �� ��")
    public void databaseQueryAndValidate(TestCaseRunner runner, String sql, String column, String... values) {
        runner.$(
                query(db)
                        .statement(sql)
                        .validate(column, values)
        );
    }

    public void writeIdFromDb(TestCaseRunner runner, String sql) {
        runner.$(
                query(db)
                        .statement(sql)
                        .extract("id", "duckId")
        );
    }

    @Step("������� ��")
    public void clearDB(TestCaseRunner runner, String duckId) {
        runner.$(doFinally().actions(sql(db).statement("delete from duck where id=" + duckId)));
    }

    @Step("��������� id ���� �� ������")
    public void getDuckId(TestCaseRunner runner) {
        runner.$(http().client(yellowDuckService)
                .receive()
                .response()
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract(fromBody().expression("$.id", "duckId"))
        );
    }

    @Step("��������� ������ � ������� ������")
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

    @Step("��������� ������ � ������� json �����")
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

    @Step("��������� ������ � ������� ������")
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

//    @Step("��������� ������ � ������� jsonschema")
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