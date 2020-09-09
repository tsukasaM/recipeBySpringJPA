package jp.co.softbank.cxr.exam.domain.service;

import static java.util.Objects.isNull;
import static jp.co.softbank.cxr.exam.common.Messages.CREATE_SUCCESS;
import static jp.co.softbank.cxr.exam.common.Messages.GET_BY_ID_SUCCESS;
import static jp.co.softbank.cxr.exam.common.Messages.GET_FAIL;
import static jp.co.softbank.cxr.exam.common.Messages.UPDATE_SUCCESS;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import jp.co.softbank.cxr.exam.common.exception.ApplicationException;
import jp.co.softbank.cxr.exam.domain.mapper.RecipeEntityMapper;
import jp.co.softbank.cxr.exam.domain.model.Recipe;
import jp.co.softbank.cxr.exam.domain.repository.RecipeRepository;
import jp.co.softbank.cxr.exam.integration.entity.RecipeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * {@link RecipeService} の実装クラスです.
 */
@Service
public class RecipeServiceImpl implements RecipeService {

  @Autowired
  RecipeRepository recipeRepository;

  private static final String LOCAL_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

  /**
   * {@inheritDoc}
   */
  @Override
  public Recipe createRecipe(Recipe recipe) {
    return RecipeEntityMapper.fromEntity(
        recipeRepository.create(RecipeEntityMapper.toEntity(recipe)), CREATE_SUCCESS);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Recipe getRecipe(Integer id) {

    RecipeEntity recipeEntity = recipeRepository.findById(id);

    if (isNull(recipeEntity)) {
      throw new ApplicationException(GET_FAIL);
    }

    return RecipeEntityMapper.fromEntity(recipeEntity, GET_BY_ID_SUCCESS);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Recipe> getRecipeList() {

    List<Recipe> recipes = new ArrayList<>();

    for (RecipeEntity recipeEntity : recipeRepository.findAll()) {
      recipes.add(RecipeEntityMapper.fromEntity(recipeEntity,null));
    }

    return recipes;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Recipe updateRecipe(Recipe recipe, Integer id) {

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(LOCAL_DATE_TIME_FORMAT);

    RecipeEntity recipeEntity = RecipeEntity.builder()
                                            .id(id)
                                            .title(recipe.getTitle())
                                            .makingTime(recipe.getMakingTime())
                                            .serves(recipe.getServes())
                                            .ingredients(recipe.getIngredients())
                                            .cost(recipe.getCost())
                                            .updatedAt(
                                                Timestamp.valueOf(
                                                    LocalDateTime.now().format(dateTimeFormatter)))
                                            .build();

    return RecipeEntityMapper.fromEntity(recipeRepository.update(recipeEntity), UPDATE_SUCCESS);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Recipe deleteRecipe(Integer id) {
    return RecipeEntityMapper.fromEntity(recipeRepository.delete(id), null);
  }
}
