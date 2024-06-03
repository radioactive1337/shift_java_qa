package autotests.Api.controller.DuckController;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import org.springframework.http.MediaType;
import org.testng.Assert;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.dsl.JsonPathSupport.jsonPath;
import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class DeleteDuckTests extends TestNGCitrusSpringSupport {

    @Test(description = "Проверка удаления утки")
    @CitrusTest
    public void deleteDuckTest(@Optional @CitrusResource TestCaseRunner runner) {
        //  создаем утку
        createDuck(runner, "yellow", 2.2, "rubber", "quack", "ACTIVE");
        //  получаем id созданной утки
        getDuckId(runner);
        //  удаляем утку
        deleteDuck(runner);
        //  проверяем ответ
        validateResponse(runner, 200, "Duck is deleted");
        //  проверяем в бд
        checkInDb(runner);
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
                        + "\"\n" + "}"));
    }

    //  удаление утки
    public void deleteDuck(TestCaseRunner runner) {
        runner.$(http().client("http://localhost:2222")
                .send()
                .delete("/api/duck/delete")
                .queryParam("id", "${duckId}")
        );
    }

    //  проверка отсутствия утки в бд
    public void checkInDb(TestCaseRunner runner) {
        runner.$(http().client("http://localhost:2222")
                .send()
                .get("/api/duck/getAllIds")
        );
        runner.$(http().client("http://localhost:2222")
                .receive()
                .response()
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .validate(((message, testContext) -> Assert.assertFalse(message.getPayload().toString().contains("${duckId}"))))
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
