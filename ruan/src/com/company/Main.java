package com.company;

import java.util.*;
import static com.company.Method.*;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please enter the file path:");
        String filePath = scanner.nextLine();

        //功能1，创建图
        Map<String, Map<String, Integer>> graph = GenGraph(filePath);

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
                    ShowGraph(graph);
                    break;
                case 2:
                    System.out.println("Enter word1:");
                    String word1 = scanner.nextLine();
                    System.out.println("Enter word2:");
                    String word2 = scanner.nextLine();
                    System.out.println(QueryBridgeWords(graph, word1, word2));
                    break;
                case 3:
                    System.out.println("Enter text:");
                    String inputText = scanner.nextLine();
                    System.out.println(GenNewTxt(graph, inputText));
                    break;
                case 4:
                    System.out.println("Enter word1:");
                    word1 = scanner.nextLine();
                    System.out.println("Enter word2:");
                    word2 = scanner.nextLine();
                    System.out.println(CalcDij(graph, word1, word2));
                    break;
                case 5:
                    RandomWalk(graph);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
            System.out.println("Do you want continue? 'yes' or 'no'.");
            String ci = scanner.nextLine();
            if (ci.equals("yes")){

            }
            else{
                scanner.close();
                return;
            }
        }
    }
}