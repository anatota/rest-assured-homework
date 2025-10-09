package ge.tbc.testautomation.tests.assignment5;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.ErrorLoggingFilter;
import io.restassured.filter.log.LogDetail;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import pet.store.v3.invoker.ApiClient;
import pet.store.v3.invoker.JacksonObjectMapper;
import pet.store.v3.model.Order;

import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static io.restassured.config.RestAssuredConfig.config;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static pet.store.v3.invoker.ResponseSpecBuilders.shouldBeCode;
import static pet.store.v3.invoker.ResponseSpecBuilders.validatedWith;

public class PetStoreTest {
    private ApiClient api;

    private final String BASE_URL = "https://petstore3.swagger.io/api/v3";

    @BeforeSuite
    public void createApi() {
        api = ApiClient.api(ApiClient.Config.apiConfig()
                .reqSpecSupplier(() -> new RequestSpecBuilder()
                        .log(LogDetail.ALL)
                        .setConfig(config()
                                .objectMapperConfig(objectMapperConfig()
                                        .defaultObjectMapper(JacksonObjectMapper.jackson())))
                        .addFilter(new ErrorLoggingFilter()) // log only in case of error
//                        .addFilter(new RequestLoggingFilter()) // log all requests
//                        .addFilter(new ResponseLoggingFilter()) // log all responses
                        .setBaseUri(BASE_URL)));
    }

    @Test
    public void createOrderTest() {
        Order order = new Order();
        order.setId(123L);
        order.setPetId(1L);
        order.setQuantity(7);
        order.setStatus(Order.StatusEnum.APPROVED);
        order.setComplete(true);

        Order createdOrder = api.store()
                .placeOrder()
                .body(order)
                .executeAs(
                        validatedWith(
                                shouldBeCode(SC_OK)
                        ).andThen(response -> response)
                );

        assertThat(createdOrder.getId(), equalTo(123L));
        assertThat(createdOrder.getPetId(), equalTo(1L));
        assertThat(createdOrder.getQuantity(), equalTo(7));
        assertThat(createdOrder.getStatus(), equalTo(Order.StatusEnum.APPROVED));
        assertThat(createdOrder.getComplete(), equalTo(true));
    }
}
