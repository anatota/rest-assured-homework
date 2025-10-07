package ge.tbc.testautomation.api.client;

import ge.tbc.testautomation.data.model.request.BookingRequest;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class BookingApi {
    private static final String BASE_URI = "https://restful-booker.herokuapp.com";

    public Response createBooking(BookingRequest request) {
        return given()
                .baseUri(BASE_URI)
                .header("Content-Type", "application/json")
                .body(request)
                .when()
                .post("/booking");
    }

    public Response updateBooking(int bookingId, BookingRequest request, String token) {
        return given()
                .baseUri(BASE_URI)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Cookie", "token=" + token)
                .body(request)
                .when()
                .put("/booking/" + bookingId);
    }

    public Response getBooking(int bookingId) {
        return given()
                .baseUri(BASE_URI)
                .header("Accept", "application/json")
                .when()
                .get("/booking/" + bookingId);
    }
}
