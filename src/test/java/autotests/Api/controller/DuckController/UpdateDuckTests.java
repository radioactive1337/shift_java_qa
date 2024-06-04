package autotests.tests.Api.controller.DuckController;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;
import autotests.clients.DuckActionsClient;

public class UpdateDuckTests extends DuckActionsClient {

    @Test(description = "Проверка обновления цвета и высоты утки")
    @CitrusTest
    public void updateColorAndHeightDuckTest(@Optional @CitrusResource TestCaseRunner runner) {
        //  создаем утку
        createDuck(runner, "yellow", 1.1, "rubber", "quack", "ACTIVE");
        //  получаем id созданной утки
        getDuckId(runner);
        //  обновляем 2 поля
        updateDuck(runner, "rainbow", "9.9", "${duckId}", "rubber", "quack", "ACTIVE");
//        //  проверяем ответ
        validateMessageResponse(runner, 200, "Duck with id = ${duckId} is updated");
//        //  CHECK DB --->
    }

    @Test(description = "Проверка обновления цвета и звука утки")
    @CitrusTest
    public void updateColorAndSoundDuckTest(@Optional @CitrusResource TestCaseRunner runner) {
        //  создаем утку
        createDuck(runner, "yellow", 1.1, "rubber", "quack", "ACTIVE");
        //  получаем id созданной утки
        getDuckId(runner);
        //  обновляем 2 поля
        updateDuck(runner, "green", "1.1", "${duckId}", "rubber", "mew", "ACTIVE");
//        //  проверяем ответ
        validateMessageResponse(runner, 200, "Duck with id = ${duckId} is updated");
//        //  CHECK DB --->
    }

}
