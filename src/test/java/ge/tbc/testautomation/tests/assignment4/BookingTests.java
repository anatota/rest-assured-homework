package ge.tbc.testautomation.tests.assignment4;

import ge.tbc.testautomation.data.DataSupplier;
import ge.tbc.testautomation.data.model.request.BookingRequest;
import ge.tbc.testautomation.data.model.response.BookingResponse;
import ge.tbc.testautomation.steps.AuthSteps;
import ge.tbc.testautomation.steps.BookingSteps;
import org.testng.annotations.Test;

public class BookingTests {
    private final BookingSteps bookingSteps = new BookingSteps();
    private final AuthSteps authSteps = new AuthSteps();

    @Test(dataProviderClass = DataSupplier.class, dataProvider = "bookingData")
    public void testUpdateBooking(BookingRequest request) {
        String token = authSteps.generateToken("admin", "password123");

        BookingResponse created = bookingSteps.createBooking(request);
        int bookingId = created.getBookingId();

        request.setFirstName(request.getFirstName() + "_Updated");
        request.setAdditionalNeeds("Updated_" + request.getAdditionalNeeds());

        BookingResponse.Booking updated = bookingSteps.updateBooking(bookingId, request, token);

        bookingSteps.validateUpdatedBooking(updated, request);
    }
}
