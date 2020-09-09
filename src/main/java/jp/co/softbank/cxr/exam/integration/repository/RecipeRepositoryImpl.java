package jp.co.softbank.cxr.exam.integration.repository;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static jp.co.softbank.cxr.exam.common.Messages.GET_FAIL;

import java.util.List;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import jp.co.softbank.cxr.exam.common.exception.ApplicationException;
import jp.co.softbank.cxr.exam.domain.repository.RecipeRepository;
import jp.co.softbank.cxr.exam.integration.entity.RecipeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;



/**
 * {@link RecipeRepository} の実装クラスです.
 */
@Repository
public class RecipeRepositoryImpl implements RecipeRepository {

  private static final String SELECT_ALL = "FROM RecipeEntity";

  @Autowired
  EntityManager entityManager;

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional
  public RecipeEntity create(RecipeEntity recipeEntity) {
    entityManager.persist(recipeEntity);
    return recipeEntity;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RecipeEntity findById(Integer id) {
    return entityManager.find(RecipeEntity.class, id);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<RecipeEntity> findAll() {
    return entityManager.createQuery(SELECT_ALL, RecipeEntity.class).getResultList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional
  public RecipeEntity update(RecipeEntity recipeEntity) {
    RecipeEntity existingEntity = entityManager.find(RecipeEntity.class, recipeEntity.getId());

    if (isNull(existingEntity)) {
      throw new ApplicationException(GET_FAIL);
    }

    RecipeEntity mergedEntity = merge(recipeEntity, existingEntity);

    mergedEntity.setUpdatedAt(recipeEntity.getUpdatedAt());

    return entityManager.merge(mergedEntity);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional
  public RecipeEntity delete(Integer id) {
    RecipeEntity recipeEntity = findById(id);

    if (isNull(recipeEntity)) {
      throw new ApplicationException(GET_FAIL);
    }

    entityManager.remove(recipeEntity);

    return recipeEntity;
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
