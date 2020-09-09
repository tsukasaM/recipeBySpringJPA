package jp.co.softbank.cxr.exam.common;

/**
 * レシピAPI におけるメッセージの定数クラス.
 */
public final class Messages {

  private Messages() {
    // Do Nothing.
  }

  /** レシピ登録に成功した際のメッセージ. */
  public static final String CREATE_SUCCESS = "Recipe successfully created!";

  /** レシピ登録に失敗した際のメッセージ. */
  public static final String CREATE_FAIL = "Recipe creation failed!";

  /** レシピ登録に必要な項目を表すメッセージ. */
  public static final String REQUIRED = "title, making_time, serves, ingredients, cost";

  /** レシピ1件取得に成功した際のメッセージ. */
  public static final String GET_BY_ID_SUCCESS = "Recipe details by id";

  /** レシピ取得に失敗した際のメッセージ. */
  public static final String GET_FAIL = "No Recipe found";

  /** レシピ更新に成功した際のメッセージ. */
  public static final String UPDATE_SUCCESS = "Recipe successfully updated!";

}
