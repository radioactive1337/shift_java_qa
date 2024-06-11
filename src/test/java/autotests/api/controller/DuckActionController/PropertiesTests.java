package autotests.api.controller.DuckActionController;

import autotests.clients.DuckActionsClient;
import autotests.payloads.Duck;
import autotests.payloads.WingsState;
import com.consol.citrus.TestCaseRunner;
import com.consol.citrus.annotations.CitrusResource;
import com.consol.citrus.annotations.CitrusTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

@Epic("Duck action controller")
@Feature("/api/duck/action/properties")
public class PropertiesTests extends DuckActionsClient {

    @Test(description = "�������� ��������� ������� � ������ id � ���������� wood")
    @CitrusTest
    public void getEvenWoodenDuckPropsTest(@Optional @CitrusResource TestCaseRunner runner) {
        //  ������� ���� � ������ id + ������� �� � ����� �����
        runner.variable("duckId", "citrus:randomNumber(4, false)1");
        clearDB(runner, "${duckId}");
        databaseUpdate(runner, "insert into duck values (${duckId}, 'green', 1.1, 'wood', 'quack', 'ACTIVE')");

        //  �������� �� ��������
        getDuckProps(runner, "${duckId}");

        //  ��������� �����
        validateResponseByClass(runner, 200, new Duck().color("green").height(1.1).material("wood").sound("quack").wingsState(WingsState.ACTIVE));
    }

    @Test(description = "�������� ��������� ������� � �������� id � ���������� rubber")
    @CitrusTest
    public void getOddRubberDuckPropsTest(@Optional @CitrusResource TestCaseRunner runner) {
        //  ������� ���� � �������� id + ������� �� � ����� �����
        runner.variable("duckId", "citrus:randomNumber(4, false)2");
        clearDB(runner, "${duckId}");
        databaseUpdate(runner, "insert into duck values (${duckId}, 'green', 1.1, 'rubber', 'quack', 'ACTIVE')");

        //  �������� �� ��������
        getDuckProps(runner, "${duckId}");

        //  ��������� �����
        validateResponseByClass(runner, 200, new Duck().color("green").height(1.1).material("rubber").sound("quack").wingsState(WingsState.ACTIVE));
    }

}
