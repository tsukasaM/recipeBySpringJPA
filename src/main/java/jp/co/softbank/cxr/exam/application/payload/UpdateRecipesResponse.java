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
 * 更新したレシピのレスポンスを表すクラスです.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateRecipesResponse {

  @JsonProperty("message")
  private String message;

  @JsonProperty("recipe")
  private List<UpdateRecipePayload> recipes;

  /**
   * ドメインモデルから UpdateRecipesResponse のインスタンスを生成します.
   *
   * @param recipe UpdateRecipesResponse に変換したいレシピのドメインモデル
   * @return ドメインモデルから変換された UpdateRecipesResponse のインスタンス
   */
  public static UpdateRecipesResponse of(Recipe recipe) {
    return UpdateRecipesResponse.builder()
                                .message(recipe.getMessage())
                                .recipes(Collections.singletonList(UpdateRecipePayload.of(recipe)))
                                .build();
  }

}
