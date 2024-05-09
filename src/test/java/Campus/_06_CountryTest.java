package Campus;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class _06_CountryTest {

    RequestSpecification reqSpec; // Diğer testler de bu spec i görsün diye önce en yukarıya tanımladık.
    Faker randomUreticisi = new Faker();

    String countryName;
    String countryCode;

    String countryID;



    @BeforeClass
    public void LoginCampus() {
        baseURI = "https://test.mersys.io";
        // token cookies içerisinde geldi
        // cookies i burada almam lazım
        // request spec hazırlayacağım (before class  gibi çalışan şey)
        //environment variable: baseURI
        //{"username": "turkeyts", "password": "TechnoStudy123","rememberMe": "true"}

        // String userCredential = "{\"username\": \"turkeyts\", \"password\": \"TechnoStudy123\",\"rememberMe\": \"true\"}";

        Map<String, String> userCredMap = new HashMap<>(); // burada key value şeklinde mapin içerisinde attık login bilgilerimizi
        userCredMap.put("username", "turkeyts"); // eğer içerideki değişkenler string int şeklinde değişiyorsamap içinde ikinci stringi object yapabiliriz.
        userCredMap.put("password", "TechnoStudy123");
        userCredMap.put("rememberMe", "true");

        Cookies gelenCookies = // cookie bilgisini alabilmek için bunu ekledik.
                given()
                        .body(userCredMap) // tıpkı postmandaki gibi bodyde içeriği yolladık
                        .contentType(ContentType.JSON) // içerik tipi json dedik
                        .when()

                        .post("/auth/login")
                        .then()
                        //.log().all()
                        .statusCode(200)
                        .extract().response().getDetailedCookies() // cookiye aldık
                ;
        System.out.println("gelenCookies = " + gelenCookies);

        reqSpec = new RequestSpecBuilder() // request specification oluşturduk
                .addCookies(gelenCookies) // cookiesleri ekledik
                .setContentType(ContentType.JSON) // content tipini verdik
                .build(); // builk dedik
    }

    @Test
    public void CreateCountry() {
        // burada cookiesten aldığımız tookenın gitmesi lazım: spec
        countryName = randomUreticisi.address().country() + randomUreticisi.address().countryCode();
        countryCode = randomUreticisi.address().countryCode();

        Map<String, String> newCountry = new HashMap<>();
        newCountry.put("name", countryName);
        newCountry.put("code", countryCode);

        //Not: Spec bilgileri givendan hemen sonra yazılmalı.
        countryID= // değişkeni en yukarıda tanımladık burada da aldığımız idyi değişkene atadık.
        given()
                .spec(reqSpec) // yukarıda tanımladığımız her şey otomatik buraya da geldi.
                .body(newCountry)
                .when()
                .post("/school-service/api/countries") // baseURI kullandığımız için sadece kalan kısmını yazıyoruz

                .then()
                .log().body()
                .statusCode(201)
                .extract().path("id")   // update testinde kullanmak için idyi aldık.
        ;

    }

    @Test(dependsOnMethods = "CreateCountry")
    public void CreateCountryNegative() {
        // burada da cookiesten aldığımız tookenın gitmesi lazım: spec


        Map<String, String> reNewCountry = new HashMap<>();
        reNewCountry.put("name", countryName);
        reNewCountry.put("code", countryCode);

        //Not: Spec bilgileri givendan hemen sonra yazılmalı.

        given()
                .spec(reqSpec) // yukarıda tanımladığımız her şey otomatik buraya da geldi.
                .body(reNewCountry)
                .when()
                .post("/school-service/api/countries") // baseURI kullandığımız için sadece kalan kısmını yazıyoruz

                .then()
                .log().body()
                .statusCode(400)// negatifte 400 almamız gerekiyor code olarak.
        ;
    }

    @Test (dependsOnMethods = "CreateCountryNegative")
    public void updateCountry(){

        String updCountryName="Kaan "+randomUreticisi.address().country()+randomUreticisi.address().latitude();


        Map<String, String> updCountry = new HashMap<>();
        updCountry.put("id",countryID);
        updCountry.put("name", updCountryName); // yeni isim ürettirdim randım şekilde
        updCountry.put("code", countryCode);



        given()
                .spec(reqSpec) // yukarıda tanımladığımız her şey otomatik buraya da geldi.
                .body(updCountry)
                .when()
                .put("/school-service/api/countries") // update yaptığımız için put olacak

                .then()
                .log().body()
                .statusCode(200)
                .body("name",equalTo(updCountryName))//
;

    }



    @Test(dependsOnMethods = "updateCountry")
    public void DeleteCountry(){



        given()
                .spec(reqSpec)
                .when()
                .delete("/school-service/api/countries/"+countryID)

                .then()
                .statusCode(200)

                ;



    }

    @Test(dependsOnMethods = "DeleteCountry")
    public void DeleteCountryNegative(){



        given()
                .spec(reqSpec)
                .when()
                .delete("/school-service/api/countries/"+countryID)

                .then()
                .statusCode(400)

        ;

    }


}
