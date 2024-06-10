package autotests.api.controller.DuckController;

import autotests.payloads.WingsState;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;
import autotests.clients.DuckActionsClient;


@Epic("Duck controller")
@Feature("/api/duck/update")
public class UpdateDuckTests extends DuckActionsClient {

    @Test(description = "Проверка обновления цвета и высоты утки")
    @CitrusTest
    public void updateColorAndHeightDuckTest(@Optional @CitrusResource TestCaseRunner runner) {
        //  создаем утку + очитска бд в конце теста
        runner.variable("duckId", "citrus:randomNumber(4, false)");
        clearDB(runner, "${duckId}");
        databaseUpdate(runner, "insert into duck values (${duckId}, 'green', 1.1, 'rubber', 'quack', 'ACTIVE')");

        //  обновляем 2 поля
        updateDuck(runner, "rainbow", 9.9, "${duckId}", "rubber", "quack", WingsState.ACTIVE);

        // проверяем ответ
        validateResponseByString(runner, 200, "{\n" +
                "  \"message\": \"Duck with id = ${duckId} is updated\"\n" +
                "}");

        //  проверяем в бд
        databaseValidateDuck(runner, "${duckId}", "rainbow", 9.9, "rubber", "quack", WingsState.ACTIVE);
    }

    @Test(description = "Проверка обновления цвета и звука утки")
    @CitrusTest
    public void updateColorAndSoundDuckTest(@Optional @CitrusResource TestCaseRunner runner) {
        //  создаем утку + очитска бд в конце теста
        runner.variable("duckId", "citrus:randomNumber(4, false)");
        clearDB(runner, "${duckId}");
        databaseUpdate(runner, "insert into duck values (${duckId}, 'green', 1.1, 'rubber', 'quack', 'ACTIVE')");

        //  обновляем 2 поля
        updateDuck(runner, "rainbow", 1.1, "${duckId}", "rubber", "mew", WingsState.ACTIVE);

        //  проверяем ответ
        validateResponseByString(runner, 200, "{\n" +
                "  \"message\": \"Duck with id = ${duckId} is updated\"\n" +
                "}");

        //  проверяем в бд
        databaseValidateDuck(runner, "${duckId}", "rainbow", 1.1, "rubber", "mew", WingsState.ACTIVE);
    }

}
