package jp.co.softbank.cxr.exam.common.exception;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.NonFinal;

/**
 * レシピ API においてアプリケーション例外を表現するクラスです.
 */
@Value
@NonFinal
@Builder
public class RestErrorException extends RuntimeException {

  private final String message;

  private final String required;

  /**
   * 指定されたエラー詳細を使用して {@link RestErrorException} を構築します.
   * @param message エラーメッセージ
   * @param required リクエスト必要項目
   */
  public RestErrorException(String message, String required) {
    super();
    this.message = message;
    this.required = required;
  }
}
