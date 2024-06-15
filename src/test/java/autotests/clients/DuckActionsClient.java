package autotests.clients;

import autotests.BaseTest;
import autotests.payloads.WingsState;
import com.consol.citrus.TestCaseRunner;
import io.qameta.allure.Step;
import org.springframework.http.HttpStatus;

import java.util.Map;

import static com.consol.citrus.actions.ExecuteSQLQueryAction.Builder.query;

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

    @Step("запрос на полет утки")
    protected void requestFlyDuck(TestCaseRunner runner, String id) {
        Map<String, String> queryParams = Map.of(
                "id", id
        );
        sendGetRequest(runner, yellowDuckService, "/api/duck/action/fly", queryParams);
    }

    @Step("запрос на плавание утки")
    protected void requestSwimDuck(TestCaseRunner runner, String id) {
        Map<String, String> queryParams = Map.of(
                "id", id
        );
        sendGetRequest(runner, yellowDuckService, "/api/duck/action/swim", queryParams);
    }

    @Step("запрос на получение свойств утки")
    protected void requestDuckProps(TestCaseRunner runner, String id) {
        Map<String, String> queryParams = Map.of(
                "id", id
        );
        sendGetRequest(runner, yellowDuckService, "/api/duck/action/properties", queryParams);
    }

    @Step("запрос на кряканье")
    protected void requestQuackDuck(TestCaseRunner runner, String id, String repetitionCount, String soundCount) {
        Map<String, String> queryParams = Map.of(
                "id", id,
                "repetitionCount", repetitionCount,
                "soundCount", soundCount
        );
        sendGetRequest(runner, yellowDuckService, "/api/duck/action/quack", queryParams);
    }

    @Step("обновление данных в бд")
    protected void databaseUpdate(TestCaseRunner runner, String sqlQuery) {
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

    @Step("получение и запись id в переменную ${duckId} из бд")
    protected void writeIdFromDb(TestCaseRunner runner, String sql) {
        writeIdFromDbToVariable(runner, sql, "id", "duckId");
    }

    @Step("финальная очистка бд")
    protected void finallyClearDb(TestCaseRunner runner) {
        finallyExecuteSqlQuery(runner, "delete from duck where id=${duckId}");
    }

    @Step("валидация ответа с помощью строки")
    protected void validateResponseByString(TestCaseRunner runner, HttpStatus httpStatus, String expectedString) {
        validateResponseByString(runner, httpStatus, expectedString, yellowDuckService);
    }

    @Step("валидация ответа с помощью json файла")
    protected void validateResponseByJson(TestCaseRunner runner, HttpStatus httpStatus, String expectedPayload) {
        validateResponseByJson(runner, httpStatus, expectedPayload, yellowDuckService);
    }

    @Step("валидация ответа с помощью класса")
    protected void validateResponseByClass(TestCaseRunner runner, HttpStatus httpStatus, Object expectedPayload) {
        validateResponseByClass(runner, httpStatus, expectedPayload, yellowDuckService);
    }

}