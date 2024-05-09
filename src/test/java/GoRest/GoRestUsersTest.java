package GoRest;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class GoRestUsersTest {
    RequestSpecification reqSpec;
    Faker randomUreticisi = new Faker();
    String randomName;
    String randomEmail;

    int userID;

    @BeforeClass
    public void Setup() {

        baseURI = "https://gorest.co.in/public/v2/users";
        reqSpec = new RequestSpecBuilder()
                .addHeader("Authorization", "Bearer a288902d5f81f1788ed9692462ff4ecffb78827a7e315e1470fcf8b799b6d46b")
                .setContentType(ContentType.JSON)
                .build();

    }

    @Test
    public void CreateUser() {
        randomName = randomUreticisi.name().firstName();
        randomEmail = randomUreticisi.internet().emailAddress();

        Map<String, String> newUser = new HashMap<>();
        newUser.put("name", randomName);
        newUser.put("gender", "Male");
        newUser.put("email", randomEmail);
        newUser.put("status", "active");

        userID =
                given()
                        .spec(reqSpec) // yukarıda tanımladığımız her şey otomatik buraya da geldi.
                        .body(newUser)
                        .when()
                        .post("https://gorest.co.in/public/v2/users")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id")

        ;

    }

    @Test(dependsOnMethods = "CreateUser")
    public void getUserById() {


        given()
                .spec(reqSpec) // yukarıda tanımladığımız her şey otomatik buraya da geldi.
                .when()
                .get("/" + userID)

                .then()
                .log().body()
                .statusCode(200)
                .body("id", equalTo(userID))

        ;

    }

    @Test(dependsOnMethods = "getUserById")
    public void UpdateUser() {

        String updatedName="Kaan Hakan";
        Map<String, String> updateUser = new HashMap<>();
        updateUser.put("name", updatedName);

        given()
                .spec(reqSpec)
                .body(updateUser)
                .when()
                .put("/" + userID)

                .then()
                .statusCode(200)
                .log().body()
                .body("id",equalTo(userID))
                .body("name",equalTo(updatedName))


        ;

    }

    @Test (dependsOnMethods = "UpdateUser")
    public void DeleteUser(){


        given()
                .spec(reqSpec)

                .when()
                .delete("/"+userID)

                .then()
                .statusCode(204)


    ;

    }

    @Test (dependsOnMethods = "DeleteUser")
    public void DeleteUserNegative(){


        given()
                .spec(reqSpec)

                .when()
                .delete("/"+userID)

                .then()
                .statusCode(404)


        ;

    }

}
