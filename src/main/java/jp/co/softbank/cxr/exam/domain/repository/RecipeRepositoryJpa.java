package jp.co.softbank.cxr.exam.domain.repository;

import jp.co.softbank.cxr.exam.domain.model.Recipe;
import jp.co.softbank.cxr.exam.integration.entity.RecipeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface RecipeRepositoryJpa extends JpaRepository<RecipeEntity,Integer> {

//  /**
//   * レシピを登録します.
//   *
//   * @param recipeEntity 新規登録するレシピ
//   * @return 新規登録されたレシピのエンティティ
//   */
//  RecipeEntity create(RecipeEntity recipeEntity);
//
//  /**
//   * ID を指定してレシピのエンティティを取得します.
//   *
//   * @param id レシピの ID
//   * @return 取得したレシピのエンティティ
//   */
//  RecipeEntity findById(Integer id);
//
//  /**
//   * 複数のレシピのエンティティを取得します.
//   *
//   * @return 取得したレシピのエンティティのリスト
//   */
//  List<RecipeEntity> findAll();
//
//  /**
//   * レシピを更新します.
//   *
//   * @param recipeEntity 更新するレシピ
//   * @return 更新されたレシピのエンティティ
//   */
//  RecipeEntity update(RecipeEntity recipeEntity);
//
//  /**
//   * 指定した ID のレシピを削除します.
//   *
//   * @param id 削除するレシピの ID
//   * @return 削除されたレシピのエンティティ
//   */
//  RecipeEntity delete(Integer id);
}
