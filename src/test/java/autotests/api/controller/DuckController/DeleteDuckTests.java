package autotests.api.controller.DuckController;

import autotests.clients.DuckActionsClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

public class DeleteDuckTests extends DuckActionsClient {

    @Test(description = "Проверка удаления утки")
    @CitrusTest
    public void deleteDuckTest(@Optional @CitrusResource TestCaseRunner runner) {
        //  создаем утку
        createDuck(runner, "yellow", 2.2, "rubber", "quack", "ACTIVE");
        //  получаем id созданной утки
        getDuckId(runner);
        //  удаляем утку
        deleteDuck(runner, "${duckId}");
        //  проверяем ответ
        validateMessageResponse(runner, 200, "Duck is deleted");
        //  проверяем в бд
        checkInDb(runner);
    }

}
