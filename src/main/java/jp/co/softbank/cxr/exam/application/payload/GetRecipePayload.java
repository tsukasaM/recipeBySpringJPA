package jp.co.softbank.cxr.exam.application.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import jp.co.softbank.cxr.exam.domain.model.Recipe;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 取得したレシピを表現するペイロードです.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetRecipePayload {

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
   * レシピのドメインモデルから GetRecipePayload のインスタンスを生成します.
   *
   * @param recipe GetRecipePayload に変換したいレシピのドメインモデル
   * @return ドメインモデルから変換された GetRecipePayload のインスタンス
   */
  public static GetRecipePayload of(Recipe recipe) {
    return GetRecipePayload.builder()
                           .title(recipe.getTitle())
                           .makingTime(recipe.getMakingTime())
                           .serves(recipe.getServes())
                           .ingredients(recipe.getIngredients())
                           .cost(Integer.toString(recipe.getCost()))
                           .build();
  }

}
