package autotests.api.controller.DuckController;

import autotests.clients.DuckActionsClient;
import autotests.payloads.Duck;
import autotests.payloads.Message;
import autotests.payloads.WingsState;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class DeleteDuckTests extends DuckActionsClient {

    @Test(description = "Проверка удаления утки")
    @CitrusTest
    public void deleteDuckTest(@Optional @CitrusResource TestCaseRunner runner) {
        //  создаем утку
        Duck duck = new Duck().color("green").height(1.1).material("rubber").sound("quack").wingsState(WingsState.ACTIVE);
        createDuck(runner, duck);
        //  получаем id созданной утки
        getDuckId(runner);
        //  удаляем утку
        deleteDuck(runner, "${duckId}");
        //  проверяем ответ
//        validateResponseByClass(runner, 200, new Message().message("Duck is deleted"));
//        validateResponseByJson(runner, 200, "test_responses/deleteDuckTest/deleteDuckResponse.json");
        validateResponseByString(runner, 200, "{\n" +
                "  \"message\": \"Duck is deleted\"\n" +
                "}");
        //  проверяем в бд
        checkInDb(runner);
        //  CHECK DB --->
    }

}
