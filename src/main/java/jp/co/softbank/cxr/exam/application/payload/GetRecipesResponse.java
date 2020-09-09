package jp.co.softbank.cxr.exam.application.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collections;
import java.util.List;
import jp.co.softbank.cxr.exam.domain.model.Recipe;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 取得したレシピのレスポンスを表すクラスです.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetRecipesResponse {

  @JsonProperty("message")
  private String message;

  @JsonProperty("recipe")
  private List<GetRecipePayload> recipes;

  /**
   * ドメインモデルから GetRecipesResponse のインスタンスを生成します.
   *
   * @param recipe GetRecipesResponse に変換したいレシピのドメインモデル
   * @return ドメインモデルから変換された GetRecipesResponse のインスタンス
   */
  public static GetRecipesResponse of(Recipe recipe) {
    return GetRecipesResponse.builder()
                             .message(recipe.getMessage())
                             .recipes(Collections.singletonList(GetRecipePayload.of(recipe)))
                             .build();
  }

}
