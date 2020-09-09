package jp.co.softbank.cxr.exam.application.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;

import jp.co.softbank.cxr.exam.domain.model.Recipe;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * レシピの全権取得で使用するペイロードです.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetAllRecipePayload {

  @NotNull
  @JsonProperty("id")
  private Integer id;

  @NotNull
  @JsonProperty("title")
  private String title;

  @NotNull
  @JsonProperty("making_time")
  private String makingTime;

  @NotNull
  @JsonProperty("serves")
  private String serves;

  @NotNull
  @JsonProperty("ingredients")
  private String ingredients;

  @NotNull
  @JsonProperty("cost")
  private String cost;

  /**
   * レシピのドメインモデルから GetAllRecipesPayload のインスタンスを生成します.
   *
   * @param recipe GetAllRecipesPayload に変換したいレシピのドメインモデル
   * @return ドメインモデルから変換された GetAllRecipesPayload のインスタンス
   */
  public static GetAllRecipePayload of(Recipe recipe) {
    return GetAllRecipePayload.builder()
                              .id(recipe.getId())
                              .title(recipe.getTitle())
                              .makingTime(recipe.getMakingTime())
                              .serves(recipe.getServes())
                              .ingredients(recipe.getIngredients())
                              .cost(Integer.toString(recipe.getCost()))
                              .build();
  }
}
