package autotests.api.controller.DuckController;

import autotests.clients.DuckActionsClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;


@Epic("Duck controller")
@Feature("/api/duck/delete")
public class DeleteDuckTests extends DuckActionsClient {

    @Test(description = "Проверка удаления утки")
    @CitrusTest
    public void deleteDuckTest(@Optional @CitrusResource TestCaseRunner runner) {
        //  создаем утку + очитска бд в конце теста
        runner.variable("duckId", "citrus:randomNumber(4, false)");
        finallyClearDb(runner);
        databaseUpdate(runner, "insert into duck values (${duckId}, 'red', 1.1, 'wood', 'quack', 'ACTIVE')");

        //  запрос на удаление утки
        requestDeleteDuck(runner);

        //  проверяем ответ
        validateResponseByJson(runner, HttpStatus.OK, "test_responses/deleteDuckTest/deleteDuckResponse.json");

        //  проверяем бд
        validateDatabaseQuery(runner, "select count(*) as ducks_count from duck where id = ${duckId}", "ducks_count", "0");
    }

}
