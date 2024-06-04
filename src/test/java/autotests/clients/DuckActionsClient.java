package autotests.clients;

import autotests.EndpointConfig;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import com.consol.citrus.http.client.HttpClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.testng.Assert;

import static com.consol.citrus.dsl.JsonPathSupport.jsonPath;
import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

@ContextConfiguration(classes = {EndpointConfig.class})
public class DuckActionsClient extends TestNGCitrusSpringSupport {

    @Autowired
    public HttpClient yellowDuckService;

    public boolean even;

    //  создание утки
    public void createDuck(TestCaseRunner runner, String color, double height, String material, String sound, String wingsState) {
        runner.$(http().client(yellowDuckService)
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
    public void updateDuck(TestCaseRunner runner, String color, String height, String id, String material, String sound, String wingsState) {
        runner.$(http().client(yellowDuckService)
                .send()
                .put("/api/duck/update")
                .queryParam("color", color)
                .queryParam("height", height)
                .queryParam("id", id)
                .queryParam("material", material)
                .queryParam("sound", sound)
                .queryParam("wingsState", wingsState)
        );
    }

    //  удаление утки
    public void deleteDuck(TestCaseRunner runner) {
        runner.$(http().client("http://localhost:2222")
                .send()
                .delete("/api/duck/delete")
                .queryParam("id", "${duckId}")
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

    //  плыть
    public void swimDuck(TestCaseRunner runner, String id) {
        runner.$(http().client("http://localhost:2222")
                .send()
                .get("/api/duck/action/swim")
                .queryParam("id", id)
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
        runner.$(http().client(yellowDuckService)
                .receive()
                .response()
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract(fromBody().expression("$.id", "duckId"))
        );
    }

    //  проверка ответа (сообщение)
    public void validateMessageResponse(TestCaseRunner runner, int statusCode, String message) {
        runner.$(http().client("http://localhost:2222")
                .receive()
                .response()
                .message()
                .statusCode(statusCode)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .validate(jsonPath().expression("$.message", message))
        );
    }

    //  проверка ответа (тело утки)
    public void validateDuckResponse(TestCaseRunner runner, int statusCode, String color, double height, String material, String sound, String wingsState) {
        runner.$(http().client(yellowDuckService)
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

    //  проверка ответа (звук)
    public void validateSoundResponse(TestCaseRunner runner, int statusCode, String sound) {
        runner.$(http().client("http://localhost:2222")
                .receive()
                .response()
                .message()
                .statusCode(statusCode)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .validate(jsonPath().expression("$.sound", sound))
        );
    }

}