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

public class QuackTests extends TestNGCitrusSpringSupport {

    private Boolean even;

    @Test(description = "Проверка кряканья с четным id")
    @CitrusTest
    public void quackTest1(@Optional @CitrusResource TestCaseRunner runner) {
        //  создаем утку с четным id
        createEvenDuck(runner, "green", 1.1, "wood", "quack", "ACTIVE");
        //  крякаем
        quackDuck(runner);
        //  проверка ответа
        validateResponse(runner, 200, "quack");
    }

    @Test(description = "Проверка кряканья с нечетным id")
    @CitrusTest
    public void quackTest2(@Optional @CitrusResource TestCaseRunner runner) {
        //  создаем утку с нечетным id
        createOddDuck(runner, "green", 1.1, "rubber", "quack", "ACTIVE");
        //  крякаем
        quackDuck(runner);
        //  проверка ответа
        validateResponse(runner, 200, "quack");
    }

    //  проверка ответа
    public void validateResponse(TestCaseRunner runner, int statusCode, String sound) {
        runner.$(http().client("http://localhost:2222")
                .receive()
                .response()
                .message()
                .statusCode(statusCode)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .validate(jsonPath().expression("$.sound", sound))
        );
    }

    //  крякать
    public void quackDuck(TestCaseRunner runner) {
        runner.$(http().client("http://localhost:2222")
                .send()
                .get("/api/duck/action/quack")
                .queryParam("id", "${duckId}")
                .queryParam("repetitionCount", "1")
                .queryParam("soundCount", "1")
        );
    }

    //  создание четной утки
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

    //  создание нечетной утки
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
