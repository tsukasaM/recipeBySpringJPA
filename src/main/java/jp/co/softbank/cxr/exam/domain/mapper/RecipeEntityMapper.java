package jp.co.softbank.cxr.exam.domain.mapper;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import jp.co.softbank.cxr.exam.domain.model.Recipe;
import jp.co.softbank.cxr.exam.integration.entity.RecipeEntity;


/**
 * レシピのドメインモデルとエンティティを相互に変換を行うマッパー.
 */
public final class RecipeEntityMapper {

  private static final String LOCAL_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

  private RecipeEntityMapper() {
    // Do Nothing.
  }

  /**
   * ドメインモデルからレシピのエンティティに変換します.
   *
   * @param recipe レシピのドメインモデル
   * @return レシピのエンティティ
   */
  public static RecipeEntity toEntity(Recipe recipe) {
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(LOCAL_DATE_TIME_FORMAT);
    return RecipeEntity.builder()
                       .title(recipe.getTitle())
                       .makingTime(recipe.getMakingTime())
                       .serves(recipe.getServes())
                       .ingredients(recipe.getIngredients())
                       .cost(recipe.getCost())
                       .createdAt(Timestamp.valueOf(LocalDateTime.now().format(dateTimeFormatter)))
                       .updatedAt(Timestamp.valueOf(LocalDateTime.now().format(dateTimeFormatter)))
                       .build();
  }


  /**
   * レシピのエンティティからレシピを表すドメインモデルに変換します.
   *
   * @param recipeEntity レシピのエンティティ
   * @param message レスポンスの際のメッセージ
   * @return レシピを表すドメインモデル
   */
  public static Recipe fromEntity(RecipeEntity recipeEntity, String message) {
    return Recipe.builder()
                 .id(recipeEntity.getId())
                 .message(message)
                 .title(recipeEntity.getTitle())
                 .makingTime(recipeEntity.getMakingTime())
                 .serves(recipeEntity.getServes())
                 .ingredients(recipeEntity.getIngredients())
                 .cost(recipeEntity.getCost())
                 .build();
  }
}
