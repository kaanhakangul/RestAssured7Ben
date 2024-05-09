import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class _02_APITest {


    RequestSpecification requestSpec;
    ResponseSpecification responseSpec;

    @BeforeClass
    public void Setup() {

        baseURI = "https://gorest.co.in/public/v1";  // hazırda tanımlanmış restAssured a ait temel değişken
        // static olduğu için her yerde kullanabiliyoruz.
        requestSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .log(LogDetail.URI)
                .build();

        responseSpec = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .log(LogDetail.BODY)
                .expectContentType(ContentType.JSON)
                .build();
    }


        @Test
        public void Test1 () {

            given()

                    .spec(requestSpec)
                    .when()
                    .get("/users") // başında http yok ise baseURI den urlyi alıyor yani otomatik çalışıyor

                    .then()
                    .spec(responseSpec)
            ;

        }

        @Test
        public void Test2 () {


            given()
                    .spec(requestSpec)
                    .when()
                    .get("/users")  // eğer buradaki tırnak için http kısmı yoksa baseURI hemen başına ekleniyor

                    .then()
                    .spec(responseSpec)
            ;


        }

        @Test
        public void Test3 () {

            given()
                    .spec(requestSpec)
                    .when()
                    .get("/users")

                    .then()
                    .spec(responseSpec)
            ;

        }

    }
