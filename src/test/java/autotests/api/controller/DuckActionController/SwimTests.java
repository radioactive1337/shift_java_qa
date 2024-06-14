package autotests.api.controller.DuckActionController;

import autotests.clients.DuckActionsClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

@Epic("Duck action controller")
@Feature("/api/duck/action/swim")
public class SwimTests extends DuckActionsClient {
    @Test(description = "Проверка swim с существующим id")
    @CitrusTest
    public void swimExistedDuckTest(@Optional @CitrusResource TestCaseRunner runner) {
        //  создаем утку + очитска бд в конце теста
        runner.variable("duckId", "citrus:randomNumber(4, false)");
        finallyClearDb(runner);
        databaseUpdate(runner, "insert into duck values (${duckId}, 'green', 1.1, 'rubber', 'quack', 'ACTIVE')");

        //  плаваем
        requestSwimDuck(runner, "${duckId}");

        //  проверяем ответ
        validateResponseByJson(runner, HttpStatus.OK, "test_responses/swimTest/swimExistedDuckResponse.json");
    }

    @Test(description = "Проверка swim с несуществующим id")
    @CitrusTest
    public void swimNotExistedDuckTest(@Optional @CitrusResource TestCaseRunner runner) {
        //  плаваем
        requestSwimDuck(runner, "1234567");

        //  проверяем ответ
        validateResponseByJson(runner, HttpStatus.NOT_FOUND, "test_responses/swimTest/swimNotExistedDuckResponse.json");
    }

}
