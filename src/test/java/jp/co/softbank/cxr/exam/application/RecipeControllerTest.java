package jp.co.softbank.cxr.exam.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.co.softbank.cxr.exam.application.controller.RecipeController;
import jp.co.softbank.cxr.exam.application.payload.ApplicationErrorResponse;
import jp.co.softbank.cxr.exam.application.payload.CreateRecipePayload;
import jp.co.softbank.cxr.exam.application.payload.CreateRecipeRequest;
import jp.co.softbank.cxr.exam.application.payload.CreateRecipesResponse;
import jp.co.softbank.cxr.exam.application.payload.GetRecipePayload;
import jp.co.softbank.cxr.exam.application.payload.GetRecipesResponse;
import jp.co.softbank.cxr.exam.application.payload.RestErrorResponse;
import jp.co.softbank.cxr.exam.application.payload.GetAllRecipePayload;
import jp.co.softbank.cxr.exam.application.payload.GetAllRecipesResponse;
import jp.co.softbank.cxr.exam.application.payload.UpdateRecipePayload;
import jp.co.softbank.cxr.exam.application.payload.UpdateRecipeRequest;
import jp.co.softbank.cxr.exam.application.payload.UpdateRecipesResponse;
import jp.co.softbank.cxr.exam.common.exception.ApplicationException;
import jp.co.softbank.cxr.exam.domain.model.Recipe;
import jp.co.softbank.cxr.exam.domain.service.RecipeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("RecipeController に対するテスト")
@ExtendWith(SpringExtension.class)
@WebMvcTest(RecipeController.class)
class RecipeControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  RecipeService recipeService;

  @Test
  void test_recipe_apiのentryにアクセスすると200レスポンスが返される事() throws Exception {
    // execute & assert
    mockMvc.perform(get("/"))
        .andExpect(status().isOk());
  }

  @Test
  void test_POSTでリクエストしてレシピが登録できる事() throws Exception {

    //setup
    ObjectMapper mapper = new ObjectMapper();

    String requestJson = mapper.writeValueAsString(CreateRecipeRequest.builder()
                                                                      .title("トマトスープ")
                                                                      .makingTime("15分")
                                                                      .serves("5人")
                                                                      .ingredients("玉ねぎ, トマト, スパイス, 水")
                                                                      .cost("450")
                                                                      .build());

    Recipe recipe = generateRecipe();

    Recipe createdRecipe = generateRecipeWithMessage("Recipe successfully created!");

    when(recipeService.createRecipe(recipe)).thenReturn(createdRecipe);

    List<CreateRecipePayload> createRecipePayloads = new ArrayList<>();

    CreateRecipePayload createrecipePayload = CreateRecipePayload.builder()
                                                                 .title("トマトスープ")
                                                                 .makingTime("15分")
                                                                 .serves("5人")
                                                                 .ingredients("玉ねぎ, トマト, スパイス, 水")
                                                                 .cost("450")
                                                                 .build();

    createRecipePayloads.add(createrecipePayload);

    String expected = mapper.writeValueAsString(CreateRecipesResponse.builder()
                                                                      .message("Recipe successfully created!")
                                                                      .recipes(createRecipePayloads)
                                                                      .build());
    // execute & assert
    mockMvc.perform(post("/recipes")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(requestJson))
        .andExpect(content().json(expected))
        .andExpect(status().isCreated());

    verify(recipeService).createRecipe(recipe);
  }

  @Test
  void test_POSTでリクエストして必須項目が欠けていた場合400でエラーメッセージが返される事() throws Exception {

    //setup
    ObjectMapper mapper = new ObjectMapper();

    String requestJson = mapper.writeValueAsString(CreateRecipePayload.builder()
                                                                      .title(null)
                                                                      .makingTime("15分")
                                                                      .serves("5人")
                                                                      .ingredients("玉ねぎ, トマト, スパイス, 水")
                                                                      .cost("450")
                                                                      .build());

    String expected = mapper.writeValueAsString(RestErrorResponse.builder()
                                                             .message("Recipe creation failed!")
                                                             .required("title, making_time, serves, ingredients, cost")
                                                             .build());

    // execute & assert
    mockMvc.perform(post("/recipes")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(requestJson))
        .andExpect(content().json(expected))
        .andExpect(status().isBadRequest());

  }

  @Test
  void test_GETでリクエストしてレシピのidで一件検索ができる事() throws Exception {

    //setup
    ObjectMapper mapper = new ObjectMapper();

    List<GetRecipePayload> getRecipePayload = Collections.singletonList(GetRecipePayload.builder()
                                                                                        .title("トマトスープ")
                                                                                        .makingTime("15分")
                                                                                        .serves("5人")
                                                                                        .ingredients("玉ねぎ, トマト, スパイス, 水")
                                                                                        .cost("450")
                                                                                        .build());

    String expected = mapper.writeValueAsString(GetRecipesResponse.builder()
                                                                  .message("Recipe details by id")
                                                                  .recipes(getRecipePayload)
                                                                  .build());

    Recipe recipe = generateRecipeWithMessage("Recipe details by id");

    when(recipeService.getRecipe(1)).thenReturn(recipe);

    // execute & assert
    mockMvc.perform(get("/recipes/1"))
        .andExpect(status().isOk())
        .andExpect(content().json(expected));

    verify(recipeService).getRecipe(1);
  }

  @Test
  void test_GETでリクエストしたIDのレシピが存在しない場合404でエラーメッセージが返される事() throws Exception {

    //setup
    ObjectMapper mapper = new ObjectMapper();

    when(recipeService.getRecipe(9999)).thenThrow(new ApplicationException("No Recipe found"));

    String expected = mapper.writeValueAsString(ApplicationErrorResponse.builder()
                                                             .message("No Recipe found")
                                                             .build());

    // execute & assert
    mockMvc.perform(get("/recipes/9999"))
        .andExpect(status().isNotFound())
        .andExpect(content().json(expected));

    verify(recipeService).getRecipe(9999);
  }

  @Test
  void test_GETでリクエストしてレシピの全権検索ができる事() throws Exception {

    //setup
    ObjectMapper mapper = new ObjectMapper();

    List<GetAllRecipePayload> getAllRecipePayloadList = Arrays.asList(GetAllRecipePayload.builder()
                                                                                            .id(1)
                                                                                            .title("トマトスープ")
                                                                                            .makingTime("15分")
                                                                                            .serves("5人")
                                                                                            .ingredients("玉ねぎ, トマト, スパイス, 水")
                                                                                            .cost("450")
                                                                                            .build(),
                                                                          GetAllRecipePayload.builder()
                                                                                            .id(2)
                                                                                            .title("オムライス")
                                                                                            .makingTime("30分")
                                                                                            .serves("5人")
                                                                                            .ingredients("玉ねぎ, トマト, スパイス, 水")
                                                                                            .cost("450")
                                                                                            .build());
    String expected = mapper.writeValueAsString(GetAllRecipesResponse.builder()
                                                                     .recipes(getAllRecipePayloadList)
                                                                     .build());
    List<Recipe> recipes = Arrays.asList(Recipe.builder()
                                               .id(1)
                                               .title("トマトスープ")
                                               .makingTime("15分")
                                               .serves("5人")
                                               .ingredients("玉ねぎ, トマト, スパイス, 水")
                                               .cost(450)
                                               .build(),
                                         Recipe.builder()
                                               .id(2)
                                               .title("オムライス")
                                               .makingTime("30分")
                                               .serves("5人")
                                               .ingredients("玉ねぎ, トマト, スパイス, 水")
                                               .cost(450)
                                               .build());

    when(recipeService.getRecipeList()).thenReturn(recipes);

    // execute & assert
    mockMvc.perform(get("/recipes"))
        .andExpect(status().isOk())
        .andExpect(content().json(expected));

    verify(recipeService).getRecipeList();
  }

  @Test
  void test_PATCHでリクエストしてレシピが更新できる事() throws Exception {

    //setup
    ObjectMapper mapper = new ObjectMapper();

    String requestJson = mapper.writeValueAsString(UpdateRecipeRequest.builder()
                                                                      .title("トマトスープ")
                                                                      .makingTime("15分")
                                                                      .build());

    Recipe requestRecipe = Recipe.builder()
                                 .title("トマトスープ")
                                 .makingTime("15分")
                                 .build();

    Recipe updatedRecipe = generateRecipeWithMessage("Recipe successfully updated!");

    when(recipeService.updateRecipe(requestRecipe, 1)).thenReturn(updatedRecipe);

    List<UpdateRecipePayload> updateRecipePayloads = new ArrayList<>();

    UpdateRecipePayload updateRecipePayload = UpdateRecipePayload.builder()
                                                                 .title("トマトスープ")
                                                                 .makingTime("15分")
                                                                 .serves("5人")
                                                                 .ingredients("玉ねぎ, トマト, スパイス, 水")
                                                                 .cost("450")
                                                                 .build();

    updateRecipePayloads.add(updateRecipePayload);

    String expected = mapper.writeValueAsString(UpdateRecipesResponse.builder()
                                                                     .message("Recipe successfully updated!")
                                                                     .recipes(updateRecipePayloads)
                                                                     .build());
    // execute & assert
    mockMvc.perform(patch("/recipes/1")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(requestJson))
        .andExpect(content().json(expected))
        .andExpect(status().isOk());

    verify(recipeService).updateRecipe(requestRecipe, 1);
  }

  @Test
  void test_PATCHでリクエストしたIDのレシピが存在しない場合404でエラーメッセージが返される事() throws Exception {

    //setup
    ObjectMapper mapper = new ObjectMapper();

    String requestJson = mapper.writeValueAsString(UpdateRecipeRequest.builder()
                                                                      .title("トマトスープ")
                                                                      .makingTime("15分")
                                                                      .build());

    Recipe requestRecipe = Recipe.builder()
                                  .title("トマトスープ")
                                  .makingTime("15分")
                                  .build();

    when(recipeService.updateRecipe(requestRecipe, 9999)).thenThrow(new ApplicationException("No Recipe found"));

    String expected = mapper.writeValueAsString(ApplicationErrorResponse.builder()
                                                                        .message("No Recipe found")
                                                                        .build());

    // execute & assert
    mockMvc.perform(patch("/recipes/9999")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(requestJson))
        .andExpect(status().isNotFound())
        .andExpect(content().json(expected));

    verify(recipeService).updateRecipe(requestRecipe, 9999);
  }


  @Test
  void test_DELETEでリクエストして指定したidのレシピが削除できる事() throws Exception {

    //setup
    when(recipeService.deleteRecipe(1)).thenReturn(Recipe.builder()
                                                             .id(1)
                                                             .title("トマトスープ")
                                                             .makingTime("15分")
                                                             .serves("5人")
                                                             .ingredients("玉ねぎ, トマト, スパイス, 水")
                                                             .cost(450)
                                                             .build());
    // execute & assert
    mockMvc.perform(delete("/recipes/1"))
        .andExpect(status().isNoContent());

    verify(recipeService).deleteRecipe(1);
  }

  @Test
  void test_DELETEでリクエストしたIDのレシピが存在しない場合404でエラーメッセージが返される事() throws Exception {

    //setup
    ObjectMapper mapper = new ObjectMapper();

    when(recipeService.deleteRecipe(9999)).thenThrow(new ApplicationException("No Recipe found"));

    String expected = mapper.writeValueAsString(ApplicationErrorResponse.builder()
                                                                        .message("No Recipe found")
                                                                        .build());
    // execute & assert
    mockMvc.perform(delete("/recipes/9999"))
        .andExpect(status().isNotFound())
        .andExpect(content().json(expected));

    verify(recipeService).deleteRecipe(9999);
  }

  private Recipe generateRecipe() {
    return Recipe.builder()
                 .title("トマトスープ")
                 .makingTime("15分")
                 .serves("5人")
                 .ingredients("玉ねぎ, トマト, スパイス, 水")
                 .cost(450)
                 .build();
  }

  private Recipe generateRecipeWithMessage(String message) {
    return Recipe.builder()
                 .message(message)
                 .title("トマトスープ")
                 .makingTime("15分")
                 .serves("5人")
                 .ingredients("玉ねぎ, トマト, スパイス, 水")
                 .cost(450)
                 .build();
  }
}
