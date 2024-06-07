package autotests.api.controller.DuckActionController;

import autotests.clients.DuckActionsClient;
import autotests.payloads.Duck;
import autotests.payloads.WingsState;
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
        Duck duck = new Duck().color("green").height(1.1).material("wood").sound("quack").wingsState(WingsState.ACTIVE);
        createEvenDuck(runner, duck);
        //  получаем ее свойства
        getDuckProps(runner, "${duckId}");
        //  проверяем ответ
        validateResponseByJson(runner, 200, "test_responses/propertiesTest/getEvenWoodenDuckPropsResponse.json");
    }

    @Test(description = "Проверка получения свойств с нечетным id и материалом rubber")
    @CitrusTest
    public void getOddRubberDuckPropsTest(@Optional @CitrusResource TestCaseRunner runner) {
        //  создаем утку с нечетным id
        Duck duck = new Duck().color("green").height(1.1).material("rubber").sound("quack").wingsState(WingsState.ACTIVE);
        createOddDuck(runner, duck);
        //  получаем ее свойства
        getDuckProps(runner, "${duckId}");
        //  проверяем ответ
        String expectedString = "{\n" +
                "  \"color\": \"green\",\n" +
                "  \"height\": 1.1,\n" +
                "  \"material\": \"rubber\",\n" +
                "  \"sound\": \"quack\",\n" +
                "  \"wingsState\": \"ACTIVE\"\n" +
                "}";
        validateResponseByString(runner, 200, expectedString);
    }

}
