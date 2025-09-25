package ge.tbc.testautomation.tests;

import ge.tbc.testautomation.data.DataSupplier;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class RestAssuredExerciseTest {
//    @Parameters("baseURL")
    @BeforeMethod
    public void setup(/*String baseURL*/) {
//        RestAssured.baseURI = "https://bookstore.toolsqa.com";
        RestAssured.baseURI = "https://petstore.swagger.io";
//        RestAssured.baseURI = baseURL;
    }

    // 1. Send a GET request to https://bookstore.toolsqa.com/BookStore/v1/Books to retrieve the list of books.
    @Test
    public void getBooks() {
        Response response = given()
                .log().all()
                .when()
                .basePath("/BookStore/v1/")
                .when()
                .get("/Books");
        response
                .then()
                .log().all();
    }

    // 2. Extract the ISBN and author information of the first and second books.
    @Test
    public void validateFirstTwoBooks() {
        Response allBooksResponse = given()
                .basePath("/BookStore/v1/")
                .when()
                .get("/Books")
                .then()
                .statusCode(200)
                .extract()
                .response();

        String firstIsbn = allBooksResponse.path("books[0].isbn");
        String firstAuthor = allBooksResponse.path("books[0].author");

        String secondIsbn = allBooksResponse.path("books[1].isbn");
        String secondAuthor = allBooksResponse.path("books[1].author");

        validateBook(firstIsbn, firstAuthor);
        validateBook(secondIsbn, secondAuthor);
    }

//    3. For each book:
//
//    Send a GET request to https://bookstore.toolsqa.com/BookStore/v1/Book?ISBN={isbn}, where {isbn} is the extracted ISBN.
//    Ensure that the response status code is 200 (OK).
//    Parse the response and extract the author information.
//    Compare the retrieved author information with the expected author.
//    Validate that the response contains all necessary information about the book, such as title, ISBN, publish_date, and pages.
    private void validateBook(String isbn, String expectedAuthor) {
        Response singleBookResponse = given()
                .basePath("/BookStore/v1/")
                .queryParam("ISBN", isbn)
                .when()
                .get("/Book")
                .then()
                .statusCode(200)
                .extract()
                .response();

        String actualAuthor = singleBookResponse.path("author");
        Assert.assertEquals(actualAuthor, expectedAuthor);

        Assert.assertNotNull(singleBookResponse.path("title"));
        Assert.assertNotNull(singleBookResponse.path("isbn"));
        Assert.assertNotNull(singleBookResponse.path("publish_date"));
        Assert.assertNotNull(singleBookResponse.path("pages"));

        System.out.println("Validated isbn: " + isbn + " author: " + expectedAuthor);
    }

    // 4. Implement a data provider to iterate over different index and ISBN combinations.
    @Test(dataProviderClass = DataSupplier.class, dataProvider = "bookData")
    public void validateBookWithDataProvider(int index, String isbn, String expectedAuthor) {
        Response singleBookResponse = given()
                .basePath("/BookStore/v1/")
                .queryParam("ISBN", isbn)
                .when()
                .get("/Book")
                .then()
                .statusCode(200)
                .extract()
                .response();

        String actualAuthor = singleBookResponse.path("author");
        Assert.assertEquals(actualAuthor, expectedAuthor);

        Assert.assertNotNull(singleBookResponse.path("title"));
        Assert.assertNotNull(singleBookResponse.path("isbn"));
        Assert.assertNotNull(singleBookResponse.path("publish_date"));
        Assert.assertNotNull(singleBookResponse.path("pages"));

        System.out.println("Validated book index " + index + " with ISBN: " + isbn);
    }


    // 5. Send a DELETE request to BookStore/v1/Book to remove the book.
    //
    // Validate that the response is an Unauthorized 401 status code.
    // Ensure that the message is "User not authorized!".
    @Test
    public void deleteBookUnauthorized() {
        String isbnToDelete = "9781449325862";
        given()
                .baseUri("https://bookstore.toolsqa.com")
                .basePath("/BookStore/v1")
                .queryParam("ISBN", isbnToDelete)
                .when()
                .delete("/Book")
                .then()
                .log().body()
                .statusCode(401)
                .body("message", equalTo("User not authorized!"));
    }

    // 6. Send a POST request to https://petstore.swagger.io/v2/store/order with a JSON body.
    //
    //Validate that the response contains all necessary information.
    @Test(description = "Needs to change base URI from here")
    public void validateOrderResponseTest() {
        String jsonBody = """
        {
          "id": 12345,
          "petId": 98765,
          "quantity": 1,
          "shipDate": "2025-09-25T13:17:03.747Z",
          "status": "placed",
          "complete": true
        }
        """;

        Response response = given()
                .basePath("/v2/store")
                .contentType("application/json")
                .accept("application/json")
                .body(jsonBody)
                .when()
                .post("/order")
                .then()
                .body("petId", equalTo(98765))
                .log().body()
                .statusCode(200)
                .extract()
                .response();

        Assert.assertNotNull(response.path("id"));
        Assert.assertNotNull(response.path("petId"));
        Assert.assertNotNull(response.path("quantity"));
        Assert.assertNotNull(response.path("shipDate"));
        Assert.assertNotNull(response.path("status"));
        Assert.assertNotNull(response.path("complete"));
    }

    // 7. Send a (form) POST request to https://petstore.swagger.io/v2/pet/{petId} with petId, name & status parameters.
    //
    // Validate that the response JSON has code, type, and message.
    @Test
    public void updatePetFormTest() {
        int petId = 12345;
        String newName = "RedPanda";
        String newStatus = "available";

        Response response = given()
                .basePath("/v2")
                .formParam("name", newName)
                .formParam("status", newStatus)
                .when()
                .post("/pet/{petId}", petId)
                .then()
                .log().body()
                .statusCode(200)
                .extract()
                .response();

        Assert.assertNotNull(response.path("code"));
        Assert.assertNotNull(response.path("type"));
        Assert.assertNotNull(response.path("message"));

        System.out.println("Pet updated successfully with ID: " + petId);
    }

    // 8. Send another request and try to cause a 404 error.
    //
    // Validate that the response has "code": 404. <<<< this had code 1
    @Test
    public void notFoundErrorTest() {
        int nonExistentPetId = 574285948;

        Response response = given()
                .basePath("/v2")
                .accept("application/json")
                .when()
                .get("/pet/{petId}", nonExistentPetId)
                .then()
                .body("message", equalTo("Pet not found"))
                .log().body()
                .statusCode(404) // http 404 error validation
                .extract()
                .response();

        int code = response.path("code");
        Assert.assertEquals(code, 1); // code in json body returned 1 instead of 404

        System.out.println("404 error returned for petId: " + nonExistentPetId);
    }

    // 9. Send a GET request to petstore /user/login resource with query parameters username & password.
    //
    // Ensure that the response status code is 200 (OK).
    // Validate that the message contains 10 significant numbers.
    // Extract that number and print it.
    @Test
    public void userLoginTest() {
        // Created username in swagger
        String username = "string";
        String password = "string";

        Response response = given()
                .basePath("/v2")
                .accept("application/json")
                .queryParam("username", username)
                .queryParam("password", password)
                .when()
                .get("/user/login")
                .then()
                .log().body()
                .statusCode(200)
                .extract()
                .response();

        String message = response.path("message");
        System.out.println("Login message: " + message);

        Pattern pattern = Pattern.compile("\\d{10,}"); // It was more than 10 added , in regex
        Matcher matcher = pattern.matcher(message);

        if (matcher.find()) {
            String number = matcher.group();
            System.out.println("Extracted number: " + number);
        } else {
            Assert.fail("No 10-digit number found in the message");
        }
    }
}