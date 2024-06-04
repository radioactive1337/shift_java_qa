package autotests.Api.controller.DuckActionController;

import autotests.clients.DuckActionsClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class SwimTests extends DuckActionsClient {
    @Test(description = "Проверка swim с существующим id")
    @CitrusTest
    public void swimExistedDuckTest(@Optional @CitrusResource TestCaseRunner runner) {
        //  создаем утку
        createDuck(runner, "yellow", 1.1, "rubber", "quack", "ACTIVE");
        //  получаем id созданной утки
        getDuckId(runner);
        //  плаваем
        swimDuck(runner, "${duckId}");
        //  проверяем ответ
        validateMessageResponse(runner, 200, "I'm swimming");
    }

    @Test(description = "Проверка swim с несуществующим id")
    @CitrusTest
    public void swimNotExistedDuckTest(@Optional @CitrusResource TestCaseRunner runner) {
        //  плаваем
        swimDuck(runner, "666");
        //  проверяем ответ
        validateMessageResponse(runner, 404, "Paws are not found ((((");
    }

}
