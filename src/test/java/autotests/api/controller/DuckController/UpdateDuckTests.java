package autotests.api.controller.DuckController;

import autotests.payloads.WingsState;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;
import autotests.clients.DuckActionsClient;
import autotests.payloads.Message;


@Epic("Duck controller")
@Feature("/api/duck/update")
public class UpdateDuckTests extends DuckActionsClient {

    @Test(description = "�������� ���������� ����� � ������ ����")
    @CitrusTest
    public void updateColorAndHeightDuckTest(@Optional @CitrusResource TestCaseRunner runner) {
        //  ������� ���� + ������� �� � ����� �����
        runner.variable("duckId", "citrus:randomNumber(4, false)");
        clearDB(runner, "${duckId}");
        databaseUpdate(runner, "insert into duck values (${duckId}, 'green', 1.1, 'rubber', 'quack', 'ACTIVE')");

        //  ��������� 2 ����
        updateDuck(runner, "rainbow", 9.9, "${duckId}", "rubber", "quack", WingsState.ACTIVE);

        // ��������� �����
        String expectedMessage = "Duck with id = ${duckId} is updated";
        validateResponseByString(runner, 200, "{\n" +
                "  \"message\": \"" + expectedMessage + "\"\n" +
                "}");

        //  ��������� � ��
        databaseValidateDuck(runner, "${duckId}", "rainbow", 9.9, "rubber", "quack", WingsState.ACTIVE);
    }

    @Test(description = "�������� ���������� ����� � ����� ����")
    @CitrusTest
    public void updateColorAndSoundDuckTest(@Optional @CitrusResource TestCaseRunner runner) {
        //  ������� ���� + ������� �� � ����� �����
        runner.variable("duckId", "citrus:randomNumber(4, false)");
        clearDB(runner, "${duckId}");
        databaseUpdate(runner, "insert into duck values (${duckId}, 'green', 1.1, 'rubber', 'quack', 'ACTIVE')");

        //  ��������� 2 ����
        updateDuck(runner, "rainbow", 1.1, "${duckId}", "rubber", "mew", WingsState.ACTIVE);

        //  ��������� �����
        String expectedMessage = "Duck with id = ${duckId} is updated";
        validateResponseByClass(runner, 200, new Message().message(expectedMessage));

        //  ��������� � ��
        databaseValidateDuck(runner, "${duckId}", "rainbow", 1.1, "rubber", "mew", WingsState.ACTIVE);
    }

}
