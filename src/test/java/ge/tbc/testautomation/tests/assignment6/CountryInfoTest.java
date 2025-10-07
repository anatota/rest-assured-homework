package ge.tbc.testautomation.tests.assignment6;

import io.qameta.allure.*;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.List;
import java.util.regex.Pattern;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Epic("Country Info Service")
@Feature("Country data endpoints")
public class CountryInfoTest {

    String url = "http://webservices.oorsprong.org/websamples.countryinfo/CountryInfoService.wso/ListOfContinentsByName";

    @Story("List continents")
    @Description("Validates list of continents and codes")
    @Severity(SeverityLevel.NORMAL)
    @Test
    public void validateListOfContinentsByName() {
        Response response = given()
                .when()
                .get(url)
                .then().log().all()
                .statusCode(200)
                .extract().response();

        XmlPath xml = new XmlPath(response.asString());

        List<String> sCodes = xml.getList("ArrayOftContinent.tContinent.sCode");
        List<String> sNames = xml.getList("ArrayOftContinent.tContinent.sName");

        assertThat("Unexpected count of continents", sNames.size(), equalTo(6));

        assertThat(sNames, containsInAnyOrder(
                "Africa",
                "Antarctica",
                "Asia",
                "Europe",
                "Ocenania",      // typo in service
                "The Americas"
        ));

        String lastName = sNames.get(sNames.size() - 1);
        assertThat(lastName, equalTo("The Americas"));

        assertThat(sNames, hasItems(
                "Africa", "Antarctica", "Asia", "Europe", "Ocenania", "The Americas"
        ));

        assertThat(sNames, everyItem(not(matchesPattern(".*\\d.*"))));

        long distinctCount = sNames.stream().distinct().count();
        assertThat("Duplicate continent names found", distinctCount, equalTo((long) sNames.size()));

        String nameWithO = xml.getString("ArrayOftContinent.tContinent.find { it.sCode == 'OC' }.sName");
        assertThat(nameWithO, equalTo("Ocenania"));

        List<String> namesAtoCa = sNames.stream()
                .filter(n -> n.startsWith("A") && n.endsWith("ca"))
                .toList();
        assertThat(namesAtoCa, hasItems("Africa", "Antarctica"));

        Pattern pattern = Pattern.compile("^[A-Z]{2}$");
        assertThat(sCodes, everyItem(matchesPattern(pattern)));
    }
}
