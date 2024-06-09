package autotests.api.controller.DuckController;

import autotests.clients.DuckActionsClient;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;


@Epic("Duck controller")
@Feature("/api/duck/delete")
public class DeleteDuckTests extends DuckActionsClient {

    @Test(description = "�������� �������� ����")
    @CitrusTest
    public void deleteDuckTest(@Optional @CitrusResource TestCaseRunner runner) {
        //  ������� ���� + ������� �� � ����� �����
        runner.variable("duckId", "citrus:randomNumber(3, false)");
        clearDB(runner, "${duckId}");
        databaseUpdate(runner, "insert into duck values (${duckId}, 'red', 1.1, 'wood', 'quack', 'ACTIVE')");

        //  ������ �� �������� ����
        deleteDuck(runner, "${duckId}");

        //  ��������� �����
        validateResponseByJson(runner, 200, "test_responses/deleteDuckTest/deleteDuckResponse.json");

        //  ��������� ��
        databaseQueryAndValidate(runner, "select count(*) as ducks_count from duck", "ducks_count", "0");
    }

}
