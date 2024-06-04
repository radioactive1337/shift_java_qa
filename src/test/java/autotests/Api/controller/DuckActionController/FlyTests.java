package autotests.Api.controller.DuckActionController;

import autotests.clients.DuckActionsClient;
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
        createDuck(runner, "yellow", 1.1, "rubber", "quack", "ACTIVE");
        //  получаем id созданной утки
        getDuckId(runner);
        //  летаем
        flyDuck(runner, "${duckId}");
        //  проверяем ответ
        validateMessageResponse(runner, 200, "I am flying :)");
    }

    @Test(description = "Проверка fly с существующим id со связанными крыльями")
    @CitrusTest
    public void flyFixedDuckTest(@Optional @CitrusResource TestCaseRunner runner) {
        //  создаем утку
        createDuck(runner, "yellow", 1.1, "rubber", "quack", "FIXED");
        //  получаем id созданной утки
        getDuckId(runner);
        //  летаем
        flyDuck(runner, "${duckId}");
        //  проверяем ответ
        validateMessageResponse(runner, 200, "I can not fly :C");
    }

    @Test(description = "Проверка fly с существующим id с крыльями в неопределенном состоянии")
    @CitrusTest
    public void flyUndefinedDuckTest(@Optional @CitrusResource TestCaseRunner runner) {
        //  создаем утку
        createDuck(runner, "yellow", 1.1, "rubber", "quack", "UNDEFINED");
        //  получаем id созданной утки
        getDuckId(runner);
        //  летаем
        flyDuck(runner, "${duckId}");
        //  проверяем ответ
        validateMessageResponse(runner, 200, "Wings are not detected :(");
    }

}
