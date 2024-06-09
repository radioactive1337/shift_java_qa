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
@Feature("/api/duck/action/properties")
public class PropertiesTests extends DuckActionsClient {

    @Test(description = "�������� ��������� ������� � ������ id � ���������� wood")
    @CitrusTest
    public void getEvenWoodenDuckPropsTest(@Optional @CitrusResource TestCaseRunner runner) {
        //  ������� ���� � ������ id + ������� �� � ����� �����
        runner.variable("duckId", "citrus:randomNumber(3, false)1");
        clearDB(runner, "${duckId}");
        databaseUpdate(runner, "insert into duck values (${duckId}, 'green', 1.1, 'wood', 'quack', 'ACTIVE')");

        //  �������� �� ��������
        getDuckProps(runner, "${duckId}");

        //  ��������� �����
        validateResponseByJson(runner, 200, "test_responses/propertiesTest/getEvenWoodenDuckPropsResponse.json");
    }

    @Test(description = "�������� ��������� ������� � �������� id � ���������� rubber")
    @CitrusTest
    public void getOddRubberDuckPropsTest(@Optional @CitrusResource TestCaseRunner runner) {
        //  ������� ���� � �������� id + ������� �� � ����� �����
        runner.variable("duckId", "citrus:randomNumber(3, false)2");
        clearDB(runner, "${duckId}");
        databaseUpdate(runner, "insert into duck values (${duckId}, 'green', 1.1, 'rubber', 'quack', 'ACTIVE')");

        //  �������� �� ��������
        getDuckProps(runner, "${duckId}");

        //  ��������� �����
        String expectedString = "{\n" +
                "  \"color\": \"green\",\n" +
                "  \"height\": 1.1,\n" +
                "  \"material\": \"rubber\",\n" +
                "  \"sound\": \"quack\",\n" +
                "  \"wingsState\": \"ACTIVE\"\n" +
                "}";
        validateResponseByString(runner, 200, expectedString);
    }

}
