package ge.tbc.testautomation.api.client;

import ge.tbc.testautomation.data.model.request.OrderRequest;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderApi extends BaseApi {
    public Response createOrder(OrderRequest order) {
        return given()
                .spec(REQ_SPEC)
                .baseUri("https://petstore3.swagger.io")
                .basePath("/api/v3")
                .body(order)
                .when()
                .post("/store/order");
    }

    public Response getOrder(long orderId) {
        return given()
                .spec(REQ_SPEC)
                .baseUri("https://petstore3.swagger.io")
                .basePath("/api/v3")
                .pathParam("orderId", orderId)
                .get("/store/order/{orderId}");
    }

    public Response deleteOrder(long orderId) {
        return given()
                .spec(REQ_SPEC)
                .baseUri("https://petstore3.swagger.io")
                .basePath("/api/v3")
                .pathParam("orderId", orderId)
                .delete("/store/order/{orderId}");
    }
}
