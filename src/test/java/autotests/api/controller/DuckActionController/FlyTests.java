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
@Feature("/api/duck/action/fly")
public class FlyTests extends DuckActionsClient {

    @Test(description = "Проверка fly с существующим id с активными крыльями")
    @CitrusTest
    public void flyActiveDuckTest(@Optional @CitrusResource TestCaseRunner runner) {
        //  создаем утку + очитска бд в конце теста
        runner.variable("duckId", "citrus:randomNumber(4, false)");
        clearDB(runner, "${duckId}");
        databaseUpdate(runner, "insert into duck values (${duckId}, 'green', 1.1, 'rubber', 'quack', 'ACTIVE')");

        //  летаем
        flyDuck(runner, "${duckId}");

        //  проверяем ответ
        validateResponseByJson(runner, 200, "test_responses/flyTest/flyActiveDuckResponse.json");
    }

    @Test(description = "Проверка fly с существующим id со связанными крыльями")
    @CitrusTest
    public void flyFixedDuckTest(@Optional @CitrusResource TestCaseRunner runner) {
        //  создаем утку + очитска бд в конце теста
        runner.variable("duckId", "citrus:randomNumber(4, false)");
        clearDB(runner, "${duckId}");
        databaseUpdate(runner, "insert into duck values (${duckId}, 'green', 1.1, 'rubber', 'quack', 'FIXED')");

        //  летаем
        flyDuck(runner, "${duckId}");

        //  проверяем ответ
        validateResponseByJson(runner, 200, "test_responses/flyTest/flyFixedDuckResponse.json");
    }

    @Test(description = "Проверка fly с существующим id с крыльями в неопределенном состоянии")
    @CitrusTest
    public void flyUndefinedDuckTest(@Optional @CitrusResource TestCaseRunner runner) {
        //  создаем утку + очитска бд в конце теста
        runner.variable("duckId", "citrus:randomNumber(4, false)");
        clearDB(runner, "${duckId}");
        databaseUpdate(runner, "insert into duck values (${duckId}, 'green', 1.1, 'rubber', 'quack', 'UNDEFINED')");

        //  летаем
        flyDuck(runner, "${duckId}");

        //  проверяем ответ
        validateResponseByJson(runner, 200, "test_responses/flyTest/flyUndefinedDuckResponse.json");
    }

}
