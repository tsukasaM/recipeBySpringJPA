package jp.co.softbank.cxr.exam.application.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

import jp.co.softbank.cxr.exam.domain.model.Recipe;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 取得した全てのレシピのレスポンスを表すクラスです.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetAllRecipesResponse {

  @JsonProperty("recipes")
  private List<GetAllRecipePayload> recipes;

  /**
   * ドメインモデルから GetAllRecipesResponse のインスタンスを生成します.
   *
   * @param recipes GetAllRecipesResponse に変換したいレシピのドメインモデル
   * @return ドメインモデルから変換された GetAllRecipesResponse のインスタンス
   */
  public static GetAllRecipesResponse of(List<Recipe> recipes) {

    List<GetAllRecipePayload> getAllRecipePayloadList = new ArrayList<>();

    for (Recipe recipe : recipes) {
      getAllRecipePayloadList.add(GetAllRecipePayload.of(recipe));
    }
    return GetAllRecipesResponse.builder()
                                .recipes(getAllRecipePayloadList)
                                .build();
  }
}
