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
public class ApplicationException extends RuntimeException {

  private final String message;

  /**
   * 指定されたエラー詳細を使用して {@link ApplicationException} を構築します.
   * @param message エラーメッセージ
   */
  public ApplicationException(String message) {
    super();
    this.message = message;
  }
}
