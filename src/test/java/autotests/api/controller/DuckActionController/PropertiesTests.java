package autotests.api.controller.DuckActionController;

import autotests.clients.DuckActionsClient;
import autotests.payloads.Duck;
import autotests.payloads.WingsState;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

@Epic("Duck action controller")
@Feature("/api/duck/action/properties")
public class PropertiesTests extends DuckActionsClient {

    @Test(description = "Проверка получения свойств с четным id и материалом wood")
    @CitrusTest
    public void getEvenWoodenDuckPropsTest(@Optional @CitrusResource TestCaseRunner runner) {
        //  создаем утку с четным id + очитска бд в конце теста
        runner.variable("duckId", "citrus:randomNumber(4, false)1");
        finallyClearDb(runner);
        databaseUpdate(runner, "insert into duck values (${duckId}, 'green', 1.1, 'wood', 'quack', 'ACTIVE')");

        //  получаем ее свойства
        requestDuckProps(runner, "${duckId}");

        //  проверяем ответ
        validateResponseByClass(runner, 200, new Duck().color("green").height(1.1).material("wood").sound("quack").wingsState(WingsState.ACTIVE));
    }

    @Test(description = "Проверка получения свойств с нечетным id и материалом rubber")
    @CitrusTest
    public void getOddRubberDuckPropsTest(@Optional @CitrusResource TestCaseRunner runner) {
        //  создаем утку с нечетным id + очитска бд в конце теста
        runner.variable("duckId", "citrus:randomNumber(4, false)2");
        finallyClearDb(runner);
        databaseUpdate(runner, "insert into duck values (${duckId}, 'green', 1.1, 'rubber', 'quack', 'ACTIVE')");

        //  получаем ее свойства
        requestDuckProps(runner, "${duckId}");

        //  проверяем ответ
        validateResponseByClass(runner, 200, new Duck().color("green").height(1.1).material("rubber").sound("quack").wingsState(WingsState.ACTIVE));
    }

}
