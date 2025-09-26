package ge.tbc.testautomation.tests.assignment2;

import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class BookStoreTest {
    @Test
    public void validateBookPagesAndAuthors() {
        given()
                .baseUri("https://bookstore.toolsqa.com")
                .basePath("/BookStore/v1")
                .contentType(ContentType.JSON)
                .when()
                .get("/Books")
                .then()
                .log().ifStatusCodeIsEqualTo(200)
                .body("books.pages", everyItem(lessThan(1000)))
                .body("books[0].author", equalTo("Richard E. Silverman"))
                .body("books[1].author", equalTo("Addy Osmani"));
    }
}
