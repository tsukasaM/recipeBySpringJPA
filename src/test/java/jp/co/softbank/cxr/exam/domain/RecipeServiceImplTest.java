package jp.co.softbank.cxr.exam.domain;

import jp.co.softbank.cxr.exam.common.exception.ApplicationException;
import jp.co.softbank.cxr.exam.domain.model.Recipe;
import jp.co.softbank.cxr.exam.domain.repository.RecipeRepository;
import jp.co.softbank.cxr.exam.domain.service.RecipeService;
import jp.co.softbank.cxr.exam.integration.entity.RecipeEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("RecipeService のテスト")
@ExtendWith(SpringExtension.class)
@SpringBootTest
class RecipeServiceImplTest {

  @Autowired
  private RecipeService recipeService;

  @MockBean
  RecipeRepository recipeRepository;

  @Test
  void test_レシピを適切に登録できる事() {

    //setup
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    Timestamp createdAt = Timestamp.valueOf(LocalDateTime.now().format(dateTimeFormatter));
    Timestamp updatedAt = Timestamp.valueOf(LocalDateTime.now().format(dateTimeFormatter));

    RecipeEntity recipeEntity = RecipeEntity.builder()
                                            .title("トマトスープ")
                                            .makingTime("15分")
                                            .serves("5人")
                                            .ingredients("玉ねぎ, トマト, スパイス, 水")
                                            .cost(450)
                                            .createdAt(createdAt)
                                            .updatedAt(updatedAt)
                                            .build();

    RecipeEntity createdRecipeEntity = RecipeEntity.builder()
                                                   .id(1)
                                                   .title("トマトスープ")
                                                   .makingTime("15分")
                                                   .serves("5人")
                                                   .ingredients("玉ねぎ, トマト, スパイス, 水")
                                                   .cost(450)
                                                   .createdAt(createdAt)
                                                   .updatedAt(updatedAt)
                                                   .build();

    when(recipeRepository.create(recipeEntity)).thenReturn(createdRecipeEntity);


    Recipe expected = generateRecipeWithMessage("Recipe successfully created!");

    ////execute
    Recipe actual = recipeService.createRecipe(generateRecipe());

    //assert
    assertThat(actual).isEqualTo(expected);
    verify(recipeRepository).create(recipeEntity);
  }

  @Test
  void test_レシピの情報を適切に取得できる事() {

    //setup
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    Timestamp timestamp = Timestamp.valueOf(LocalDateTime.of(2020,1,1,15,0).format(dateTimeFormatter));

    RecipeEntity recipeEntity = RecipeEntity.builder()
                                            .id(1)
                                            .title("トマトスープ")
                                            .makingTime("15分")
                                            .serves("5人")
                                            .ingredients("玉ねぎ, トマト, スパイス, 水")
                                            .cost(450)
                                            .createdAt(timestamp)
                                            .updatedAt(timestamp)
                                            .build();

    when(recipeRepository.findById(1)).thenReturn(recipeEntity);

    Recipe expected = generateRecipeWithMessage("Recipe details by id");

    //execute
    Recipe actual = recipeService.getRecipe(1);

    //assert
    assertThat(actual).isEqualTo(expected);
    verify(recipeRepository).findById(1);
  }

