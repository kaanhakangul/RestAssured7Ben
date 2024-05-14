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
public class _08_GoRestCommentTest {

    RequestSpecification reqSpec;
    Faker randomUreticisi = new Faker();
    int commentID=0;

    @BeforeClass
    public void Setup() {

        baseURI = "https://gorest.co.in/public/v2/comments";
        reqSpec = new RequestSpecBuilder()
                .addHeader("Authorization", "Bearer a288902d5f81f1788ed9692462ff4ecffb78827a7e315e1470fcf8b799b6d46b")
                .setContentType(ContentType.JSON)
                .build();

    }


    @Test
    public void CreateComment(){

        String fullName=randomUreticisi.name().fullName();
        String email=randomUreticisi.internet().emailAddress();
        String body=randomUreticisi.lorem().paragraph();
        String postId="122490";

        Map<String,String> newComment=new HashMap<>();
        newComment.put("name",fullName);
        newComment.put("email",email);
        newComment.put("body",body);
        newComment.put("post_id",postId);

        commentID=
        given()
                .spec(reqSpec)
                .body(newComment)

                .when()
                .post("")

                .then()
                .log().body()
                .statusCode(201)
                .extract().path("id")

                ;

        System.out.println("commentID = "+ commentID);
    }

    @Test(dependsOnMethods = "CreateComment")
    public void GetCommentByID(){

        given()
                .spec(reqSpec)
                .when()
                .get(""+commentID)

                .then()
                .log().body()
                .body("id",equalTo(commentID))
        ;
    }

    @Test(dependsOnMethods = "GetCommentByID")
    public void UpdateComment()
    {
        String updName="Kaan__"+randomUreticisi.name().fullName();
        Map<String,String> updComment=new HashMap<>();
        updComment.put("name",updName);

        given()
                .spec(reqSpec)
                .body(updComment)
                .when()
                .put(""+commentID)

                .then()
                .log().body()
                .body("name", equalTo(updName))
        ;
    }

}
