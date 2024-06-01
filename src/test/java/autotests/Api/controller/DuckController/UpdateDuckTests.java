package autotests.Api.controller.DuckController;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.http.MediaType;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.dsl.JsonPathSupport.jsonPath;
import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class UpdateDuckTests extends TestNGCitrusSpringSupport {

    @Test(description = "Проверка обновления цвета и высоты утки")
    @CitrusTest
    public void updateDuckTest1(@Optional @CitrusResource TestCaseRunner runner) {
        //  создаем утку
        createDuck(runner, "yellow", 1.1, "rubber", "quack", "ACTIVE");
        //  получаем id созданной утки
        getDuckId(runner);
        //  обновляем 2 поля
        updateDuck(runner, "rainbow", "9.9", "${duckId}", "rubber", "quack");
        //  проверяем ответ
        validateResponse(runner, 200, "${duckId}");
        //  получаем пропсы обновленный утки
        getDuckProps(runner);
        //  проверяем ответ
        validateDuckResponse(runner, 200, "rainbow", 9.9, "rubber", "quack");
    }

    @Test(description = "Проверка обновления цвета и звука утки")
    @CitrusTest
    public void updateDuckTest2(@Optional @CitrusResource TestCaseRunner runner) {
        //  создаем утку
        createDuck(runner, "yellow", 1.1, "rubber", "quack", "ACTIVE");
        //  получаем id созданной утки
        getDuckId(runner);
        //  обновляем 2 поля
        updateDuck(runner, "green", "1.1", "${duckId}", "rubber", "mew");
        //  проверяем ответ
        validateResponse(runner, 200, "${duckId}");
        //  получаем пропсы обновленный утки
        getDuckProps(runner);
        //  проверяем ответ
        validateDuckResponse(runner, 200, "green", 1.1, "rubber", "mew");
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

    //  обновление утки
    public void updateDuck(TestCaseRunner runner, String color, String height, String id, String material, String sound) {
        runner.$(http().client("http://localhost:2222")
                .send()
                .put("/api/duck/update")
                .queryParam("color", color)
                .queryParam("height", height)
                .queryParam("id", id)
                .queryParam("material", material)
                .queryParam("sound", sound)
        );
    }

    //  получение свойств утки
    public void getDuckProps(TestCaseRunner runner) {
        runner.$(http().client("http://localhost:2222")
                .send()
                .get("/api/duck/action/properties")
                .queryParam("id", "${duckId}")
        );
    }

    //  получение id утки
    public void getDuckId(TestCaseRunner runner) {
        runner.$(http().client("http://localhost:2222")
                .receive()
                .response()
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract(fromBody().expression("$.id", "duckId"))
        );
    }

    //  проверка ответа
    public void validateResponse(TestCaseRunner runner, int statusCode, String id) {
        runner.$(http().client("http://localhost:2222")
                .receive()
                .response()
                .message()
                .statusCode(statusCode)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .validate(jsonPath().expression("$.message", "Duck with id = " + id + " is updated"))
        );
    }

    //  проверка свойств утки
    public void validateDuckResponse(TestCaseRunner runner, int statusCode, String color, double height, String material, String sound) {
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
        );
    }

}
