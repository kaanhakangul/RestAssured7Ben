import Model.Location;
import Model.Place;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
public class _05_PathAndJsonPath {

    @Test
    public void extractingPath(){


        // gelen body de bilgiyi dışarı almanın 2 yöntemini gördük
        // .extract.path("") , as(ToDo.class) şeklinde

        String postCode=
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .extract().path("'post code'") // arası boşlıklu olduğu için içerisine başına ve sonuna tek tırnak ekliyoruz
                ;

        System.out.println("postCode = " + postCode);
        int postCodeInt= Integer.parseInt(postCode);
        System.out.println("postCodeInt = " + postCodeInt);

        // Normalde dışarı çıkarırken post code String olarak çıkıyor çünkü öyle verilmiş değişkeni
        // Peki biz bunu inte çevirmek istersek parse işlemi yapmamız gerekir
        //Şimdi aşağıda bu işlemsiz haline bakacağız.

    }
    @Test
    public void extractingJsonPath(){


       // Yukarıdaki işlemin aynısını parse yapmadan jsonPath().getInt("buraya istediğimiz alanı yazıyoruz");
        // şeklinde alabiliyoruz. Tabii ki given() dan öncesine alacağımız değişken tipi ne ise onu yazıyoruz.
        // Stringdi ve biz bunu int yapmak istedik o yüzden int olarak tanımlıyoruz en başa yazdığımız değişkeni

        int postCode=
                given()
                        .when()
                        .get("http://api.zippopotam.us/us/90210")

                        .then()
                        .log().body()
                        .extract().jsonPath().getInt("'post code'")
                ;

        System.out.println("postCode = " + postCode);

    }

    @Test
    public void getZipCode(){

        Response response=
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .extract().response()

                ;

        Location locationAsPath= response.as(Location.class);   // bütün class yapısını yazmak zorundayız
        System.out.println("locationAsPath.getPlaces() = " + locationAsPath.getPlaces()); // bu as li kalıpta sadece place lazım olsa bile hepsini yazmak zorundayız

        List<Place> places=response.jsonPath().getList("place", Place.class);
        // Sadece Place dizisi lazım ise diğerlerini yazmak zorunda değilsin.

        // Daha önceki örneklerde (as) Clas dönüşümleri için tüm yapıya karşılık gelen
        // gereken tüm classları yazarak dönüştürüp istediğimiz elemanlara ulaşıyorduk.

        // Burada ise(JsonPath) aradaki bir veriyi clasa dönüştürerek bir list olarak almamıza
        // imkan veren JSONPATH i kullandık.Böylece tek class ile veri alınmış oldu
        // diğer class lara gerek kalmadan

        // path : class veya tip dönüşümüne imkan veremeyen direk veriyi verir. List<String> gibi
        // jsonPath : class dönüşümüne ve tip dönüşümüne izin vererek , veriyi istediğimiz formatta verir.

    }

    // https://gorest.co.in/public/v1/users  endpointte dönen Sadece Data Kısmını POJO
    // dönüşümü ile alarak yazdırınız.

    @Test
    public void SoruSon(){

        given()

                .when()
                .get("https://gorest.co.in/public/v1/users")

                .then()
                .log().body()

                ;


    }

}
