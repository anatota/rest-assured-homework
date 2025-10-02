package ge.tbc.testautomation.tests.assignment2;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class DriversListTest {
    private static final String BASE_URI = "https://api.jolpi.ca/ergast/f1/2025/drivers/";
    private Response response;

    @BeforeClass
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
        response = given()
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .response();
    }

    @Test
    public void seriesAndSeasonValidationTest() {
        assertThat(response.jsonPath().getString("MRData.series"), equalTo("f1"));
        assertThat(response.jsonPath().getString("MRData.DriverTable.season"), equalTo("2025"));
        System.out.println("Series 'f1' and Season '2025' validated.");
    }

    @Test
    public void totalDriversCountTest() {
        int driversCount = response.jsonPath().getList("MRData.DriverTable.Drivers").size();
        int totalMRData = response.jsonPath().getInt("MRData.total");
        assertThat(driversCount, equalTo(totalMRData));
    }

    @Test
    public void firstDriverBornBefore1990Test() {
        Map<String, ?> driver = response.jsonPath().get(
                "MRData.DriverTable.Drivers.find { it.dateOfBirth.substring(0, 4).toInteger() < 1990 }"
        );

        assertThat("No driver found born before 1990", driver, is(notNullValue()));

        String fullName = driver.get("givenName") + " " + driver.get("familyName");
        System.out.println("First driver born before 1990: " + fullName);

        assertThat(fullName, is(not(emptyString())));
    }

    @Test
    public void driversBornAfter2000Test() {
        List<String> driverFullNames = response.jsonPath().getList(
                "MRData.DriverTable.Drivers.findAll { it.dateOfBirth.substring(0, 4).toInteger() > 2000 } " +
                        ".collect { it.givenName + ' ' + it.familyName }"
        );
        System.out.println("Drivers born after 2000: " + driverFullNames);
        assertThat("Expected at least 8 drivers born after 2000", driverFullNames.size(), is(greaterThanOrEqualTo(8)));
    }

    @Test
    public void frenchDriversCountTest() {
        List<String> frenchDrivers = response.jsonPath().getList(
                "MRData.DriverTable.Drivers.findAll { it.nationality == 'French' } " +
                        ".collect { it.givenName + ' ' + it.familyName }"
        );
        assertThat(frenchDrivers.size(), equalTo(3));
    }

    public List<String> findDriversByNationality(String nationality) {
        return response.jsonPath().getList(
                "MRData.DriverTable.Drivers.findAll { it.nationality == '" + nationality + "' } " +
                        ".collect { it.givenName + ' ' + it.familyName }"
        );
    }

    @Test
    public void findDriversByNationalityTest() {
        List<String> dutchDrivers = findDriversByNationality("Dutch");
        assertThat(dutchDrivers, hasItem(containsString("Verstappen")));

        List<String> brazilianDrivers = findDriversByNationality("Brazilian");
        assertThat(brazilianDrivers, is(not(empty())));

        List<String> canadianDrivers = findDriversByNationality("Canadian");
        assertThat(canadianDrivers, hasItem("Lance Stroll"));
    }
}