import Model.Location;
import Model.Place;
import Model.ToDo;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Test;
import org.testng.Assert;

import java.util.List;

import static org.hamcrest.Matchers.*;

import static io.restassured.RestAssured.given;

/**

 Task 1
 create a request to https://jsonplaceholder.typicode.com/todos/2
 expect status 200
 expect content type JSON
 expect title in response body to be "quis ut nam facilis et officia qui"
 */

public class _04_Tasks {

    @Test
    public void soru(){


        given()

                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")

                .then()
                //.log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("title",equalTo("quis ut nam facilis et officia qui"))
;



    }

    @Test
    public void soru2(){

        given()
                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")

                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("completed",equalTo(false)) // dışarı çıkarmadan assert yaptık
                ;

        boolean donenData=
        given()

                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")

                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .log().body()
                .extract().path("completed"); // Extract dediğimizde dışarı çıkarmış oluyoruz


        Assert.assertFalse(donenData);


    }

    /** Task 3

     create a request to https://jsonplaceholder.typicode.com/todos/2
     expect status 200
     Converting Into POJO  */

    @Test
    public void Task3(){

        ToDo todoNesne=
        given()

                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")

                .then()
                //.log().body()
                .extract().body().as(ToDo.class)

                ;

        System.out.println("todoNesne = " + todoNesne);
        System.out.println("todoNesne = " + todoNesne.getTitle());
        System.out.println("todoNesne = " + todoNesne.getId());
        System.out.println("todoNesne.isCompleted() = " + todoNesne.isCompleted());



    }
}
