package autotests.api.controller.DuckController;

import autotests.payloads.Duck;
import autotests.payloads.WingsState;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;
import autotests.clients.DuckActionsClient;

@Epic("Duck controller")
@Feature("/api/duck/create")
public class CreateDuckTests extends DuckActionsClient {

    @Test(description = "Проверка создания утки с material = rubber")
    @CitrusTest
    public void createRubberDuckTest(@Optional @CitrusResource TestCaseRunner runner) {
        String color = "green";
        double height = 1.12;
        String material = "rubber";
        String sound = "quack";
        WingsState wingsState = WingsState.ACTIVE;

        //  запрос на создание утки + очистка бд после теста
        Duck duck = new Duck().color(color).height(height).material(material).sound(sound).wingsState(wingsState);
        createDuck(runner, duck);
        writeIdFromDb(runner, "select * from duck where height = " + height);
        clearDB(runner, "${duckId}");

        //  проверяем ответ
        Duck expectedPayload = new Duck().id("@isNumber()@").color(color).height(height).material(material).sound(sound).wingsState(wingsState);
        validateResponseByClass(runner, 200, expectedPayload);

        //  проверяем в бд
        databaseValidateDuck(runner, "${duckId}", color, height, material, sound, wingsState);
    }

    @Test(description = "Проверка создания утки с material = wood")
    @CitrusTest
    public void createWoodenDuckTest(@Optional @CitrusResource TestCaseRunner runner) {
        String color = "green";
        double height = 1.123;
        String material = "wood";
        String sound = "quack";
        WingsState wingsState = WingsState.ACTIVE;

        //  запрос на создание утки + очистка бд после теста
        Duck duck = new Duck().color(color).height(height).material(material).sound(sound).wingsState(wingsState);
        createDuck(runner, duck);
        writeIdFromDb(runner, "select * from duck where height = " + height);
        clearDB(runner, "${duckId}");

        //  проверяем ответ
        String expectedString = "{\n" +
                "  \"id\": \"@isNumber()@\",\n" +
                "  \"color\": \"green\",\n" +
                "  \"height\": 1.123,\n" +
                "  \"material\": \"wood\",\n" +
                "  \"sound\": \"quack\",\n" +
                "  \"wingsState\": \"ACTIVE\"\n" +
                "}";
        validateResponseByString(runner, 200, expectedString);

        //  проверяем в бд
        databaseValidateDuck(runner, "${duckId}", color, height, material, sound, wingsState);
    }

}
