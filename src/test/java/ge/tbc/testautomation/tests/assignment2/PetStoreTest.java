package ge.tbc.testautomation.tests.assignment2;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class PetStoreTest {
    private long petId;
    private String petName;
    private String petStatus;
    private String categoryName;
    private String photoUrl;
    private String tagName;

    @BeforeClass
    public void setUp() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";
    }

    @Test(priority = 1)
    public void addPetTest() {
        petId = System.currentTimeMillis();
        petName = "TestPet-" + petId;
        petStatus = "available";
        categoryName = "Dogs";
        photoUrl = "https://example.com/photo-" + petId + ".jpg";
        tagName = "friendly";

        JSONObject category = new JSONObject();
        category.put("id", 1);
        category.put("name", categoryName);

        JSONObject tag = new JSONObject();
        tag.put("id", 101);
        tag.put("name", tagName);

        JSONObject requestBody = new JSONObject();
        requestBody.put("id", petId);
        requestBody.put("category", category);
        requestBody.put("name", petName);
        requestBody.put("photoUrls", new String[]{photoUrl});
        requestBody.put("tags", new JSONObject[]{tag});
        requestBody.put("status", petStatus);

        Response addPetResponse = given()
                .contentType(ContentType.JSON)
                .body(requestBody.toString())
                .when()
                .post("/pet")
                .then()
                .log().all()
                .extract().response();

        assertEquals(addPetResponse.statusCode(), 200);
        assertEquals(addPetResponse.jsonPath().getLong("id"), petId);
        assertEquals(addPetResponse.jsonPath().getString("name"), petName);
        assertEquals(addPetResponse.jsonPath().getString("status"), petStatus);
    }

    @Test(priority = 2)
    public void validatePetTest() {
        Response findByStatusResponse = given()
                .queryParam("status", petStatus)
                .when()
                .get("/pet/findByStatus")
                .then()
                .log().all()
                .extract().response();

        findByStatusResponse.then()
                .statusCode(200)
                .body("id", hasItem(petId));

        JsonPath jsonPath = findByStatusResponse.jsonPath();
        JSONObject foundPet = new JSONObject(jsonPath.getMap("find { it.id == " + petId + " }"));

        assertNotNull(foundPet);

        assertEquals(foundPet.getLong("id"), petId);
        assertEquals(foundPet.getString("name"), petName);
        assertEquals(foundPet.getString("status"), petStatus);
        assertEquals(foundPet.getJSONObject("category").getString("name"), categoryName);
        assertEquals(foundPet.getJSONArray("photoUrls").getString(0), photoUrl);
        assertEquals(foundPet.getJSONArray("tags").getJSONObject(0).getString("name"), tagName);

        System.out.println("All validations passed for pet: " + petName);
    }
}