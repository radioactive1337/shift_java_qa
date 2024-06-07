package autotests.clients;

import autotests.EndpointConfig;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.message.builder.ObjectMappingPayloadBuilder;
import com.consol.citrus.testng.spring.TestNGCitrusSpringSupport;
import com.consol.citrus.http.client.HttpClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.testng.Assert;

import static com.consol.citrus.dsl.MessageSupport.MessageBodySupport.fromBody;
import static com.consol.citrus.http.actions.HttpActionBuilder.http;

@ContextConfiguration(classes = {EndpointConfig.class})
public class DuckActionsClient extends TestNGCitrusSpringSupport {

    @Autowired
    public HttpClient yellowDuckService;

    public boolean even;

    /**
     * создание утки
     */
    public void createDuck(TestCaseRunner runner, Object body) {
        runner.$(http().client(yellowDuckService)
                .send()
                .post("/api/duck/create")
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new ObjectMappingPayloadBuilder(body, new ObjectMapper()))
        );
    }

    /**
     * обновление утки
     */
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

    /**
     * удаление утки
     */
    public void deleteDuck(TestCaseRunner runner, String id) {
        runner.$(http().client(yellowDuckService)
                .send()
                .delete("/api/duck/delete")
                .queryParam("id", id)
        );
    }

    /**
     * лететь
     */
    public void flyDuck(TestCaseRunner runner, String id) {
        runner.$(http().client(yellowDuckService)
                .send()
                .get("/api/duck/action/fly")
                .queryParam("id", id)
        );
    }

    /**
     * плыть
     */
    public void swimDuck(TestCaseRunner runner, String id) {
        runner.$(http().client(yellowDuckService)
                .send()
                .get("/api/duck/action/swim")
                .queryParam("id", id)
        );
    }

    /**
     * получение свойств утки
     */
    public void getDuckProps(TestCaseRunner runner, String id) {
        runner.$(http().client(yellowDuckService)
                .send()
                .get("/api/duck/action/properties")
                .queryParam("id", id)
        );
    }

    /**
     * крякать
     */
    public void quackDuck(TestCaseRunner runner, String id, String repetitionCount, String soundCount) {
        runner.$(http().client(yellowDuckService)
                .send()
                .get("/api/duck/action/quack")
                .queryParam("id", id)
                .queryParam("repetitionCount", repetitionCount)
                .queryParam("soundCount", soundCount)
        );
    }

    /**
     * создание нечетной утки
     */
    public void createOddDuck(TestCaseRunner runner, Object body) {
        runner.$(http().client(yellowDuckService)
                .send()
                .post("/api/duck/create")
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new ObjectMappingPayloadBuilder(body, new ObjectMapper()))

        );
        runner.$(http().client(yellowDuckService)
                .receive()
                .response()
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract(fromBody().expression("$.id", "duckId"))
                .validate((message, testContext) -> {
                    try {
                        String id = new ObjectMapper().readTree(message.getPayload().toString()).get("id").toString();
                        even = Integer.parseInt(id) % 2 == 0;
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                })
        );
        if (even) {
            createOddDuck(runner, body);
        }
    }

    /**
     * создание четной утки
     */
    public void createEvenDuck(TestCaseRunner runner, Object body) {
        runner.$(http().client(yellowDuckService)
                .send()
                .post("/api/duck/create")
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new ObjectMappingPayloadBuilder(body, new ObjectMapper()))
        );
        runner.$(http().client(yellowDuckService)
                .receive()
                .response()
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract(fromBody().expression("$.id", "duckId"))
                .validate((message, testContext) -> {
                    try {
                        String id = new ObjectMapper().readTree(message.getPayload().toString()).get("id").toString();
                        even = Integer.parseInt(id) % 2 == 0;
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                })
        );
        if (!even) {
            createEvenDuck(runner, body);
        }
    }

    /**
     * проверка отсутствия утки в бд
     */
    public void checkInDb(TestCaseRunner runner) {
        runner.$(http().client(yellowDuckService)
                .send()
                .get("/api/duck/getAllIds")
        );
        runner.$(http().client(yellowDuckService)
                .receive()
                .response()
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .validate(((message, testContext) -> Assert.assertFalse(message.getPayload().toString().contains("${duckId}"))))
        );
    }

    /**
     * получение id утки
     */
    public void getDuckId(TestCaseRunner runner) {
        runner.$(http().client(yellowDuckService)
                .receive()
                .response()
                .message()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract(fromBody().expression("$.id", "duckId"))
        );
    }

    /**
     * валидация ответа с помощью строки
     */
    public void validateResponseByString(TestCaseRunner runner, int statusCode, String expectedString) {
        runner.$(http()
                .client(yellowDuckService)
                .receive()
                .response()
                .message()
                .statusCode(statusCode)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(expectedString)
        );
    }

    /**
     * валидация ответа с помощью json файла
     */
    public void validateResponseByJson(TestCaseRunner runner, int statusCode, String expectedPayload) {
        runner.$(http()
                .client(yellowDuckService)
                .receive()
                .response()
                .message()
                .statusCode(statusCode)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new ClassPathResource(expectedPayload))
        );
    }

    /**
     * валидация с помощью класса
     */
    public void validateResponseByClass(TestCaseRunner runner, int statusCode, Object expectedPayload) {
        runner.$(http()
                .client(yellowDuckService)
                .receive()
                .response()
                .message()
                .statusCode(statusCode)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new ObjectMappingPayloadBuilder(expectedPayload, new ObjectMapper()))
        );
    }

    /**
     * валидация ответа с помощью jsonschema
     */
//    public void validateResponseByJsonSchema(TestCaseRunner runner, int statusCode, String schema) {
//        runner.$(http()
//                .client(yellowDuckService)
//                .receive()
//                .response()
//                .message()
//                .statusCode(statusCode)
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .validate(json().schemaValidation(true).schema(schema))
//        );
//    }

}