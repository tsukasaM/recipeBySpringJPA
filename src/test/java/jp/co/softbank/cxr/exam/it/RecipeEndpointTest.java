package jp.co.softbank.cxr.exam.it;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.Operations;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.destination.Destination;
import com.ninja_squad.dbsetup.operation.Operation;
import jp.co.softbank.cxr.exam.application.payload.CreateRecipePayload;
import jp.co.softbank.cxr.exam.application.payload.CreateRecipeRequest;
import jp.co.softbank.cxr.exam.application.payload.CreateRecipesResponse;
import jp.co.softbank.cxr.exam.application.payload.GetAllRecipePayload;
import jp.co.softbank.cxr.exam.application.payload.GetAllRecipesResponse;
import jp.co.softbank.cxr.exam.application.payload.GetRecipePayload;
import jp.co.softbank.cxr.exam.application.payload.GetRecipesResponse;
import jp.co.softbank.cxr.exam.application.payload.UpdateRecipePayload;
import jp.co.softbank.cxr.exam.application.payload.UpdateRecipeRequest;
import jp.co.softbank.cxr.exam.application.payload.UpdateRecipesResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.ninja_squad.dbsetup.Operations.deleteAllFrom;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("recipes/ エンドポイントに対するテスト")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RecipeEndpointTest {

  @Autowired
  MockMvc mockMvc;

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
  void test_レシピが適切に保存できる事() throws Exception {

    //setup
    ObjectMapper mapper = new ObjectMapper();

    String requestJson = mapper.writeValueAsString(CreateRecipeRequest.builder()
        .title("トマトスープ")
        .makingTime("15分")
        .serves("5人")
        .ingredients("玉ねぎ, トマト, スパイス, 水")
        .cost("450")
        .build());

    List<CreateRecipePayload> createRecipePayloads = new ArrayList<>();

    CreateRecipePayload createrecipePayload = CreateRecipePayload.builder()
                                                                 .title("トマトスープ")
                                                                 .makingTime("15分")
                                                                 .serves("5人")
                                                                 .ingredients("玉ねぎ, トマト, スパイス, 水")
                                                                 .cost("450")
                                                                 .build();

    createRecipePayloads.add(createrecipePayload);

    String expected = mapper.writeValueAsString(CreateRecipesResponse.builder()
        .message("Recipe successfully created!")
        .recipes(createRecipePayloads)
        .build());

    // execute & assert
    mockMvc.perform(post("/recipes")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(requestJson))
        .andExpect(content().json(expected))
        .andExpect(status().isCreated());
  }

  @Test
  void test_レシピがIDを指定して適切に1件取得できる事() throws Exception {

    //setup
    ObjectMapper mapper = new ObjectMapper();

    List<GetRecipePayload> getRecipePayload = Collections.singletonList(GetRecipePayload.builder()
                                                                                        .title("チキンカレー")
                                                                                        .makingTime("45分")
                                                                                        .serves("4人")
                                                                                        .ingredients("玉ねぎ,肉,スパイス")
                                                                                        .cost("1000")
                                                                                        .build());

    String expected = mapper.writeValueAsString(GetRecipesResponse.builder()
                                                                  .message("Recipe details by id")
                                                                  .recipes(getRecipePayload)
                                                                  .build());

    // execute & assert
    mockMvc.perform(get("/recipes/1"))
        .andExpect(status().isOk())
        .andExpect(content().json(expected));
  }

  @Test
  void test_レシピを全件取得できる事() throws Exception {

    //setup
    ObjectMapper mapper = new ObjectMapper();

    List<GetAllRecipePayload> getAllRecipePayloadList = Arrays.asList(GetAllRecipePayload.builder()
                                                                                         .id(1)
                                                                                         .title("チキンカレー")
                                                                                         .makingTime("45分")
                                                                                         .serves("4人")
                                                                                         .ingredients("玉ねぎ,肉,スパイス")
                                                                                         .cost("1000")
                                                                                         .build(),
                                                                                     GetAllRecipePayload.builder()
                                                                                         .id(2)
                                                                                         .title("オムライス")
                                                                                         .makingTime("30分")
                                                                                         .serves("2人")
                                                                                         .ingredients("玉ねぎ,卵,スパイス,醤")
                                                                                         .cost("700")
                                                                                         .build());
    String expected = mapper.writeValueAsString(GetAllRecipesResponse.builder()
                                                                     .recipes(getAllRecipePayloadList)
                                                                     .build());

    // execute & assert
    mockMvc.perform(get("/recipes"))
        .andExpect(status().isOk())
        .andExpect(content().json(expected));
  }

  @Test
  void test_レシピが適切に更新できる事() throws Exception {
    //setup
    ObjectMapper mapper = new ObjectMapper();

    String requestJson = mapper.writeValueAsString(UpdateRecipeRequest.builder()
                                                                      .title("トマトスープ")
                                                                      .makingTime("15分")
                                                                      .build());

    List<UpdateRecipePayload> updateRecipePayloads = new ArrayList<>();

    UpdateRecipePayload updateRecipePayload = UpdateRecipePayload.builder()
                                                                 .title("トマトスープ")
                                                                 .makingTime("15分")
                                                                 .serves("4人")
                                                                 .ingredients("玉ねぎ,肉,スパイス")
                                                                 .cost("1000")
                                                                 .build();

    updateRecipePayloads.add(updateRecipePayload);

    String expected = mapper.writeValueAsString(UpdateRecipesResponse.builder()
                                                                     .message("Recipe successfully updated!")
                                                                     .recipes(updateRecipePayloads)
                                                                     .build());

    // execute & assert
    mockMvc.perform(patch("/recipes/1")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(requestJson))
        .andExpect(content().json(expected))
        .andExpect(status().isOk());
  }

  @Test
  void test_レシピが指定したIDで適切に削除できる事() throws Exception {
    // execute & assert
    mockMvc.perform(delete("/recipes/1"))
        .andExpect(status().isNoContent());
  }
}
