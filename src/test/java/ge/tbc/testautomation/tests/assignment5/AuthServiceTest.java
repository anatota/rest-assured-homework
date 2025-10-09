package ge.tbc.testautomation.tests.assignment5;

import ge.tbc.auth.model.*;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.ErrorLoggingFilter;
import io.restassured.filter.log.LogDetail;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import ge.tbc.auth.invoker.ApiClient;
import ge.tbc.auth.invoker.JacksonObjectMapper;

import java.util.HashMap;
import java.util.List;

import static ge.tbc.auth.invoker.ResponseSpecBuilders.shouldBeCode;
import static ge.tbc.auth.invoker.ResponseSpecBuilders.validatedWith;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static io.restassured.config.RestAssuredConfig.config;
import static org.testng.AssertJUnit.*;

public class AuthServiceTest {
    private ApiClient api;

    private final String BASE_URL = "http://localhost:8086";

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
    public void registerAdminTest() {
        RegisterRequest register = new RegisterRequest()
                .firstname("Ana")
                .lastname("Totadze")
                .email("ana.admin@blablabla.com")
                .password("Password!4321")
                .role(RegisterRequest.RoleEnum.ADMIN);

        AuthenticationResponse authResponse = api.authentication()
                .register()
                .body(register)
                .executeAs(response -> {
            response.then().log().all();
            validatedWith(shouldBeCode(200));
            return response;
        });

        String accessToken = authResponse.getAccessToken();
        System.out.println("First access token:::: "+accessToken);

        // This didn't work

//        HashMap<String, String> headers = new HashMap<>();
//        headers.put("Accept","");
//        String authAndToken = "Bearer " + accessToken;
//        headers.put("Authorization", authAndToken);
//        String message = api.authorization()
//                .sayHelloWithRoleAdminAndReadAuthority()
//                .reqSpec(req -> req.addHeaders(headers))
//                .executeAs(response -> {
//                    response.then().statusCode(200);
//                    return response;
//                });

        // This also didn't work
//        String message = api.authorization()
//                .sayHelloWithRoleAdminAndReadAuthority()
//                .reqSpec(req -> req.addHeader("Authorization", "Bearer " + accessToken))
//                .executeAs(response -> {
//                    response.then().statusCode(200);
//                    return response;
//                });
//
//        assertEquals("Hello, you have access to a protected resource that requires admin role and read authority", message);

        String refreshToken = authResponse.getRefreshToken();
        List<String> roles = authResponse.getRoles();

        Assert.assertNotNull(roles);
        assertTrue(roles.contains("READ_PRIVILEGE"));
        assertTrue(roles.contains("WRITE_PRIVILEGE"));
        assertTrue(roles.contains("DELETE_PRIVILEGE"));
        assertTrue(roles.contains("UPDATE_PRIVILEGE"));
        assertTrue(roles.contains("ROLE_ADMIN"));
        assertFalse(roles.contains("blabla"));

        RefreshTokenRequest refreshRequest = new RefreshTokenRequest()
                .refreshToken(refreshToken);

        RefreshTokenResponse refreshedResponse = api.authentication()
                .refreshToken()
                .body(refreshRequest)
                .executeAs(response -> {
                    response.then().statusCode(200);
                    return response;
                });

        System.out.println("REFRESHED TOKEN:::::::" + refreshedResponse.getAccessToken());
        assertNotNull(refreshedResponse.getAccessToken());
    }
}
