package autotests.clients;

import autotests.BaseTest;
import autotests.EndpointConfig;
import autotests.payloads.WingsState;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.http.client.HttpClient;
import io.qameta.allure.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.Map;

import static com.consol.citrus.actions.ExecuteSQLQueryAction.Builder.query;

@ContextConfiguration(classes = {EndpointConfig.class})
public class DuckActionsClient extends BaseTest {

    @Autowired
    public HttpClient yellowDuckService;

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

    @Step("запрос на полет утки")
    public void requestFlyDuck(TestCaseRunner runner, String id) {
        Map<String, String> queryParams = Map.of(
                "id", id
        );
        sendGetRequest(runner, yellowDuckService, "/api/duck/action/fly", queryParams);
    }

    @Step("запрос на плавание утки")
    public void requestSwimDuck(TestCaseRunner runner, String id) {
        Map<String, String> queryParams = Map.of(
                "id", id
        );
        sendGetRequest(runner, yellowDuckService, "/api/duck/action/swim", queryParams);
    }

    @Step("запрос на получение свойств утки")
    public void requestDuckProps(TestCaseRunner runner, String id) {
        Map<String, String> queryParams = Map.of(
                "id", id
        );
        sendGetRequest(runner, yellowDuckService, "/api/duck/action/properties", queryParams);
    }

    @Step("запрос на кряканье")
    public void requestQuackDuck(TestCaseRunner runner, String id, String repetitionCount, String soundCount) {
        Map<String, String> queryParams = Map.of(
                "id", id,
                "repetitionCount", repetitionCount,
                "soundCount", soundCount
        );
        sendGetRequest(runner, yellowDuckService, "/api/duck/action/quack", queryParams);
    }

    @Step("изменение даныых в бд")
    public void databaseUpdate(TestCaseRunner runner, String sqlQuery) {
        executeSqlQuery(runner, sqlQuery);
    }

    @Step("валидация данных утки в бд")
    protected void validateDatabaseDuck(TestCaseRunner runner, String id, String color, double height, String material, String sound, WingsState wingsState) {
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

    @Step("получение и запись id в переменную из бд")
    public void writeIdFromDb(TestCaseRunner runner, String sql) {
        writeIdFromDbToVariable(runner, sql, "id", "duckId");
    }

    @Step("финальная очистка бд")
    public void finallyClearDb(TestCaseRunner runner) {
        finallyExecuteSqlQuery(runner, "delete from duck where id=${duckId}");
    }

    @Step("валидация ответа с помощью строки")
    public void validateResponseByString(TestCaseRunner runner, int statusCode, String expectedString) {
        validateResponseByString(runner, statusCode, expectedString, yellowDuckService);
    }

    @Step("валидация ответа с помощью json файла")
    public void validateResponseByJson(TestCaseRunner runner, int statusCode, String expectedPayload) {
        validateResponseByJson(runner, statusCode, expectedPayload, yellowDuckService);
    }

    @Step("валидация ответа с помощью класса")
    public void validateResponseByClass(TestCaseRunner runner, int statusCode, Object expectedPayload) {
        validateResponseByClass(runner, statusCode, expectedPayload, yellowDuckService);
    }

}