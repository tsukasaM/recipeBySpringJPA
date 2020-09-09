package jp.co.softbank.cxr.exam.domain.mapper;

import jp.co.softbank.cxr.exam.domain.model.Recipe;
import jp.co.softbank.cxr.exam.integration.entity.RecipeEntity;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

class RecipeEntityMapperTest {
  
  @Test
  void test_正常にドメインモデルからエンティティにマッピングできる場合() {
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    Timestamp createdAt = Timestamp.valueOf(LocalDateTime.now().format(dateTimeFormatter));
    Timestamp updatedAt = Timestamp.valueOf(LocalDateTime.now().format(dateTimeFormatter));

    Recipe recipe = Recipe.builder()
                          .title("トマトスープ")
                          .makingTime("15分")
                          .serves("5人")
                          .ingredients("玉ねぎ, トマト, スパイス, 水")
                          .cost(450)
                          .build();

    RecipeEntity actual = RecipeEntityMapper.toEntity(recipe);

    RecipeEntity expected = RecipeEntity.builder()
                                            .title("トマトスープ")
                                            .makingTime("15分")
                                            .serves("5人")
                                            .ingredients("玉ねぎ, トマト, スパイス, 水")
                                            .cost(450)
                                            .createdAt(createdAt)
                                            .updatedAt(updatedAt)
                                            .build();

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void test_正常にエンティティからレシピを表すドメインモデルにマッピングできる場合() {
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    Timestamp createdAt = Timestamp.valueOf(LocalDateTime.now().format(dateTimeFormatter));
    Timestamp updatedAt = Timestamp.valueOf(LocalDateTime.now().format(dateTimeFormatter));

    RecipeEntity recipeEntity = RecipeEntity.builder()
                                            .id(1)
                                            .title("トマトスープ")
                                            .makingTime("15分")
                                            .serves("5人")
                                            .ingredients("玉ねぎ, トマト, スパイス, 水")
                                            .cost(450)
                                            .createdAt(createdAt)
                                            .updatedAt(updatedAt)
                                            .build();

    Recipe actual = RecipeEntityMapper.fromEntity(recipeEntity, "Recipe successfully created!");

    Recipe expected = Recipe.builder()
                            .id(1)
                            .message("Recipe successfully created!")
                            .title("トマトスープ")
                            .makingTime("15分")
                            .serves("5人")
                            .ingredients("玉ねぎ, トマト, スパイス, 水")
                            .cost(450)
                            .build();

    assertThat(actual).isEqualTo(expected);
  }
}
