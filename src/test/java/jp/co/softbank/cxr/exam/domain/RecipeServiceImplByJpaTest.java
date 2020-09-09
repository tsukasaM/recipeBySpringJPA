package jp.co.softbank.cxr.exam.domain;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.Operations;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.destination.Destination;
import com.ninja_squad.dbsetup.operation.Operation;
import jp.co.softbank.cxr.exam.common.exception.ApplicationException;
import jp.co.softbank.cxr.exam.domain.model.Recipe;
import jp.co.softbank.cxr.exam.domain.repository.RecipeRepository;
import jp.co.softbank.cxr.exam.domain.service.RecipeService;
import jp.co.softbank.cxr.exam.integration.entity.RecipeEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.ninja_squad.dbsetup.Operations.deleteAllFrom;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("RecipeService のテスト")
@ExtendWith(SpringExtension.class)
@SpringBootTest
class RecipeServiceImplByJpaTest {

  @Autowired
  private RecipeService recipeService;

  @Autowired
  private DataSource dataSource;

  private static final Operation DELETE_ALL = deleteAllFrom("recipes");
  private final Operation INSERT_RECIPE_DATA = Operations.insertInto("recipes").columns("id",
                                                                                              "title",
                                                                                              "making_time",
                                                                                              "serves",
                                                                                              "ingredients",
                                                                                              "cost",
                                                                                              "created_at",
                                                                                              "updated_at")
                                                                                      .values( 1,
                                                                                          "トマトスープ",
                                                                                          "15分",
                                                                                          "5人",
                                                                                          "玉ねぎ, トマト, スパイス, 水",
                                                                                          450,
                                                                                          Timestamp.valueOf(LocalDateTime.parse("2016-01-10 12:10:12", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))),
                                                                                          Timestamp.valueOf(LocalDateTime.parse("2016-01-10 12:10:12", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))))
                                                                                      .values(2,
                                                                                          "オムライス",
                                                                                          "30分",
                                                                                          "5人",
                                                                                          "玉ねぎ, トマト, スパイス, 水",
                                                                                          450,
                                                                                          Timestamp.valueOf(LocalDateTime.parse("2016-01-11 13:10:12", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))),
                                                                                          Timestamp.valueOf(LocalDateTime.parse("2016-01-11 13:10:12", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))))
                                                                                      .build();

  private void dbSetup(Operation operation) {
    Destination destination = new DataSourceDestination(dataSource);
    DbSetup dbSetup = new DbSetup(destination, operation);
    dbSetup.launch();
  }

  @BeforeEach
  void setUp() {
    dbSetup(Operations.sequenceOf(
        DELETE_ALL,
        INSERT_RECIPE_DATA
    ));
  }

  @Test
  void test_レシピを適切に登録できる事() {

    //setup
    Recipe expected = generateRecipeWithMessage("Recipe successfully created!");

    expected.setId(3);

    ////execute
    Recipe actual = recipeService.createRecipe(generateRecipe());

    //assert
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void test_レシピの情報を適切に取得できる事() {

    //setup
    Recipe expected = generateRecipeWithMessage("Recipe details by id");

    //execute
    Recipe actual = recipeService.getRecipe(1);

    //assert
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void test_レシピの情報を複数適切に取得できる事() {

    //setup
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
    Recipe requestRecipe = Recipe.builder()
                                 .title("トマトスープ")
                                 .makingTime("15分")
                                 .build();

    Recipe expected = generateRecipeWithMessage("Recipe successfully updated!");

    ////execute
    Recipe actual = recipeService.updateRecipe(requestRecipe, 1);

    //assert
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void test_指定したIDのレシピを適切に削除できる事() {

    //setup
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

    List<Recipe> expected2 = Collections.singletonList(
                                                        Recipe.builder()
                                                              .id(2)
                                                              .title("オムライス")
                                                              .makingTime("30分")
                                                              .serves("5人")
                                                              .ingredients("玉ねぎ, トマト, スパイス, 水")
                                                              .cost(450)
                                                              .build());

    //execute
    List<Recipe> actual2 = recipeService.getRecipeList();

    //assert
    assertThat(actual2).isEqualTo(expected2);
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
