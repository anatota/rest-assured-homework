package ge.tbc.testautomation.steps;

import ge.tbc.testautomation.api.client.OrderApi;
import ge.tbc.testautomation.data.model.request.OrderRequest;
import ge.tbc.testautomation.data.model.response.OrderResponse;
import io.restassured.response.Response;
import org.hamcrest.Matchers;

import static org.hamcrest.MatcherAssert.assertThat;

public class OrderSteps {

    private final OrderApi orderApi = new OrderApi();

    public OrderResponse createOrder(OrderRequest order) {
        Response resp = orderApi.createOrder(order)
                .then().statusCode(200)
                .extract().response();

        OrderResponse created = resp.as(OrderResponse.class);

        // Basic validations
        assertThat(created.getId(), Matchers.notNullValue());
        assertThat(created.getStatus(), Matchers.equalTo(order.getStatus()));

        return created;
    }

    public OrderResponse getOrder(long orderId) {
        Response resp = orderApi.getOrder(orderId)
                .then().statusCode(200)
                .extract().response();

        return resp.as(OrderResponse.class);
    }
}
