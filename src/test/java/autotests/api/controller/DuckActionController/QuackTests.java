package autotests.api.controller.DuckActionController;

import autotests.clients.DuckActionsClient;
import autotests.payloads.Duck;
import autotests.payloads.WingsState;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class QuackTests extends DuckActionsClient {

    @Test(description = "Проверка кряканья с четным id")
    @CitrusTest
    public void quackEvenDuckTest(@Optional @CitrusResource TestCaseRunner runner) {
        //  создаем утку с четным id
        Duck duck = new Duck().color("green").height(1.1).material("rubber").sound("quack").wingsState(WingsState.ACTIVE);
        createEvenDuck(runner, duck);
        //  крякаем
        quackDuck(runner, "${duckId}", "1", "1");
        //  проверка ответа
        String expectedString = "{\n" +
                "  \"sound\": \"quack\"\n" +
                "}";
        validateResponseByString(runner, 200, expectedString);
    }

    @Test(description = "Проверка кряканья с нечетным id")
    @CitrusTest
    public void quackOddDuckTest(@Optional @CitrusResource TestCaseRunner runner) {
        //  создаем утку с нечетным id
        Duck duck = new Duck().color("green").height(1.1).material("rubber").sound("quack").wingsState(WingsState.ACTIVE);
        createOddDuck(runner, duck);
        //  крякаем
        quackDuck(runner, "${duckId}", "1", "1");
        //  проверка ответа
        validateResponseByJson(runner, 200, "test_responses/quackTest/quackOddDuckResponse.json");
    }

}
