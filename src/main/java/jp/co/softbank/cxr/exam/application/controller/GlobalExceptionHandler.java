package jp.co.softbank.cxr.exam.application.controller;

import jp.co.softbank.cxr.exam.application.payload.ApplicationErrorResponse;
import jp.co.softbank.cxr.exam.application.payload.RestErrorResponse;
import jp.co.softbank.cxr.exam.common.exception.ApplicationException;
import jp.co.softbank.cxr.exam.common.exception.RestErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 共通して例外のハンドリングを行うクラスです.
 */
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

  /**
   * 例外をハンドリングして Json 形式のエラーを返却する処理を実行します.
   *
   * @param e 発生した例外
   * @return エラーレスポンス
   */
  @ExceptionHandler(RestErrorException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  private ResponseEntity<RestErrorResponse> handleException(RestErrorException e) {
    return new ResponseEntity<>(new RestErrorResponse(e.getMessage(), e.getRequired()),
                                  HttpStatus.BAD_REQUEST);
  }

  /**
   * ビジネス例外をハンドリングして Json 形式のエラーを返却する処理を実行します.
   *
   * @param e 発生した例外
   * @return エラーレスポンス
   */
  @ExceptionHandler(ApplicationException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  private ResponseEntity<ApplicationErrorResponse> handleException(ApplicationException e) {
    return new ResponseEntity<>(new ApplicationErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
  }
}
