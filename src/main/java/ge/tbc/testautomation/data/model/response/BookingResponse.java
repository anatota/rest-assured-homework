package ge.tbc.testautomation.data.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BookingResponse {
    @JsonProperty("bookingid")
    private Integer bookingId;

    @JsonProperty("booking")
    private Booking booking;

    @Data
    public static class Booking {
        @JsonProperty("firstname")
        private String firstName;

        @JsonProperty("lastname")
        private String lastName;

        @JsonProperty("totalprice")
        private Integer totalPrice;

        @JsonProperty("depositpaid")
        private Boolean depositPaid;

        @JsonProperty("bookingdates")
        private BookingDates bookingDates;

        @JsonProperty("additionalneeds")
        private String additionalNeeds;
    }

    @Data
    public static class BookingDates {
        @JsonProperty("checkin")
        private String checkIn;

        @JsonProperty("checkout")
        private String checkOut;
    }
}
