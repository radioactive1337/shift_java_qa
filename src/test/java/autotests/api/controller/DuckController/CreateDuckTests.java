package autotests.api.controller.DuckController;

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
        createDuck(runner, "green", 1.1, "rubber", "quack", "ACTIVE");
        //  проверяем ответ
        validateDuckResponse(runner, 200, "green", 1.1, "rubber", "quack", "ACTIVE");
    }

    @Test(description = "Проверка создания утки с material = wood ")
    @CitrusTest
    public void createWoodenDuckTest(@Optional @CitrusResource TestCaseRunner runner) {
        //  создаем утку
        createDuck(runner, "green", 1.1, "wood", "quack", "ACTIVE");
        //  проверяем ответ
        validateDuckResponse(runner, 200, "green", 1.1, "wood", "quack", "ACTIVE");
    }

}
