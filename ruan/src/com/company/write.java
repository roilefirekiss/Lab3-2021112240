package com.company;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class write {
    public static void Printout(String txt, String filePath) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(filePath));
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
