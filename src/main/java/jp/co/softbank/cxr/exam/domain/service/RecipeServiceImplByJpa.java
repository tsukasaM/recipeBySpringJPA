package jp.co.softbank.cxr.exam.domain.service;

import jp.co.softbank.cxr.exam.common.exception.ApplicationException;
import jp.co.softbank.cxr.exam.domain.mapper.RecipeEntityMapper;
import jp.co.softbank.cxr.exam.domain.model.Recipe;
import jp.co.softbank.cxr.exam.domain.repository.RecipeRepository;
import jp.co.softbank.cxr.exam.domain.repository.RecipeRepositoryJpa;
import jp.co.softbank.cxr.exam.integration.entity.RecipeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static jp.co.softbank.cxr.exam.common.Messages.CREATE_SUCCESS;
import static jp.co.softbank.cxr.exam.common.Messages.GET_BY_ID_SUCCESS;
import static jp.co.softbank.cxr.exam.common.Messages.GET_FAIL;
import static jp.co.softbank.cxr.exam.common.Messages.UPDATE_SUCCESS;


/**
 * {@link RecipeService} の実装クラスです.
 */
@Service
public class RecipeServiceImplByJpa implements RecipeService {

  @Autowired
  RecipeRepositoryJpa recipeRepository;

  private static final String LOCAL_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

  /**
   * {@inheritDoc}
   */
  @Override
  public Recipe createRecipe(Recipe recipe) {
    return RecipeEntityMapper.fromEntity(
        recipeRepository.save(RecipeEntityMapper.toEntity(recipe)), CREATE_SUCCESS);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Recipe getRecipe(Integer id) {

    Optional<RecipeEntity> recipeEntity = recipeRepository.findById(id);

    if (!recipeEntity.isPresent()) {
      throw new ApplicationException(GET_FAIL);
    }

    return RecipeEntityMapper.fromEntity(recipeEntity.get(), GET_BY_ID_SUCCESS);
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

    Optional<RecipeEntity> existingEntity = recipeRepository.findById(id);

    if (!existingEntity.isPresent()) {
      throw new ApplicationException(GET_FAIL);
    }

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(LOCAL_DATE_TIME_FORMAT);

    RecipeEntity updateEntity = RecipeEntity.builder()
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

    return RecipeEntityMapper.fromEntity(recipeRepository.save(merge(updateEntity, existingEntity.get())), UPDATE_SUCCESS);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Recipe deleteRecipe(Integer id) {

    Optional<RecipeEntity> recipeEntity = recipeRepository.findById(id);

    if (!recipeEntity.isPresent()) {
      throw new ApplicationException(GET_FAIL);
    }
    recipeRepository.deleteById(id);

    return RecipeEntityMapper.fromEntity(recipeEntity.get(), null);
  }

  private RecipeEntity merge(RecipeEntity updateEntity, RecipeEntity existingEntity) {
    RecipeEntity result = existingEntity;

    if (nonNull(updateEntity.getTitle())) {
      result.setTitle(updateEntity.getTitle());
    }

    if (nonNull(updateEntity.getMakingTime())) {
      result.setMakingTime(updateEntity.getMakingTime());
    }

    if (nonNull(updateEntity.getServes())) {
      result.setServes(updateEntity.getServes());
    }

    if (nonNull(updateEntity.getIngredients())) {
      result.setIngredients(updateEntity.getIngredients());
    }

    if (nonNull(updateEntity.getCost())) {
      result.setCost(updateEntity.getCost());
    }

    return result;
  }
}
