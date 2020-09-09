package jp.co.softbank.cxr.exam.application.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * エラーが発生したときの詳細情報を表すクラスです.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RestErrorResponse {
  @JsonProperty("message")
  private String message;

  @JsonProperty("required")
  private String required;
}
