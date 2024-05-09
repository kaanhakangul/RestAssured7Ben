import io.restassured.http.ContentType;
import io.restassured.matcher.ResponseAwareMatcher;
import io.restassured.response.Response;
import org.hamcrest.collection.HasItemInArray;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class _01_APITest {


    @Test
    public void test1() {

        given()
                // Hazırlık kodları buraya yazılıyor
                .when()
                // endpoint(url), metoduyla birlikte istek gönderilme aşaması

                .then();
        // assertion, test, data işlemleri
    }


    @Test
    public void statusCodeTest() {

        given()
                // gönderilecek bilgiler burada olacak
                .when()
                .get("http://api.zippopotam.us/us/90210")  // endpoint verdik
                // burası post, put, delete de olabilir tıpkı postmandeki gibi

                .then()
                .log().body()  // gelen yani response body yazdırıyoruz, all() dersek her şeyi yazar status code vs
                .statusCode(200); // test kısmı yani assertion status 200 diyoruz


    }

    @Test
    public void contentTypeTest() {

        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body() // dönen body bilgisini istedik
                .statusCode(200) // dönen kod 200 mu
                .contentType(ContentType.JSON); // Dönen responsun yani datanın tipi JSON mı kontrolü
        // buraya .TEXT mi de diyebiliyoruz

    }

    @Test
    public void checkCountryResponseBody() {

        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body() // dönen body bilgisini istedik
                .statusCode(200) // dönen kod 200 mu
                .body("country", equalTo("United States"));
        // bulunduğu yeri path i vererek assertion ı hamcrest kütüphanesi yapıyor
        ;
    }

    @Test
    public void checkStatesInResponseBody() {

        // Soru : "http://api.zippopotam.us/us/90210"  endpoint indne dönen
        // place dizisinin ilk elemanının state değerinin  "California"
        // olduğunu doğrulayınız
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .statusCode(200)
                .body("places[0].state", equalTo("California") // 0. indexteki state California mı?
                );
    }


    @Test
    public void checkHasItem() {
        // Soru : "http://api.zippopotam.us/tr/01000"  endpoint in dönen
        // place dizisinin herhangi bir elemanında  "Dörtağaç Köyü" değerinin
        // olduğunu doğrulayınız
        given()
                .when()
                .get("http://api.zippopotam.us/tr/01000")

                .then()
                .log().body()
                .statusCode(200)
                .body("places.'place name'", hasItem("Dörtağaç Köyü")) // places -- place name içerilerinde Dörtağaç Köyü var mı?
        ;
    }

    @Test
    public void bodyArrayHasSizeTest() {
        // Soru : "http://api.zippopotam.us/us/90210"  endpoint in dönen
        // place dizisinin dizi uzunluğunun 1 olduğunu doğrulayınız.
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .statusCode(200)
                .body("places", hasSize(1))// places in eleman uzunluğu kaç 1 mi kontrolü
                .body("places[0].state", equalTo("California"))
        // böyle birden fazla test eklenebilir

        ;
    }

    @Test
    public void pathParamTest(){
        given() // gönderilecek hazırlıklar
                .pathParam("ulke","us") // sol taraf isimlendirme sağ taraf değer
                .pathParam("postaKodu",90210) // path param deniyormuş bunlara
                .log().uri() // request linkini göndermeden önce görebiliyoruz böylece

                .when()
                .get("http://api.zippopotam.us/{ulke}/{postaKodu}") // tıpkı postmandaki gibi collection seviyesine çıkardık

                .then()
                .log().body()

                ;

    }

    @Test
    public void queryParamTest(){
        given() // gönderilecek hazırlıklar

                .param("page",1) //?page=1 gibi oldu
                .when()
                .get("hhttps://gorest.co.in/public/v1/users") // tıpkı postmandaki gibi collection seviyesine çıkardık
       //  https://gorest.co.in/public/v1/users?page=3 böyle de olabilir link soru işareti ile
                // yukarıdaki testlerde olduğu gibi de olabilir
                // eğer devin yaptığı yapıda link soru işeretli ise .param diyerek ? işareti dahil kısmı böyle veriyoruz.

                .then()

        ;

    }

    @Test
    public void queryParamTest2(){

        // https://gorest.co.in/public/v1/users?page=3
        // bu linkteki 1 den 10 kadar sayfaları çağırdığınızda response daki donen page degerlerinin
        // çağrılan page nosu ile aynı olup olmadığını kontrol ediniz.

        for (int i = 0; i <=10; i++) {


        given()
                .param("page",i) // soru işaretinden sonrasını böyle değişken yapıyoruz buradan otomatik urlnin sonuna ekliyor
                .log().uri() // bu kısım url i yazıyor

                .when()
                .get("https://gorest.co.in/public/v1/users")

                .then()
                .body("meta.pagination.page",equalTo(i));

        }
    }
}
