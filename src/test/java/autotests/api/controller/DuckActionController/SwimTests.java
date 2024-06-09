package autotests.api.controller.DuckActionController;

import autotests.clients.DuckActionsClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

@Epic("Duck action controller")
@Feature("/api/duck/action/swim")
public class SwimTests extends DuckActionsClient {
    @Test(description = "ѕроверка swim с существующим id")
    @CitrusTest
    public void swimExistedDuckTest(@Optional @CitrusResource TestCaseRunner runner) {
        //  создаем утку + очитска бд в конце теста
        runner.variable("duckId", "citrus:randomNumber(3, false)");
        clearDB(runner, "${duckId}");
        databaseUpdate(runner, "insert into duck values (${duckId}, 'green', 1.1, 'rubber', 'quack', 'ACTIVE')");

        //  плаваем
        swimDuck(runner, "${duckId}");

        //  провер€ем ответ
        validateResponseByJson(runner, 200, "test_responses/swimTest/swimExistedDuckResponse.json");
    }

    @Test(description = "ѕроверка swim с несуществующим id")
    @CitrusTest
    public void swimNotExistedDuckTest(@Optional @CitrusResource TestCaseRunner runner) {
        //  плаваем
        swimDuck(runner, "1234567");
        //  провер€ем ответ
        String expectedString = "{\n" +
                "  \"timestamp\": \"@ignore()@\",\n" +
                "  \"status\": 404,\n" +
                "  \"error\": \"Duck not found\",\n" +
                "  \"message\": \"Duck with id = 1234567 is not found\",\n" +
                "  \"path\": \"/api/duck/action/fly\"\n" +
                "}";
        validateResponseByString(runner, 404, expectedString);
    }

}
