package autotests.api.controller.DuckActionController;

import autotests.clients.DuckActionsClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

@Epic("Duck action controller")
@Feature("/api/duck/action/quack")
public class QuackTests extends DuckActionsClient {

    @Test(description = "Проверка кряканья с четным id")
    @CitrusTest
    public void quackEvenDuckTest(@Optional @CitrusResource TestCaseRunner runner) {
        //  создаем утку с четным id + очитска бд в конце теста
        runner.variable("duckId", "citrus:randomNumber(4, false)2");
        clearDB(runner, "${duckId}");
        databaseUpdate(runner, "insert into duck values (${duckId}, 'green', 1.1, 'rubber', 'quack', 'ACTIVE')");

        //  крякаем
        quackDuck(runner, "${duckId}", "1", "1");

        //  проверка ответа
        validateResponseByString(runner, 200, "{\n" +
                "  \"sound\": \"quack\"\n" +
                "}");
    }

    @Test(description = "Проверка кряканья с нечетным id")
    @CitrusTest
    public void quackOddDuckTest(@Optional @CitrusResource TestCaseRunner runner) {
        //  создаем утку с нечетным id + очитска бд в конце теста
        runner.variable("duckId", "citrus:randomNumber(4, false)1");
        clearDB(runner, "${duckId}");
        databaseUpdate(runner, "insert into duck values (${duckId}, 'green', 1.1, 'rubber', 'quack', 'ACTIVE')");

        //  крякаем
        quackDuck(runner, "${duckId}", "1", "1");

        //  проверка ответа
        validateResponseByString(runner, 200, "{\n" +
                "  \"sound\": \"quack\"\n" +
                "}");
    }

    @Test(description = "Проверка кряканья с нечетным id")
    @CitrusTest
    public void quack1(@Optional @CitrusResource TestCaseRunner runner) {
        //  создаем утку с нечетным id + очитска бд в конце теста
        runner.variable("duckId", "citrus:randomNumber(4, false)1");
        clearDB(runner, "${duckId}");
        databaseUpdate(runner, "insert into duck values (${duckId}, 'green', 1.1, 'rubber', 'quack', 'ACTIVE')");

        //  крякаем
        quackDuck(runner, "${duckId}", "1", "1");

        //  проверка ответа
        validateResponseByString(runner, 200, "{\n" +
                "  \"sound\": \"quack\"\n" +
                "}");
    }

}
