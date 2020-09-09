package jp.co.softbank.cxr.exam;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

public final class TestUtils {
  
  private TestUtils() {
 // Do Nothing.
  }
  
  public static String readMessageFromFile(String path) throws IOException {
    String result = null;

    try (FileInputStream input = new FileInputStream("src/test/resources/" + path)) {
      result = IOUtils.toString(input, "UTF-8");
    }

    return result;
  }
  
}
