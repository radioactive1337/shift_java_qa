package autotests.Api.controller.DuckActionController;

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

public class FlyTests extends TestNGCitrusSpringSupport {

    @Test(description = "Проверка fly с существующим id с активными крыльями")
    @CitrusTest
    public void flyActiveDuckTest(@Optional @CitrusResource TestCaseRunner runner) {
        //  создаем утку
        createDuck(runner, "yellow", 1.1, "rubber", "quack", "ACTIVE");
        //  получаем id созданной утки
        getDuckId(runner);
        //  летаем
        flyDuck(runner);
        //  проверяем ответ
        validateResponse(runner, 200, "I am flying :)");
    }

    @Test(description = "Проверка fly с существующим id со связанными крыльями")
    @CitrusTest
    public void flyFixedDuckTest(@Optional @CitrusResource TestCaseRunner runner) {
        //  создаем утку
        createDuck(runner, "yellow", 1.1, "rubber", "quack", "FIXED");
        //  получаем id созданной утки
        getDuckId(runner);
        //  летаем
        flyDuck(runner);
        //  проверяем ответ
        validateResponse(runner, 200, "I can not fly :C");
    }

    @Test(description = "Проверка fly с существующим id с крыльями в неопределенном состоянии")
    @CitrusTest
    public void flyUndefinedDuckTest(@Optional @CitrusResource TestCaseRunner runner) {
        //  создаем утку
        createDuck(runner, "yellow", 1.1, "rubber", "quack", "UNDEFINED");
        //  получаем id созданной утки
        getDuckId(runner);
        //  летаем
        flyDuck(runner);
        //  проверяем ответ
        validateResponse(runner, 200, "Wings are not detected :(");
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

    //  лететь
    public void flyDuck(TestCaseRunner runner) {
        runner.$(http().client("http://localhost:2222")
                .send()
                .get("/api/duck/action/fly")
                .queryParam("id", "${duckId}")
        );
    }

    //  проверка ответа
    public void validateResponse(TestCaseRunner runner, int statusCode, String message) {
        runner.$(http().client("http://localhost:2222")
                .receive()
                .response()
                .message()
                .statusCode(statusCode)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .validate(jsonPath().expression("$.message", message))
        );
    }

}
