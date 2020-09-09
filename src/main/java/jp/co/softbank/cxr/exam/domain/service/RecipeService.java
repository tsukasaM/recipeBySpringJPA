package jp.co.softbank.cxr.exam.domain.service;

import java.util.List;
import jp.co.softbank.cxr.exam.domain.model.Recipe;

/**
 * レシピに対して操作を行うサービスクラスです.
 */
public interface RecipeService {


  /**
   * レシピを登録します.
   *
   * @param recipe 登録したい書籍情報
   * @return 登録されたレシピ
   */
  Recipe createRecipe(Recipe recipe);

  /**
   * 指定した id でレシピを一件取得します.
   *
   * @param id 取得したレシピの id
   * @return 取得したレシピ
   */
  Recipe getRecipe(Integer id);

  /**
   * レシピを全件取得します.
   *
   * @return 取得したレシピのリスト
   */
  List<Recipe> getRecipeList();

  /**
   * 指定した id のレシピを更新します.
   *
   * @param id 取得したレシピの id
   * @return 取得したレシピ
   */
  Recipe updateRecipe(Recipe recipe, Integer id);

  /**
   * 指定した id のレシピを削除します.
   *
   * @param id 削除したいレシピの id
   * @return 削除されたレシピ
   */
  Recipe deleteRecipe(Integer id);
}
