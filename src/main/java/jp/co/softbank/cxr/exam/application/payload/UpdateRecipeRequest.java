package jp.co.softbank.cxr.exam.application.payload;

import static java.util.Objects.nonNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import jp.co.softbank.cxr.exam.domain.model.Recipe;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



/**
 * 更新するレシピのリクエストを表すクラスです.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateRecipeRequest {

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
   * リクエストからドメインモデルに変換します.
   *
   * @return 変換後のドメインモデル
   */
  public Recipe toModel() {
    Integer integerCost = null;
    if (nonNull(cost)) {
      integerCost = Integer.parseInt(cost);
    }

    return Recipe.builder()
                 .title(title)
                 .makingTime(makingTime)
                 .serves(serves)
                 .ingredients(ingredients)
                 .cost(integerCost)
                 .build();
  }
}
