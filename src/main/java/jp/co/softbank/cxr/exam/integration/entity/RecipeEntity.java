package jp.co.softbank.cxr.exam.integration.entity;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



/**
 * recipes テーブルのエンティティクラスです.
 */
@Builder
@Data
@Entity
@Table(name = "recipes")
@NoArgsConstructor
@AllArgsConstructor
public class RecipeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "title", length = 100)
  private String title;

  @Column(name = "making_time", length = 100)
  private String makingTime;

  @Column(name = "serves", length = 100)
  private String serves;

  @Column(name = "ingredients", length = 300)
  private String ingredients;

  @Column(name = "cost")
  private Integer cost;

  @Column(name = "created_at")
  private Timestamp createdAt;

  @Column(name = "updated_at")
  private Timestamp updatedAt;

}
