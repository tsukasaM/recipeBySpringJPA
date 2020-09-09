package jp.co.softbank.cxr.exam.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * レシピのドメインモデルを表します.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Recipe {

  private String message;

  private Integer id;

  private String title;

  private String makingTime;

  private String serves;

  private String ingredients;

  private Integer cost;

}
