package ge.tbc.testautomation.api.client;

import ge.tbc.testautomation.data.model.request.AuthRequest;
import ge.tbc.testautomation.data.model.response.AuthResponse;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class AuthApi {

    private static final String BASE_URI = "https://restful-booker.herokuapp.com";

    public AuthResponse getToken(AuthRequest request) {
        Response res = given()
                .baseUri(BASE_URI)
                .header("Content-Type", "application/json")
                .body(request)
                .when()
                .post("/auth");

        res.then().statusCode(200);
        return res.as(AuthResponse.class);
    }
}
