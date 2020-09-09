package jp.co.softbank.cxr.exam.application.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;

import jp.co.softbank.cxr.exam.domain.model.Recipe;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 登録するレシピのリクエストを表すクラスです.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateRecipeRequest {

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
   * リクエストからドメインモデルに変換します.
   *
   * @return 変換後のドメインモデル
   */
  public Recipe toModel() {
    return Recipe.builder()
                 .title(title)
                 .makingTime(makingTime)
                 .serves(serves)
                 .ingredients(ingredients)
                 .cost(Integer.parseInt(cost))
                 .build();
  }
}
