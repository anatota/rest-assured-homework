package ge.tbc.testautomation.data;

import ge.tbc.testautomation.data.model.request.BookingRequest;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.DataProvider;

import static io.restassured.RestAssured.given;

public class DataSupplier {
    @DataProvider(name = "bookData")
    public Object[][] bookDataProvider() {
        RestAssured.baseURI = "https://bookstore.toolsqa.com";

        Response allBooksResponse = given()
                .basePath("/BookStore/v1/")
                .when()
                .get("/Books")
                .then()
                .statusCode(200)
                .extract()
                .response();

        int bookCount = allBooksResponse.path("books.size()");

        Object[][] data = new Object[bookCount][3];

        for (int i = 0; i < bookCount; i++) {
            String isbn = allBooksResponse.path("books[" + i + "].isbn");
            String author = allBooksResponse.path("books[" + i + "].author");

            data[i][0] = i;
            data[i][1] = isbn;
            data[i][2] = author;
        }

        return data;
    }

    @DataProvider(name = "bookingData")
    public Object[][] bookingData() {
        BookingRequest.BookingDates dates = new BookingRequest.BookingDates()
                .setCheckIn("2025-10-02")
                .setCheckOut("2025-10-05");

        BookingRequest req1 = new BookingRequest()
                .setFirstName("John")
                .setLastName("Doe")
                .setTotalPrice(200)
                .setDepositPaid(true)
                .setBookingDates(dates)
                .setAdditionalNeeds("Breakfast");

        BookingRequest req2 = new BookingRequest()
                .setFirstName("Jane")
                .setLastName("Smith")
                .setTotalPrice(500)
                .setDepositPaid(false)
                .setBookingDates(dates)
                .setAdditionalNeeds("Dinner");

        return new Object[][]{{req1}, {req2}};
    }
}
