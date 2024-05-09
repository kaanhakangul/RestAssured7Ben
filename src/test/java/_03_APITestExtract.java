import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;

public class _03_APITestExtract {


    @Test
    public void extractingJsonPath(){

       String ulkeAdi=
               given()

                       .when()
                       .get("http://api.zippopotam.us/us/90210")

                       .then().extract().path("country");
                        // .body yerine .extract().path(içerisinde ilgili kısım yer alacak)
                        // kullanarak değeri değişkeni atadık yani dışarı çıkardık. Artık kullanabileceğiz.

        System.out.println("ulkeAdi = " + ulkeAdi);
        Assert.assertEquals(ulkeAdi, "United States"); // dışarı aldığımız değeri karşılaştırdık.
        // Hemcrest kütüphanesi bizim veriyi dışarı almadan içerideyken assert yapmamızı sağlıyor

    }

    @Test
    public void extractingJsonPath2(){

       String stateAd= given()
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then().extract().path("places[0].state");

        System.out.println(stateAd);
        Assert.assertEquals(stateAd , "California");

    }

    @Test
    public void extractingJsonPath3(){

        String placeName=given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then().extract().path("places[0].'place name'");


        Assert.assertEquals(placeName , "Beverly Hills");


    }

    @Test
    public void extractingJsonPath4() {

       int limit= given()

                .when()
                .get("https://gorest.co.in/public/v1/users")

                .then()
                .log().body()
                .extract().path("meta.pagination.limit");

        System.out.println(limit);
Assert.assertEquals(limit, 10);  // değişkenin tipi burada önemli o yüzden int yaptım



    }

    @Test
    public void extractingJsonPath5() {

        List<Integer> idler=


                 given()
                .when()
                .get("https://gorest.co.in/public/v1/users")

                .then()
                .log().body()
                .extract().path("data.id"); // idlerin yer aldığı bir array lazım ya da dizi
        // data[0].id yerine data.id ile ilgili tüm idleri alabiliriz. Bunun için de list lazım

        System.out.println("idler = " + idler);

    }

    @Test
    public void extractingJsonPathResponseAll() {

        Response donenData=    // var donenData=pm.Response.Json() gibi


                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        .log().body()
                        .extract().response();

        List<Integer> idler=donenData.path("data.id"); // var id=dpnenData.id;
        List<String> names=donenData.path("data.name");
        int limit=donenData.path("meta.pagination.limit");

        System.out.println("idler = " + idler);
        System.out.println("names = " + names);
        System.out.println("limit = " + limit);

        Assert.assertTrue(idler.contains(6880125));
        Assert.assertTrue(names.contains("Karunanidhi Jain"));
        Assert.assertTrue(limit==10);

    }
}
