package autotests.api.controller.DuckActionController;

import autotests.clients.DuckActionsClient;
import autotests.payloads.Duck;
import autotests.payloads.Message;
import autotests.payloads.WingsState;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class FlyTests extends DuckActionsClient {

    @Test(description = "Проверка fly с существующим id с активными крыльями")
    @CitrusTest
    public void flyActiveDuckTest(@Optional @CitrusResource TestCaseRunner runner) {
        //  создаем утку
        Duck duck = new Duck().color("green").height(1.1).material("rubber").sound("quack").wingsState(WingsState.ACTIVE);
        createDuck(runner, duck);

        //  получаем id созданной утки
        getDuckId(runner);
        //  летаем
        flyDuck(runner, "${duckId}");
        //  проверяем ответ
        validateResponseByJson(runner, 200, "test_responses/flyTest/flyActiveDuckResponse.json");
    }

    @Test(description = "Проверка fly с существующим id со связанными крыльями")
    @CitrusTest
    public void flyFixedDuckTest(@Optional @CitrusResource TestCaseRunner runner) {
        //  создаем утку
        Duck duck = new Duck().color("green").height(1.1).material("rubber").sound("quack").wingsState(WingsState.FIXED);
        createDuck(runner, duck);

        //  получаем id созданной утки
        getDuckId(runner);
        //  летаем
        flyDuck(runner, "${duckId}");
        //  проверяем ответ
        String message = "{\n" +
                "  \"message\": \"I can't fly\"\n" +
                "}";
        validateResponseByClass(runner, 200, new Message().message(message));
    }

    @Test(description = "Проверка fly с существующим id с крыльями в неопределенном состоянии")
    @CitrusTest
    public void flyUndefinedDuckTest(@Optional @CitrusResource TestCaseRunner runner) {
        //  создаем утку
        Duck duck = new Duck().color("green").height(1.1).material("rubber").sound("quack").wingsState(WingsState.UNDEFINED);
        createDuck(runner, duck);

        //  получаем id созданной утки
        getDuckId(runner);
        //  летаем
        flyDuck(runner, "${duckId}");
        //  проверяем ответ
        String expectedString = "{\n" +
                "  \"message\": \"Wings are not detected\"\n" +
                "}";
        validateResponseByString(runner, 200, expectedString);
    }

}
