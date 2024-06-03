package autotests.Api.controller.DuckController;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.http.MediaType;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.dsl.JsonPathSupport.jsonPath;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class CreateDuckTests extends TestNGCitrusSpringSupport {

    @Test(description = "Проверка создания утки с material = rubber ")
    @CitrusTest
    public void createRubberDuckTest(@Optional @CitrusResource TestCaseRunner runner) {
        //  создаем утку
        createDuck(runner, "green", 1.1, "rubber", "quack", "ACTIVE");
        //  проверяем ответ
        validateResponse(runner, 200, "green", 1.1, "rubber", "quack", "ACTIVE");
    }

    @Test(description = "Проверка создания утки с material = wood ")
    @CitrusTest
    public void createWoodenDuckTest(@Optional @CitrusResource TestCaseRunner runner) {
        //  создаем утку
        createDuck(runner, "green", 1.1, "wood", "quack", "ACTIVE");
        //  проверяем ответ
        validateResponse(runner, 200, "green", 1.1, "wood", "quack", "ACTIVE");
    }

    //  создание утки
    public void createDuck(TestCaseRunner runner, String color, double height, String material, String sound, String wingsState) {
        runner.$(http().client("http://localhost:2222")
                .send()
                .post("/api/duck/create")
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("{\n" + "  \"color\": \"" + color + "\",\n"
                        + "  \"height\": " + height + ",\n"
                        + "  \"material\": \"" + material + "\",\n"
                        + "  \"sound\": \"" + sound + "\",\n"
                        + "  \"wingsState\": \"" + wingsState
                        + "\"\n" + "}")
        );
    }

    //  проверка ответа
    public void validateResponse(TestCaseRunner runner, int statusCode, String color, double height, String material, String sound, String wingsState) {
        runner.$(http().client("http://localhost:2222")
                .receive()
                .response()
                .message()
                .statusCode(statusCode)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .validate(jsonPath().expression("$.color", color))
                .validate(jsonPath().expression("$.height", height))
                .validate(jsonPath().expression("$.material", material))
                .validate(jsonPath().expression("$.sound", sound))
                .validate(jsonPath().expression("$.wingsState", wingsState))
        );
    }

}
