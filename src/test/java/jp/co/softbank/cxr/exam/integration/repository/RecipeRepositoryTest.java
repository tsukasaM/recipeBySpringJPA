package jp.co.softbank.cxr.exam.integration.repository;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.Operations;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.destination.Destination;
import com.ninja_squad.dbsetup.operation.Operation;
import jp.co.softbank.cxr.exam.common.exception.ApplicationException;
import jp.co.softbank.cxr.exam.domain.repository.RecipeRepository;
import jp.co.softbank.cxr.exam.integration.entity.RecipeEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.ninja_squad.dbsetup.Operations.deleteAllFrom;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("RecipeRepository のテスト")
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class RecipeRepositoryTest {

  @Autowired
  private RecipeRepository target;

  @Autowired
  private DataSource dataSource;

  private static final Operation DELETE_ALL = deleteAllFrom("recipes");
  private final Operation INSERT_RECIPE_DATA = Operations.insertInto("recipes").columns("id",
                                                                                  "title",
                                                                                  "making_time",
                                                                                  "serves",
                                                                                  "ingredients",
                                                                                  "cost",
                                                                                  "created_at",
                                                                                  "updated_at")
                                                                                  .values( 1,
                                                                                      "チキンカレー",
                                                                                      "45分",
                                                                                      "4人",
                                                                                      "玉ねぎ,肉,スパイス",
                                                                                      1000,
                                                                                      Timestamp.valueOf(LocalDateTime.parse("2016-01-10 12:10:12", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))),
                                                                                      Timestamp.valueOf(LocalDateTime.parse("2016-01-10 12:10:12", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))))
                                                                                  .values(2,
                                                                                      "オムライス",
                                                                                      "30分",
                                                                                      "2人",
                                                                                      "玉ねぎ,卵,スパイス,醤",
                                                                                      700,
                                                                                      Timestamp.valueOf(LocalDateTime.parse("2016-01-11 13:10:12", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))),
                                                                                      Timestamp.valueOf(LocalDateTime.parse("2016-01-11 13:10:12", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))))
                                                                                  .build();

  private void dbSetup(Operation operation) {
    Destination destination = new DataSourceDestination(dataSource);
    DbSetup dbSetup = new DbSetup(destination, operation);
    dbSetup.launch();
  }

  @BeforeEach
  void setUp() {
    dbSetup(Operations.sequenceOf(
        DELETE_ALL,
        INSERT_RECIPE_DATA
    ));
  }

  @AfterEach
  void tearDown() {
    dbSetup(Operations.sequenceOf(
        DELETE_ALL
    ));
  }

  @Test
  void test_レシピが適切に保存できる事() {

    //setup
   DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
   Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now().format(dateTimeFormatter));

   RecipeEntity recipeEntity = RecipeEntity.builder()
                                         .title("トマトスープ")
                                         .makingTime("15分")
                                         .serves("5人")
                                         .ingredients("玉ねぎ, トマト, スパイス, 水")
                                         .cost(450)
                                         .createdAt(timestamp)
                                         .updatedAt(timestamp)
                                         .build();

    RecipeEntity expected = RecipeEntity.builder()
                                        .id(3)
                                        .title("トマトスープ")
                                        .makingTime("15分")
                                        .serves("5人")
                                        .ingredients("玉ねぎ, トマト, スパイス, 水")
                                        .cost(450)
                                        .createdAt(timestamp)
                                        .updatedAt(timestamp)
                                        .build();

    //execute
    RecipeEntity actual = target.create(recipeEntity);


    //assert
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void test_レシピがIDを指定して適切に1件取得できる事() {

    //setup
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    Timestamp timestamp = Timestamp.valueOf(LocalDateTime.parse("2016-01-10 12:10:12", dateTimeFormatter));

    RecipeEntity expected = RecipeEntity.builder()
                                        .id(1)
                                        .title("チキンカレー")
                                        .makingTime("45分")
                                        .serves("4人")
                                        .ingredients("玉ねぎ,肉,スパイス")
                                        .cost(1000)
                                        .createdAt(timestamp)
                                        .updatedAt(timestamp)
                                        .build();

    //execute
    RecipeEntity actual = target.findById(1);

    //assert
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void test_レシピを全件取得できる事() {

    //setup
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    Timestamp day10th = Timestamp.valueOf(LocalDateTime.parse("2016-01-10 12:10:12", dateTimeFormatter));
    Timestamp day11th = Timestamp.valueOf(LocalDateTime.parse("2016-01-11 13:10:12", dateTimeFormatter));

    List<RecipeEntity> expected = Arrays.asList(RecipeEntity.builder()
                                                            .id(1)
                                                            .title("チキンカレー")
                                                            .makingTime("45分")
                                                            .serves("4人")
                                                            .ingredients("玉ねぎ,肉,スパイス")
                                                            .cost(1000)
                                                            .createdAt(day10th)
                                                            .updatedAt(day10th)
                                                            .build(),
                                                RecipeEntity.builder()
                                                            .id(2)
                                                            .title("オムライス")
                                                            .makingTime("30分")
                                                            .serves("2人")
                                                            .ingredients("玉ねぎ,卵,スパイス,醤")
                                                            .cost(700)
                                                            .createdAt(day11th)
                                                            .updatedAt(day11th)
                                                            .build());

    //execute
    List<RecipeEntity> actual = target.findAll();

    //assert
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void test_レシピが適切に更新できる事() {

    //setup
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    Timestamp timestamp = Timestamp.valueOf(LocalDateTime.parse("2016-01-10 12:10:12", dateTimeFormatter));
    Timestamp updatedAt = Timestamp.valueOf(LocalDateTime.parse("2020-01-10 12:10:12", dateTimeFormatter));

    RecipeEntity recipeEntity = RecipeEntity.builder()
                                            .id(1)
                                            .title("トマトスープ")
                                            .makingTime("15分")
                                            .updatedAt(updatedAt)
                                            .build();

    RecipeEntity expected = RecipeEntity.builder()
                                        .id(1)
                                        .title("トマトスープ")
                                        .makingTime("15分")
                                        .serves("4人")
                                        .ingredients("玉ねぎ,肉,スパイス")
                                        .cost(1000)
                                        .createdAt(timestamp)
                                        .updatedAt(updatedAt)
                                        .build();

    //execute
    RecipeEntity actual = target.update(recipeEntity);

    //assert
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void test_更新しようとしたレシピが存在しなかった場合() {

    //setup
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    Timestamp updatedAt = Timestamp.valueOf(LocalDateTime.parse("2020-01-10 12:10:12", dateTimeFormatter));

    RecipeEntity recipeEntity = RecipeEntity.builder()
                                            .id(199)
                                            .title("トマトスープ")
                                            .makingTime("15分")
                                            .updatedAt(updatedAt)
                                            .build();

    //execute & assert
    ApplicationException actual = assertThrows(ApplicationException.class, () -> target.update(recipeEntity));

    assertThat(actual.getMessage()).isEqualTo("No Recipe found");
  }

  @Test
  void test_レシピが指定したIDで適切に削除できる事() {

    //setup
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    Timestamp day10th = Timestamp.valueOf(LocalDateTime.parse("2016-01-10 12:10:12", dateTimeFormatter));

    RecipeEntity expected = RecipeEntity.builder()
                                        .id(1)
                                        .title("チキンカレー")
                                        .makingTime("45分")
                                        .serves("4人")
                                        .ingredients("玉ねぎ,肉,スパイス")
                                        .cost(1000)
                                        .createdAt(day10th)
                                        .updatedAt(day10th)
                                        .build();

    Timestamp day11th = Timestamp.valueOf(LocalDateTime.parse("2016-01-11 13:10:12", dateTimeFormatter));
    List<RecipeEntity> expectedDataBase = Collections.singletonList(RecipeEntity.builder()
                                                                                .id(2)
                                                                                .title("オムライス")
                                                                                .makingTime("30分")
                                                                                .serves("2人")
                                                                                .ingredients("玉ねぎ,卵,スパイス,醤")
                                                                                .cost(700)
                                                                                .createdAt(day11th)
                                                                                .updatedAt(day11th)
                                                                                .build());

    //execute
    RecipeEntity actual = target.delete(1);

    //assert
    List<RecipeEntity> actualDataBase = target.findAll();
    assertThat(actualDataBase).containsExactlyInAnyOrderElementsOf(expectedDataBase);
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void test_削除しようとしたレシピが存在しなかった場合() {
    //execute & assert
    ApplicationException actual = assertThrows(ApplicationException.class, () -> target.delete(9999));

    assertThat(actual.getMessage()).isEqualTo("No Recipe found");
  }
}
