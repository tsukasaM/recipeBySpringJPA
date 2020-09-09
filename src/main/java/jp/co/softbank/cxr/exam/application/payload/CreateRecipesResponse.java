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
 * 登録したレシピのリストのレスポンスを表すクラスです.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateRecipesResponse {

  @JsonProperty("message")
  private String message;

  @JsonProperty("recipe")
  private List<CreateRecipePayload> recipes;

  /**
   * ドメインモデルから CreateRecipesResponse のインスタンスを生成します.
   *
   * @param recipe CreateRecipesResponse に変換したいレシピのドメインモデル
   * @return ドメインモデルから変換された CreateRecipesResponse のインスタンス
   */
  public static CreateRecipesResponse of(Recipe recipe) {
    return CreateRecipesResponse.builder()
                                .message(recipe.getMessage())
                                .recipes(Collections.singletonList(CreateRecipePayload.of(recipe)))
                                .build();
  }

}
