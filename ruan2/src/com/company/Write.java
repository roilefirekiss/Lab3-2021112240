package com.company;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

/**
 * .
 */
public class Write {

  /**
   *打印输出.

   * @param txt 文本
   * @param filePath 路径
   */
  public static void printout(String txt, String filePath) {
    BufferedWriter writer = null;
    try {
      writer = new BufferedWriter(
              new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8));
      writer.write(txt);
    } catch (IOException e) {
      System.err.println("文件写入异常" + e.getMessage());
    } finally {
      try {
        if (writer != null) {
          writer.close();
        }
      } catch (IOException e) {
        System.err.println("文件未打开: " + e.getMessage());
      }
    }
  }
}
