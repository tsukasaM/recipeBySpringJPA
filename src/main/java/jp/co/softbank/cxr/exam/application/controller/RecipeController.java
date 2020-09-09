package jp.co.softbank.cxr.exam.application.controller;

import static jp.co.softbank.cxr.exam.common.Messages.CREATE_FAIL;
import static jp.co.softbank.cxr.exam.common.Messages.REQUIRED;

import javax.validation.Valid;

import jp.co.softbank.cxr.exam.application.payload.CreateRecipeRequest;
import jp.co.softbank.cxr.exam.application.payload.CreateRecipesResponse;
import jp.co.softbank.cxr.exam.application.payload.GetAllRecipesResponse;
import jp.co.softbank.cxr.exam.application.payload.GetRecipesResponse;
import jp.co.softbank.cxr.exam.application.payload.UpdateRecipeRequest;
import jp.co.softbank.cxr.exam.application.payload.UpdateRecipesResponse;
import jp.co.softbank.cxr.exam.common.exception.RestErrorException;
import jp.co.softbank.cxr.exam.domain.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class RecipeController {

  private final RecipeService recipeService;

  /**
   * レシピ API の entry を提供するエンドポイント.
   */
  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public void entry() {
  }

  /**
   * 新規にレシピを登録するためのエンドポイントを提供します.
   *
   * @param request 作成するレシピ
   * @return 作成したレシピとメッセージ
   */
  @PostMapping(value = "recipes",
                consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public CreateRecipesResponse createRecipe(@RequestBody @Valid CreateRecipeRequest request,
                                            BindingResult bindingResult) {

    if (bindingResult.hasErrors()) {
      throw new RestErrorException(CREATE_FAIL, REQUIRED);
    }

    return CreateRecipesResponse.of(recipeService.createRecipe(request.toModel()));
  }

  /**
   * 指定した id でレシピの1件検索をするエンドポイントを提供します.
   *
   * @param id 取得するレシピの id
   * @return 取得したレシピとメッセージ
   */
  @GetMapping(value = "recipes/{id}")
  public GetRecipesResponse getRecipe(@PathVariable("id") Integer id) {
    return GetRecipesResponse.of(recipeService.getRecipe(id));
  }

  /**
   * レシピの全件検索をするエンドポイントを提供します.
   *
   * @return 取得したレシピとメッセージ
   */
  @GetMapping(value = "recipes")
  public GetAllRecipesResponse getAllRecipes() {
    return GetAllRecipesResponse.of(recipeService.getRecipeList());
  }

  /**
   * 指定した id のレシピを更新するエンドポイントを提供します.
   *
   * @param id 更新するレシピの id
   * @param updateRecipeRequest 更新するレシピ
   * @return 更新したレシピとメッセージ
   */
  @PatchMapping(value = "recipes/{id}",
      consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public UpdateRecipesResponse updateRecipe(@PathVariable("id") Integer id,
                                            @RequestBody UpdateRecipeRequest updateRecipeRequest) {
    return UpdateRecipesResponse.of(recipeService.updateRecipe(updateRecipeRequest.toModel(), id));
  }

  /**
   * 指定した id のレシピを削除するエンドポイントを提供します.
   *
   * @param id 削除するレシピの id
   */
  @DeleteMapping(value = "recipes/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteRecipe(@PathVariable("id") Integer id) {
    recipeService.deleteRecipe(id);
  }

}
