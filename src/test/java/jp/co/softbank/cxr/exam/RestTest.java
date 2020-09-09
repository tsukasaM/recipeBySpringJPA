package jp.co.softbank.cxr.exam;

import static io.restassured.RestAssured.*;
import static io.restassured.module.jsv.JsonSchemaValidator.*;
import static jp.co.softbank.cxr.exam.TestUtils.*;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;

import io.restassured.RestAssured;
import org.apache.commons.validator.routines.UrlValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RestTest {
  
  private static final String POST_RECIPE_PATH = "/recipes";
  private static final String GET_ALL_RECIPE_PATH = "/recipes";
  private static final String GET_ONE_RECIPE_PATH = "/recipes/";
  private static final String UPDATE_RECIPE_PATH = "/recipes/";
  private static final String DELETE_RECIPE_PATH = "/recipes/";
  
  @BeforeEach
  void setupAll() {
    RestAssured.baseURI = "https://secret-scrubland-12818.herokuapp.com/";
  }
  
  
  @DisplayName("RestAssured.baseURI")
  @Test
  void test_contains_a_valid_URL() {
    String[] schemes = new String[] {"http", "https"};
    UrlValidator urlValidator = new UrlValidator(schemes, UrlValidator.ALLOW_LOCAL_URLS);
    boolean actual = urlValidator.isValid(RestAssured.baseURI);
    assertThat(actual).isTrue();
  }
  
  @DisplayName("API server")
  @Test
  void test_return_status_code_200_when_accessing() {
    given()
      .get()
      .then()
        .statusCode(200);
  }
  
  @DisplayName("/GET recipes")
  @Test
  void test_should_GET_all_the_recipes() {
    given()
      .get(GET_ALL_RECIPE_PATH)
      .then()
        .statusCode(200)
        .assertThat()
        .body(matchesJsonSchemaInClasspath("get-recipes-schema.json"));
  }
  
  @DisplayName("/POST recipes")
  @Test
  void test_should_not_POST_recipe_when_title_making_time_serves_ingredients_cost_params_are_missing() {
    given()
      .when()
        .contentType("application/json")
        .body("{}")
        .post(POST_RECIPE_PATH)
      .then()
        .statusCode(400)
        .body("message", equalTo("Recipe creation failed!"))
        .body("required", equalTo("title, making_time, serves, ingredients, cost"));
      
  }  
  
  @DisplayName("/POST recipes")
  @Test
  void test_should_POST_a_recipe() throws Exception {
    given()
      .when()
        .contentType("application/json")
        .body(readMessageFromFile("request-POST-recipe.json"))
        .post(POST_RECIPE_PATH)
      .then()
        .statusCode(201)
        .body("message", equalTo("Recipe successfully created!"))
        .body("recipe[0].title", equalTo("トマトスープ"))
        .body("recipe[0].making_time", equalTo("15分"))
        .body("recipe[0].serves", equalTo("5人"))
        .body("recipe[0].ingredients", equalTo("玉ねぎ, トマト, スパイス, 水"))
        .body("recipe[0].cost", equalTo("450"));
  }
  
  @DisplayName("/GET/{id} recipe")
  @Test
  void test_should_GET_a_recipe_by_the_given_id() throws Exception {
    String id = given().get(GET_ALL_RECIPE_PATH).jsonPath().getString("recipes[0].id");
    
    given()
      .get(GET_ONE_RECIPE_PATH + id)
      .then()
        .statusCode(200)
        .assertThat()
        .body(matchesJsonSchemaInClasspath("get-recipe-schema.json"))
        .body("message", equalTo("Recipe details by id"));
  }
    
  @DisplayName("/PATCH/{id} recipe")
  @Test
  void test_should_UPDATE_a_recipe() throws Exception {
    String id = given().get(GET_ALL_RECIPE_PATH).jsonPath().getString("recipes[0].id");
    
    given()
     .when()
       .contentType("application/json")
       .body(readMessageFromFile("request-PATCH-recipe.json"))
       .patch(UPDATE_RECIPE_PATH + id)
     .then()
       .statusCode(200)
       .body("message", equalTo("Recipe successfully updated!"))
       .body("recipe[0].title", equalTo("オムライス"))
       .body("recipe[0].making_time", equalTo("20分"))
       .body("recipe[0].serves", equalTo("7人"))
       .body("recipe[0].ingredients", equalTo("玉ねぎ,卵,スパイス,醤油"))
       .body("recipe[0].cost", equalTo("400"));
  }
  
  @DisplayName("/DELETE/{id} recipe")
  @Test
  void test_should_DELETE_a_recipe() throws Exception {
    String id = given().get(GET_ALL_RECIPE_PATH).jsonPath().getString("recipes[0].id");
    
    given()
    .delete(DELETE_RECIPE_PATH + id)
    .then()
      .statusCode(204);
  }
  
  @DisplayName("/GET/{id} recipe")
  @Test
  void test_should_GET_a_recipe_return_error() throws Exception {
    given()
      .get(GET_ONE_RECIPE_PATH + "2147483647")
      .then()
        .statusCode(404)
        .body("message", equalTo("No Recipe found"));
  }
  
  @DisplayName("/PATCH/{id} recipe")
  @Test
  void test_should_UPDATE_a_recipe_return_error() throws Exception {
    given()
    .when()
      .contentType("application/json")
      .body(readMessageFromFile("request-PATCH-recipe.json"))
      .patch(UPDATE_RECIPE_PATH + "2147483647")
    .then()
      .statusCode(404)
      .body("message", equalTo("No Recipe found"));
  }
  
  @DisplayName("/DELETE/{id} recipe")
  @Test
  void test_should_DELETE_a_recipe_return_error() throws Exception {
    given()
    .delete(DELETE_RECIPE_PATH + "2147483647")
    .then()
      .statusCode(404)
      .body("message", equalTo("No Recipe found"));
  }
  
}
