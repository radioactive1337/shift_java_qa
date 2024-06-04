package autotests.Api.controller.DuckActionController;

import autotests.clients.DuckActionsClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;


public class PropertiesTests extends DuckActionsClient {

    @Test(description = "Проверка получения свойств с четным id и материалом wood")
    @CitrusTest
    public void getEvenWoodenDuckPropsTest(@Optional @CitrusResource TestCaseRunner runner) {
        //  создаем утку с четным id
        createEvenDuck(runner, "green", 1.1, "wood", "quack", "ACTIVE");
        //  получаем ее свойства
        getDuckProps(runner, "${duckId}");
        //  проверяем ответ
        validateDuckResponse(runner, 200, "green", 1.1, "wood", "quack", "ACTIVE");
    }

    @Test(description = "Проверка получения свойств с нечетным id и материалом rubber")
    @CitrusTest
    public void getOddRubberDuckPropsTest(@Optional @CitrusResource TestCaseRunner runner) {
        //  создаем утку с нечетным id
        createOddDuck(runner, "green", 1.1, "rubber", "quack", "ACTIVE");
        //  получаем ее свойства
        getDuckProps(runner, "${duckId}");
        //  проверяем ответ
        validateDuckResponse(runner, 200, "green", 1.1, "wood", "quack", "ACTIVE");
    }

}
