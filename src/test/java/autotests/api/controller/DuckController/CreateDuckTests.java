package autotests.api.controller.DuckController;

import autotests.payloads.Duck;
import autotests.payloads.WingsState;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;
import autotests.clients.DuckActionsClient;

public class CreateDuckTests extends DuckActionsClient {

    @Test(description = "Проверка создания утки с material = rubber ")
    @CitrusTest
    public void createRubberDuckTest(@Optional @CitrusResource TestCaseRunner runner) {
        //  создаем утку
        Duck duck = new Duck().color("green").height(1.1).material("rubber").sound("quack").wingsState(WingsState.ACTIVE);
        createDuck(runner, duck);
        //  проверяем ответ
        validateResponseByClass(runner, 200, new Duck().color("green").height(1.1).material("rubber").sound("quack").wingsState(WingsState.ACTIVE));
        //        + 2 validation
    }

    @Test(description = "Проверка создания утки с material = wood ")
    @CitrusTest
    public void createWoodenDuckTest(@Optional @CitrusResource TestCaseRunner runner) {
        //  создаем утку
        Duck duck = new Duck().color("green").height(1.1).material("wood").sound("quack").wingsState(WingsState.ACTIVE);
        createDuck(runner, duck);
        //  проверяем ответ
        validateResponseByClass(runner, 200, new Duck().color("green").height(1.1).material("wood").sound("quack").wingsState(WingsState.ACTIVE));
//        + 2 validation
    }

}
