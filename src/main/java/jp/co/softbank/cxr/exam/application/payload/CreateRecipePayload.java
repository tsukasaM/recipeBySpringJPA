package jp.co.softbank.cxr.exam.application.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;
import jp.co.softbank.cxr.exam.domain.model.Recipe;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 登録したレシピを表現するペイロードです.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateRecipePayload {

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
   * レシピのドメインモデルから CreateRecipePayload のインスタンスを生成します.
   *
   * @param recipe CreateRecipePayload に変換したいレシピのドメインモデル
   * @return ドメインモデルから変換された CreateRecipePayload のインスタンス
   */
  public static CreateRecipePayload of(Recipe recipe) {
    return CreateRecipePayload.builder()
                              .title(recipe.getTitle())
                              .makingTime(recipe.getMakingTime())
                              .serves(recipe.getServes())
                              .ingredients(recipe.getIngredients())
                              .cost(Integer.toString(recipe.getCost()))
                              .build();
  }

}
