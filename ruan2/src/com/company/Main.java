package com.company;

import static com.company.Method.calcShortestPath;
import static com.company.Method.genGraph;
import static com.company.Method.generateNewText;
import static com.company.Method.queryBridgeWords;
import static com.company.Method.randomWalk;
import static com.company.Method.showDirectedGraph;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Scanner;

/**
 * class相关.
 */
public class Main {
  /**
   * main相关.
   */
  public static void main(String[] args) {
    Scanner scanner = new Scanner(new InputStreamReader(System.in, StandardCharsets.UTF_8));
    String filePath;

    while (true) {
      Path basePath = Paths.get("").toAbsolutePath().normalize();  // 设定一个安全的基目录
      while (true) {
        System.out.println("Please enter the file path:");
        filePath = scanner.nextLine();
        try {
          // Normalize the file path to prevent path traversal
          Path inputPath = basePath.resolve(filePath).normalize();

          // Check if the file exists and the path is within the base directory
          if (!inputPath.startsWith(basePath) || !inputPath.toFile().exists()) {
            throw new IllegalArgumentException("Invalid file path or file does not exist.");
          }

          filePath = inputPath.toString();  // 更新文件路径
          break;  // Exit loop if file path is valid
        } catch (Exception e) {
          System.out.println("Invalid file path or file does not exist. Please try again.");
        }
      }
      //功能1，创建图
      Map<String, Map<String, Integer>> graph = genGraph(filePath);

      //可视化
      while (true) {
        System.out.println("Enter a follower number:");
        System.out.println("1. Show Directed Graph");
        System.out.println("2. Query Bridge Words");
        System.out.println("3. New Text on Bridge Words");
        System.out.println("4. Calculate Shortest Path");
        System.out.println("5. Random Walk");
        int co = scanner.nextInt();
        scanner.nextLine();

        switch (co) {
          case 1:
            showDirectedGraph(graph);
            break;
          case 2:
            System.out.println("Enter word1:");
            String word1 = scanner.nextLine();
            System.out.println("Enter word2:");
            String word2 = scanner.nextLine();
            System.out.println(queryBridgeWords(graph, word1, word2));
            break;
          case 3:
            System.out.println("Enter text:");
            String inputText = scanner.nextLine();
            System.out.println(generateNewText(graph, inputText));
            break;
          case 4:
            System.out.println("Enter word1:");
            word1 = scanner.nextLine();
            System.out.println("Enter word2:");
            word2 = scanner.nextLine();
            System.out.println(calcShortestPath(graph, word1, word2));
            break;
          case 5:
            randomWalk(graph);
            break;
          default:
            System.out.println("Invalid choice. Please try again.");
        }
        System.out.println("Do you want continue? 'yes' or 'no'.");
        String ci = scanner.nextLine();
        if (!ci.equals("yes")) {
          scanner.close();
          return;
        }
      }
    }
  }
}