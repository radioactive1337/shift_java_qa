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
@Feature("/api/duck/action/swim")
public class SwimTests extends DuckActionsClient {
    @Test(description = "�������� swim � ������������ id")
    @CitrusTest
    public void swimExistedDuckTest(@Optional @CitrusResource TestCaseRunner runner) {
        //  ������� ���� + ������� �� � ����� �����
        runner.variable("duckId", "citrus:randomNumber(4, false)");
        clearDB(runner, "${duckId}");
        databaseUpdate(runner, "insert into duck values (${duckId}, 'green', 1.1, 'rubber', 'quack', 'ACTIVE')");

        //  �������
        swimDuck(runner, "${duckId}");

        //  ��������� �����
        validateResponseByJson(runner, 200, "test_responses/swimTest/swimExistedDuckResponse.json");
    }

    @Test(description = "�������� swim � �������������� id")
    @CitrusTest
    public void swimNotExistedDuckTest(@Optional @CitrusResource TestCaseRunner runner) {
        //  �������
        swimDuck(runner, "1234567");

        //  ��������� �����
        validateResponseByJson(runner, 404, "test_responses/swimTest/swimNotExistedDuckResponse.json");
    }

}
