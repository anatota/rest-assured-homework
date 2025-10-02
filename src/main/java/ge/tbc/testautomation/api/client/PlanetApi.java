package ge.tbc.testautomation.api.client;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class PlanetApi extends BaseApi {
    public Response getPlanets() {
        return given()
                .spec(REQ_SPEC)
                .get("https://swapi.tech/api/planets/?format=json");
    }

    public Response getPlanetDetail(String url) {
        return given()
                .spec(REQ_SPEC)
                .get(url);
    }
}
