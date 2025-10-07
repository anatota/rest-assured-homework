package ge.tbc.testautomation.soapservice;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class SoapServiceSender {

    public static Response send(String url, String action, String body) {
        return given()
                .header("Content-Type", "text/xml; charset=utf-8")
                .header("SoapAction", action)
                .body(body)
                .post(url);
    }
}