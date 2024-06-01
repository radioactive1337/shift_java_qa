package autotests.Api.controller.DuckActionController;

import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import static com.consol.citrus.dsl.JsonPathSupport.jsonPath;
import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

public class PropertiesTests extends TestNGCitrusSpringSupport {

    private Boolean even;

    @Test(description = "Проверка получения свойств с четным id и материалом wood")
    @CitrusTest
    public void getPropsTest1(@Optional @CitrusResource TestCaseRunner runner) {
        //  создаем утку с четным id
        createEvenDuck(runner, "green", 1.1, "wood", "quack", "ACTIVE");
        //  получаем ее свойства
        getDuckProps(runner);
        //  проверяем ответ
        validateResponse(runner, 200, "green", 1.1, "wood", "quack", "ACTIVE");
    }

    @Test(description = "Проверка получения свойств с нечетным id и материалом rubber")
    @CitrusTest
    public void getPropsTest2(@Optional @CitrusResource TestCaseRunner runner) {
        //  создаем утку с нечетным id
        createOddDuck(runner, "green", 1.1, "rubber", "quack", "ACTIVE");
        //  получаем ее свойства
        getDuckProps(runner);
        //  проверяем ответ
        validateResponse(runner, 200, "green", 1.1, "wood", "quack", "ACTIVE");
    }

    //  проверка свойств утки
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

    //  получение свойств утки
    public void getDuckProps(TestCaseRunner runner) {
        runner.$(http().client("http://localhost:2222")
                .send()
                .get("/api/duck/action/properties")
                .queryParam("id", "${duckId}")
        );
    }

    //  создание четной
    public void createEvenDuck(TestCaseRunner runner, String color, double height, String material, String sound, String wingsState) {
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
        runner.$(http().client("http://localhost:2222")
                        .receive()
                        .response()
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .extract(fromBody().expression("$.id", "duckId"))
                        .validate((message, testContext) -> {
                            try {
                                String id = new ObjectMapper().readTree(message.getPayload().toString()).get("id").toString();
//                        log.info(id);
                                even = Integer.parseInt(id) % 2 == 0;
                            } catch (JsonProcessingException e) {
                                throw new RuntimeException(e);
                            }
                        })
        );
        if (!even) {
            createEvenDuck(runner, color, height, material, sound, wingsState);
        }
    }

    //  создание нечетной
    public void createOddDuck(TestCaseRunner runner, String color, double height, String material, String sound, String wingsState) {
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
        runner.$(http().client("http://localhost:2222")
                        .receive()
                        .response()
                        .message()
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .extract(fromBody().expression("$.id", "duckId"))
                        .validate((message, testContext) -> {
                            try {
                                String id = new ObjectMapper().readTree(message.getPayload().toString()).get("id").toString();
//                        log.info(id);
                                even = Integer.parseInt(id) % 2 == 0;
                            } catch (JsonProcessingException e) {
                                throw new RuntimeException(e);
                            }
                        })
        );
        if (even) {
            createOddDuck(runner, color, height, material, sound, wingsState);
        }
    }

}
