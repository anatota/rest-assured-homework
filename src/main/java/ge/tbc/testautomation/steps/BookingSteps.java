package ge.tbc.testautomation.steps;

import ge.tbc.testautomation.api.client.BookingApi;
import ge.tbc.testautomation.data.model.request.BookingRequest;
import ge.tbc.testautomation.data.model.response.BookingResponse;
import io.restassured.response.Response;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class BookingSteps {

    private final BookingApi bookingApi = new BookingApi();

    public BookingResponse createBooking(BookingRequest request) {
        Response res = bookingApi.createBooking(request);
        res.then().statusCode(200);
        return res.as(BookingResponse.class);
    }

    public BookingResponse.Booking updateBooking(int bookingId, BookingRequest request, String token) {
        Response res = bookingApi.updateBooking(bookingId, request, token);
        res.then().statusCode(200);
        return res.as(BookingResponse.Booking.class);
    }

    public BookingResponse.Booking getBooking(int bookingId) {
        Response res = bookingApi.getBooking(bookingId);
        res.then().statusCode(200);
        return res.as(BookingResponse.Booking.class);
    }

    public void validateUpdatedBooking(BookingResponse.Booking actual, BookingRequest expected) {
        assertThat(actual.getFirstName(), equalTo(expected.getFirstName()));
        assertThat(actual.getLastName(), equalTo(expected.getLastName()));
        assertThat(actual.getTotalPrice(), equalTo(expected.getTotalPrice()));
        assertThat(actual.getDepositPaid(), equalTo(expected.getDepositPaid()));
        assertThat(actual.getAdditionalNeeds(), equalTo(expected.getAdditionalNeeds()));
    }
}
