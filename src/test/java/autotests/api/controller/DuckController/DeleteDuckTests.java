package autotests.api.controller.DuckController;

import autotests.clients.DuckActionsClient;
import autotests.payloads.Duck;
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
        validateResponseByJson(runner, 200, "test_responses/deleteDuckTest/deleteDuckResponse.json");
    }

}
