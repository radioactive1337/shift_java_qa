package autotests.api.controller.DuckController;

import autotests.payloads.Duck;
import autotests.payloads.WingsState;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.testng.CitrusParameters;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;
import autotests.clients.DuckActionsClient;

@Epic("Duck controller")
@Feature("/api/duck/create")
public class CreateDuckTests extends DuckActionsClient {

    @DataProvider(name = "duckData")
    public Object[][] duckData() {
        return new Object[][]{
                {"green", 1.12, "rubber", "quack", WingsState.ACTIVE, null},
                {"blue", 2.34, "plastic", "krya", WingsState.FIXED, null},
                {"red", 3.45, "wood", "privet", WingsState.ACTIVE, null},
                {"yellow", 4.56, "metal", "hello", WingsState.UNDEFINED, null},
                {"purple", 5.67, "fabric", "hola", WingsState.ACTIVE, null}
        };
    }

    @Test(description = "Проверка создания утки с material = rubber", dataProvider = "duckData")
    @CitrusTest
    @CitrusParameters({"color", "height", "material", "sound", "wingsState", "runner"})
    public void createRubberDuckTest(String color, double height, String material, String sound, WingsState wingsState, @Optional @CitrusResource TestCaseRunner runner) {
        Duck duck = new Duck().color(color).height(height).material(material).sound(sound).wingsState(wingsState);

        //  запрос на создание утки + очистка бд после теста
        requestCreateDuck(runner, duck);
        writeIdFromDb(runner, "select * from duck where height = " + height);
        finallyClearDb(runner);

        //  проверяем ответ
        validateResponseByClass(runner, 200, duck.id("@isNumber()@"));

        //  проверяем в бд
        validateDatabaseDuck(runner, "${duckId}", color, height, material, sound, wingsState);
    }

}
