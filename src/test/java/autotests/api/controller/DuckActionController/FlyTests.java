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
@Feature("/api/duck/action/fly")
public class FlyTests extends DuckActionsClient {

    @Test(description = "�������� fly � ������������ id � ��������� ��������")
    @CitrusTest
    public void flyActiveDuckTest(@Optional @CitrusResource TestCaseRunner runner) {
        //  ������� ���� + ������� �� � ����� �����
        runner.variable("duckId", "citrus:randomNumber(4, false)");
        clearDB(runner, "${duckId}");
        databaseUpdate(runner, "insert into duck values (${duckId}, 'green', 1.1, 'rubber', 'quack', 'ACTIVE')");

        //  ������
        flyDuck(runner, "${duckId}");

        //  ��������� �����
        validateResponseByJson(runner, 200, "test_responses/flyTest/flyActiveDuckResponse.json");
    }

    @Test(description = "�������� fly � ������������ id �� ���������� ��������")
    @CitrusTest
    public void flyFixedDuckTest(@Optional @CitrusResource TestCaseRunner runner) {
        //  ������� ���� + ������� �� � ����� �����
        runner.variable("duckId", "citrus:randomNumber(4, false)");
        clearDB(runner, "${duckId}");
        databaseUpdate(runner, "insert into duck values (${duckId}, 'green', 1.1, 'rubber', 'quack', 'FIXED')");

        //  ������
        flyDuck(runner, "${duckId}");

        //  ��������� �����
        validateResponseByJson(runner, 200, "test_responses/flyTest/flyFixedDuckResponse.json");
    }

    @Test(description = "�������� fly � ������������ id � �������� � �������������� ���������")
    @CitrusTest
    public void flyUndefinedDuckTest(@Optional @CitrusResource TestCaseRunner runner) {
        //  ������� ���� + ������� �� � ����� �����
        runner.variable("duckId", "citrus:randomNumber(4, false)");
        clearDB(runner, "${duckId}");
        databaseUpdate(runner, "insert into duck values (${duckId}, 'green', 1.1, 'rubber', 'quack', 'UNDEFINED')");

        //  ������
        flyDuck(runner, "${duckId}");

        //  ��������� �����
        validateResponseByJson(runner, 200, "test_responses/flyTest/flyUndefinedDuckResponse.json");
    }

}
