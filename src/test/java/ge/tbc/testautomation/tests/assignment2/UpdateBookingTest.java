package ge.tbc.testautomation.tests.assignment2;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class UpdateBookingTest {
    private static final String BASE_URL = "https://restful-booker.herokuapp.com";
    private String token;
    private int bookingId;

    @BeforeClass
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
        JSONObject authorization = new JSONObject();
        authorization.put("username", "admin");
        authorization.put("password", "password123");

        Response authResponse = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(authorization.toString())
                .post("/auth");

        Assert.assertEquals(authResponse.getStatusCode(), 200, "Failed to get auth token.");
        token = authResponse.jsonPath().getString("token");
        System.out.println("Auth Token: " + token);

        JSONObject booking = new JSONObject();
        booking.put("firstname", "John");
        booking.put("lastname", "Johns");
        booking.put("totalprice", 111);
        booking.put("depositpaid", true);

        JSONObject dates = new JSONObject();
        dates.put("checkin", "2024-01-01");
        dates.put("checkout", "2024-01-05");
        booking.put("bookingdates", dates);
        booking.put("additionalneeds", "Breakfast");

        Response createBookingResponse = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(booking.toString())
                .post("/booking");

        Assert.assertEquals(createBookingResponse.getStatusCode(), 200, "Failed to create booking.");
        bookingId = createBookingResponse.jsonPath().getInt("bookingid");
        System.out.println("Created Booking ID: " + bookingId);
    }

    @Test
    public void updateBookingTest() {
        JSONObject requestBody = new JSONObject();
        requestBody.put("firstname", "Jane");
        requestBody.put("lastname", "Jackson");
        requestBody.put("totalprice", 222);
        requestBody.put("depositpaid", false);

        JSONObject bookingDates = new JSONObject();
        bookingDates.put("checkin", "2024-02-01");
        bookingDates.put("checkout", "2024-02-05");
        requestBody.put("bookingdates", bookingDates);
        requestBody.put("additionalneeds", "Lunch");

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .header("Cookie", "token=" + token)
                .pathParam("id", bookingId)
                .body(requestBody.toString())
                .put("/booking/{id}")
                .then()
                .extract().response();

        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 200);
        Assert.assertEquals(response.jsonPath().getString("firstname"), "Jane");
        Assert.assertEquals(response.jsonPath().getString("lastname"), "Jackson");
        Assert.assertEquals(response.jsonPath().getInt("totalprice"), 222);
        Assert.assertFalse(response.jsonPath().getBoolean("depositpaid"));
        Assert.assertEquals(response.jsonPath().getString("additionalneeds"), "Lunch");
    }
}