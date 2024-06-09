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
@Feature("/api/duck/action/properties")
public class PropertiesTests extends DuckActionsClient {

    @Test(description = "Проверка получения свойств с четным id и материалом wood")
    @CitrusTest
    public void getEvenWoodenDuckPropsTest(@Optional @CitrusResource TestCaseRunner runner) {
        //  создаем утку с четным id + очитска бд в конце теста
        runner.variable("duckId", "citrus:randomNumber(3, false)1");
        clearDB(runner, "${duckId}");
        databaseUpdate(runner, "insert into duck values (${duckId}, 'green', 1.1, 'wood', 'quack', 'ACTIVE')");

        //  получаем ее свойства
        getDuckProps(runner, "${duckId}");

        //  проверяем ответ
        validateResponseByJson(runner, 200, "test_responses/propertiesTest/getEvenWoodenDuckPropsResponse.json");
    }

    @Test(description = "Проверка получения свойств с нечетным id и материалом rubber")
    @CitrusTest
    public void getOddRubberDuckPropsTest(@Optional @CitrusResource TestCaseRunner runner) {
        //  создаем утку с нечетным id + очитска бд в конце теста
        runner.variable("duckId", "citrus:randomNumber(3, false)2");
        clearDB(runner, "${duckId}");
        databaseUpdate(runner, "insert into duck values (${duckId}, 'green', 1.1, 'rubber', 'quack', 'ACTIVE')");

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
