package autotests.api.controller.DuckActionController;

import autotests.clients.DuckActionsClient;
import autotests.payloads.Duck;
import autotests.payloads.WingsState;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class SwimTests extends DuckActionsClient {
    @Test(description = "Проверка swim с существующим id")
    @CitrusTest
    public void swimExistedDuckTest(@Optional @CitrusResource TestCaseRunner runner) {
        //  создаем утку
        Duck duck = new Duck().color("green").height(1.1).material("rubber").sound("quack").wingsState(WingsState.ACTIVE);
        createDuck(runner, duck);

        //  получаем id созданной утки
        getDuckId(runner);
        //  плаваем
        swimDuck(runner, "${duckId}");
        //  проверяем ответ
        validateResponseByJson(runner, 200, "test_responses/swimTest/swimExistedDuckResponse.json");
    }

    @Test(description = "Проверка swim с несуществующим id")
    @CitrusTest
    public void swimNotExistedDuckTest(@Optional @CitrusResource TestCaseRunner runner, @Optional @CitrusResource TestContext context) {
        //  создаем утку
        Duck duck = new Duck().color("green").height(1.1).material("rubber").sound("quack").wingsState(WingsState.ACTIVE);
        createDuck(runner, duck);

        //  получаем id утки
        getDuckId(runner);
        //  создаем несуществующий id
        int notExistedDuckId = Integer.parseInt(context.getVariable("duckId")) + 1;
        //  плаваем
        swimDuck(runner, String.valueOf(notExistedDuckId));
        //  проверяем ответ
        String expectedString = "{\n" +
                "  \"timestamp\": \"@ignore()@\",\n" +
                "  \"status\": 404,\n" +
                "  \"error\": \"Duck not found\",\n" +
                "  \"message\": \"Duck with id = " + notExistedDuckId + " is not found\",\n" +
                "  \"path\": \"/api/duck/action/fly\"\n" +
                "}";
        validateResponseByString(runner, 404, expectedString);
    }

}
