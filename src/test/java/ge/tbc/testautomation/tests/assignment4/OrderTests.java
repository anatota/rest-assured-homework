package ge.tbc.testautomation.tests.assignment4;

import ge.tbc.testautomation.data.model.request.OrderRequest;
import ge.tbc.testautomation.data.model.response.OrderResponse;
import org.testng.annotations.Test;
import ge.tbc.testautomation.steps.OrderSteps;

import java.time.Instant;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class OrderTests {

    private final OrderSteps orderSteps = new OrderSteps();

    @Test
    public void testCreateOrder() {
        OrderRequest order = new OrderRequest()
                .setId(System.currentTimeMillis())
                .setPetId(123L)
                .setQuantity(2)
                .setShipDate(Instant.now().toString())
                .setStatus("placed");

        OrderResponse response = orderSteps.createOrder(order);

        // Validations
        assertThat(response.getId(), notNullValue());
        assertThat(response.getPetId(), equalTo(order.getPetId()));
        assertThat(response.getQuantity(), equalTo(order.getQuantity()));
        assertThat(response.getStatus(), equalTo(order.getStatus()));
        assertThat(response.getShipDate(), not(isEmptyString()));
        assertThat(response.getComplete(), equalTo(false));
    }
}
