package autotests.api.controller.DuckActionController;

import autotests.clients.DuckActionsClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

@Epic("Duck action controller")
@Feature("/api/duck/action/quack")
public class QuackTests extends DuckActionsClient {

    @Test(description = "�������� �������� � ������ id")
    @CitrusTest
    public void quackEvenDuckTest(@Optional @CitrusResource TestCaseRunner runner) {
        //  ������� ���� � ������ id + ������� �� � ����� �����
        runner.variable("duckId", "citrus:randomNumber(4, false)2");
        clearDB(runner, "${duckId}");
        databaseUpdate(runner, "insert into duck values (${duckId}, 'green', 1.1, 'rubber', 'quack', 'ACTIVE')");

        //  �������
        quackDuck(runner, "${duckId}", "1", "1");

        //  �������� ������
        validateResponseByString(runner, 200, "{\n" +
                "  \"sound\": \"quack\"\n" +
                "}");
    }

    @Test(description = "�������� �������� � �������� id")
    @CitrusTest
    public void quackOddDuckTest(@Optional @CitrusResource TestCaseRunner runner) {
        //  ������� ���� � �������� id + ������� �� � ����� �����
        runner.variable("duckId", "citrus:randomNumber(4, false)1");
        clearDB(runner, "${duckId}");
        databaseUpdate(runner, "insert into duck values (${duckId}, 'green', 1.1, 'rubber', 'quack', 'ACTIVE')");

        //  �������
        quackDuck(runner, "${duckId}", "1", "1");

        //  �������� ������
        validateResponseByString(runner, 200, "{\n" +
                "  \"sound\": \"quack\"\n" +
                "}");
    }

}