  @Test
  void test_レシピの情報を複数適切に取得できる事() {

    //setup
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    Timestamp timestamp = Timestamp.valueOf(LocalDateTime.of(2020,1,1,15,0).format(dateTimeFormatter));

    List<RecipeEntity> recipeEntities = Arrays.asList(RecipeEntity.builder()
                                            .id(1)
                                            .title("トマトスープ")
                                            .makingTime("15分")
                                            .serves("5人")
                                            .ingredients("玉ねぎ, トマト, スパイス, 水")
                                            .cost(450)
                                            .createdAt(timestamp)
                                            .updatedAt(timestamp)
                                            .build(),
                                        RecipeEntity.builder()
                                            .id(2)
                                            .title("オムライス")
                                            .makingTime("30分")
                                            .serves("5人")
                                            .ingredients("玉ねぎ, トマト, スパイス, 水")
                                            .cost(450)
                                            .createdAt(timestamp)
                                            .updatedAt(timestamp)
                                            .build());

    when(recipeRepository.findAll()).thenReturn(recipeEntities);

    List<Recipe> expected = Arrays.asList(Recipe.builder()
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

    //execute
    List<Recipe> actual = recipeService.getRecipeList();

    //assert
    assertThat(actual).isEqualTo(expected);
    verify(recipeRepository).findAll();
  }

  @Test
    void test_指定したIDのレシピが存在しない場合ApplicationExceptionが発生する事() {

    //execute
    ApplicationException actual = assertThrows(ApplicationException.class, () -> recipeService.getRecipe(9999));

    //assert
    assertThat(actual.getMessage()).isEqualTo("No Recipe found");
  }

  @Test
  void test_指定したIDのレシピを適切に更新できる事() {

    //setup
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    Timestamp createdAt = Timestamp.valueOf(LocalDateTime.of(2020,1,1,15,0).format(dateTimeFormatter));
    Timestamp newTimeStamp = Timestamp.valueOf(LocalDateTime.now().format(dateTimeFormatter));

    RecipeEntity recipeEntity = RecipeEntity.builder()
                                            .id(1)
                                            .title("トマトスープ")
                                            .makingTime("15分")
                                            .updatedAt(newTimeStamp)
                                            .build();

    RecipeEntity updatedRecipeEntity = RecipeEntity.builder()
                                                   .id(1)
                                                   .title("トマトスープ")
                                                   .makingTime("15分")
                                                   .serves("5人")
                                                   .ingredients("玉ねぎ, トマト, スパイス, 水")
                                                   .cost(450)
                                                   .createdAt(createdAt)
                                                   .updatedAt(newTimeStamp)
                                                   .build();

    when(recipeRepository.update(recipeEntity)).thenReturn(updatedRecipeEntity);

    Recipe requestRecipe = Recipe.builder()
                                 .title("トマトスープ")
                                 .makingTime("15分")
                                 .build();

    Recipe expected = generateRecipeWithMessage("Recipe successfully updated!");

    ////execute
    Recipe actual = recipeService.updateRecipe(requestRecipe, 1);

    //assert
    assertThat(actual).isEqualTo(expected);
    verify(recipeRepository).update(recipeEntity);
  }

  @Test
  void test_指定したIDのレシピを適切に削除できる事() {

    //setup
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    Timestamp timestamp = Timestamp.valueOf(LocalDateTime.of(2020,1,1,15,0).format(dateTimeFormatter));

    RecipeEntity recipeEntity = RecipeEntity.builder()
                                            .id(1)
                                            .title("トマトスープ")
                                            .makingTime("15分")
                                            .serves("5人")
                                            .ingredients("玉ねぎ, トマト, スパイス, 水")
                                            .cost(450)
                                            .createdAt(timestamp)
                                            .updatedAt(timestamp)
                                            .build();

    when(recipeRepository.delete(1)).thenReturn(recipeEntity);

    Recipe expected = Recipe.builder()
                            .id(1)
                            .title("トマトスープ")
                            .makingTime("15分")
                            .serves("5人")
                            .ingredients("玉ねぎ, トマト, スパイス, 水")
                            .cost(450)
                            .build();

    //execute
    Recipe actual = recipeService.deleteRecipe(1);

    //assert
    assertThat(actual).isEqualTo(expected);
    verify(recipeRepository).delete(1);
  }


  private Recipe generateRecipeWithMessage(String message) {
    return Recipe.builder()
                 .id(1)
                 .message(message)
                 .title("トマトスープ")
                 .makingTime("15分")
                 .serves("5人")
                 .ingredients("玉ねぎ, トマト, スパイス, 水")
                 .cost(450)
                 .build();
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

}
