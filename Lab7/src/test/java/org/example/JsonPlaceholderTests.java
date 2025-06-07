package org.example;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class JsonPlaceholderTests {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";
    }

    // Первый позитивный. Проверить, что запрос прошёл и ответ не пустой.
    @Test
    public void testGetAllPosts() {
        given()
                .when()
                .get("/posts")
                .then()
                .statusCode(200)
                .body("$", hasSize(greaterThan(0)))
                .body("[0].userId", notNullValue())
                .body("[0].id", notNullValue())
                .body("[0].title", notNullValue())
                .body("[0].body", notNullValue());
    }

    //Второй позитивный. Проверить на то, что запрос прошёл и сверить все полученные данные
    @Test
    public void testGetSpecificPost() {
        int postId = 1;

        given()
                .pathParam("id", postId)
                .when()
                .get("/posts/{id}")
                .then()
                .statusCode(200)
                .body("userId", equalTo(1))
                .body("id", equalTo(postId))
                .body("title", equalTo("sunt aut facere repellat provident occaecati excepturi optio reprehenderit"))
                .body("body", containsString("quia et suscipit"));
    }

    // Запросить несуществующий пост и получить 404 и пустой ответ.
    @Test
    public void testGetNonExistentPost() {
        int postId = 9999;

        given()
                .pathParam("id", postId)
                .when()
                .get("/posts/{id}")
                .then()
                .statusCode(404)
                .body("isEmpty()", is(true));
    }

    // Запросить невалидный эндпоинт и получить ответ 404
    @Test
    public void testInvalidEndpoint() {
        given()
                .when()
                .get("/bleh")
                .then()
                .statusCode(404);
    }
}