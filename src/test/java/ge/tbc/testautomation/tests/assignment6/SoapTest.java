package ge.tbc.testautomation.tests.assignment6;

import ge.tbc.testautomation.soapservice.Marshall;
import ge.tbc.testautomation.soapservice.SoapServiceSender;
import ge.tbc.testautomation.soapservice.Unmarshall;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.oorsprong.websamples.ListOfCountryNamesGroupedByContinent;
import org.oorsprong.websamples.ListOfCountryNamesGroupedByContinentResponse;
import org.oorsprong.websamples.ObjectFactory;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

public class SoapTest {
    ObjectFactory objectFactory = new ObjectFactory();
    String url = "http://webservices.oorsprong.org/websamples.countryinfo/CountryInfoService.wso";
    String action = "http://www.oorsprong.org/websamples.countryinfo/CountryInfoServiceSoapType/ListOfCountryNamesGroupedByContinentRequest";

    @Test
    public void listOfContinentCodeTest() {
        ListOfCountryNamesGroupedByContinent listOfCountry = objectFactory.createListOfCountryNamesGroupedByContinent();

        String requestBody = Marshall.marshallSoapRequest(listOfCountry);
        Response response = SoapServiceSender.send(url, action, requestBody)
                .then().log().all()
                .assertThat()
                .statusCode(200)
                .extract().response();

        ListOfCountryNamesGroupedByContinentResponse listOfCountryResponse =
                Unmarshall.unmarshallResponse(response.body().asString(), ListOfCountryNamesGroupedByContinentResponse.class);


        List<String> listOfContinentCode = listOfCountryResponse.getListOfCountryNamesGroupedByContinentResult()
                .getTCountryCodeAndNameGroupedByContinent()
                .stream()
                .map(c -> c.getContinent().getSCode())
                .toList();

        assertThat(listOfContinentCode, Matchers.contains("AF", "AM", "AN", "AS", "EU", "OC"));
    }


}