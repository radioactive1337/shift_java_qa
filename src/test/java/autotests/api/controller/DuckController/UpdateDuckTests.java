package autotests.api.controller.DuckController;

import autotests.payloads.Duck;
import autotests.payloads.WingsState;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;
import autotests.clients.DuckActionsClient;
import autotests.payloads.Message;

import java.util.concurrent.atomic.AtomicInteger;

public class UpdateDuckTests extends DuckActionsClient {

    @Test(description = "Проверка обновления цвета и высоты утки")
    @CitrusTest
    public void updateColorAndHeightDuckTest(@Optional @CitrusResource TestCaseRunner runner) {
        //  создаем утку
        Duck duck = new Duck().color("green").height(1.1).material("rubber").sound("quack").wingsState(WingsState.ACTIVE);
        createDuck(runner, duck);

        //  получаем id созданной утки
        getDuckId(runner);
        AtomicInteger duckid = new AtomicInteger(0);
        run(testContext -> duckid.set(Integer.parseInt(testContext.getVariable("duckId"))));
        //  обновляем 2 поля
        updateDuck(runner, "rainbow", "9.9", "${duckId}", "rubber", "quack", "ACTIVE");
        // проверяем ответ
        String expectedMessage = String.format("Duck with id = %d is updated", duckid.get());
        validateResponseByString(runner, 200, "{\n" +
                "  \"message\": \"" + expectedMessage + "\"\n" +
                "}");
    }

    @Test(description = "Проверка обновления цвета и звука утки")
    @CitrusTest
    public void updateColorAndSoundDuckTest(@Optional @CitrusResource TestCaseRunner runner) {
        //  создаем утку
        Duck duck = new Duck().color("green").height(1.1).material("rubber").sound("quack").wingsState(WingsState.ACTIVE);
        createDuck(runner, duck);

        //  получаем id созданной утки
        getDuckId(runner);
        AtomicInteger duckid = new AtomicInteger(0);
        run(testContext -> duckid.set(Integer.parseInt(testContext.getVariable("duckId"))));
        //  обновляем 2 поля
        updateDuck(runner, "green", "1.1", "${duckId}", "rubber", "mew", "ACTIVE");
        //  проверяем ответ
        String expectedMessage = String.format("Duck with id = %d is updated", duckid.get());
        validateResponseByClass(runner, 200, new Message().message(expectedMessage));
    }

}
