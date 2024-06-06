package autotests.api.controller.DuckActionController;

import autotests.clients.DuckActionsClient;
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
        createEvenDuck(runner, "green", 1.1, "wood", "quack", "ACTIVE");
        //  крякаем
        quackDuck(runner, "${duckId}", "1", "1");
        //  проверка ответа
//        validateSoundResponse(runner, 200, "quack");
    }

    @Test(description = "Проверка кряканья с нечетным id")
    @CitrusTest
    public void quackOddDuckTest(@Optional @CitrusResource TestCaseRunner runner) {
        //  создаем утку с нечетным id
        createOddDuck(runner, "green", 1.1, "rubber", "quack", "ACTIVE");
        //  крякаем
        quackDuck(runner, "${duckId}", "1", "1");
        //  проверка ответа
//        validateSoundResponse(runner, 200, "quack");
    }

}
