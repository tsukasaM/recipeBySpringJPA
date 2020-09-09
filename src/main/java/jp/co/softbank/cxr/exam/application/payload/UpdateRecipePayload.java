package jp.co.softbank.cxr.exam.application.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import jp.co.softbank.cxr.exam.domain.model.Recipe;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 更新したレシピを表現するペイロードです.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateRecipePayload {

  @JsonProperty("title")
  private String title;

  @JsonProperty("making_time")
  private String makingTime;

  @JsonProperty("serves")
  private String serves;

  @JsonProperty("ingredients")
  private String ingredients;

  @JsonProperty("cost")
  private String cost;

  /**
   * レシピのドメインモデルから UpdateRecipePayload のインスタンスを生成します.
   *
   * @param recipe UpdateRecipePayload に変換したいレシピのドメインモデル
   * @return ドメインモデルから変換された UpdateRecipePayload のインスタンス
   */
  public static UpdateRecipePayload of(Recipe recipe) {
    return UpdateRecipePayload.builder()
                              .title(recipe.getTitle())
                              .makingTime(recipe.getMakingTime())
                              .serves(recipe.getServes())
                              .ingredients(recipe.getIngredients())
                              .cost(Integer.toString(recipe.getCost()))
                              .build();
  }

}
