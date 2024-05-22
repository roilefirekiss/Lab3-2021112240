package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import static com.company.write.*;

public class Method {
    public static Map<String, Map<String, Integer>> GenGraph(String filename) {
        Map<String, Map<String, Integer>> graph = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            String lastWord = null;

            while ((line = br.readLine()) != null) {
                String[] words = line.replaceAll("[^a-zA-Z\\s]", "").toLowerCase().split("\\s+");
                for (String word : words) {
                    if (!word.isEmpty()) {
                        graph.putIfAbsent(word, new HashMap<>());
                        if (lastWord != null) {
                            graph.putIfAbsent(lastWord, new HashMap<>());
                            graph.get(lastWord).put(word, graph.get(lastWord).getOrDefault(word, 0) + 1);
                        }
                        lastWord = word;
                    }
                }
                lastWord = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return graph;
    }


    public static void ShowGraph(Map<String, Map<String, Integer>> graph) {
        for (Map.Entry<String, Map<String, Integer>> entry : graph.entrySet()) {
            String from = entry.getKey();
            for (Map.Entry<String, Integer> edge : entry.getValue().entrySet()) {
                String to = edge.getKey();
                int weight = edge.getValue();
                System.out.println(from + " -> " + to + " (weight: " + weight + ")");
            }
        }
    }

    public static String QueryBridgeWords(Map<String, Map<String, Integer>> graph, String word1, String word2) {
        if (!graph.containsKey(word1)) {
            return "No " + word1 + " in the graph!";
        }
        else if(!graph.containsKey(word2)){
            return "No " + word2 + " in the graph!";
        }

        Set<String> bridgeWords = new HashSet<>();
        for (String bridge : graph.get(word1).keySet()) {
            if (graph.containsKey(bridge) && graph.get(bridge).containsKey(word2)) {
                bridgeWords.add(bridge);
            }
        }

        if (bridgeWords.isEmpty()) {
            return "No bridge from " + word1 + " to " + word2 + "!";
        }

        return "The bridge from " + word1 + " to " + word2 + " are: " + String.join(", ", bridgeWords) + ".";
    }

    public static String GenNewTxt(Map<String, Map<String, Integer>> graph, String inputText) {
        String[] words = inputText.replaceAll("[^a-zA-Z\\s]", "").toLowerCase().split("\\s+");
        StringBuilder newText = new StringBuilder();
        Random r = new Random();
        for (int i = 0; i < words.length - 1; i++) {
            newText.append(words[i]).append(" ");
            if (!graph.containsKey(words[i])) {
                continue;
            }
            Set<String> bridgeWords = new HashSet<>();
            for (String bridge : graph.get(words[i]).keySet()) {
                if (graph.containsKey(bridge) && graph.get(bridge).containsKey(words[i + 1])) {
                    bridgeWords.add(bridge);
                }
            }
            if (!bridgeWords.isEmpty()) {
                List<String> bridgeWordsList = new ArrayList<>(bridgeWords);
                String bridgeWord = bridgeWordsList.get(r.nextInt(bridgeWordsList.size()));//随机选择桥
                newText.append(bridgeWord).append(" ");
            }
        }
        newText.append(words[words.length - 1]);

        return newText.toString();
    }

    public static String CalcDij(Map<String, Map<String, Integer>> graph, String word1, String word2) {
        if (!graph.containsKey(word1)) {
            return "No " + word1 + " in the graph!";
        }
        else if(!graph.containsKey(word2)){
            return "No " + word2 + " in the graph!";
        }

        Map<String, Integer> distances = new HashMap<>();
        Map<String, String> previous = new HashMap<>();
        PriorityQueue<String> queue = new PriorityQueue<>(Comparator.comparingInt(distances::get));

        // 初始化距离数组和优先队列
        for (String node : graph.keySet()) {
            if (node.equals(word1)) {
                distances.put(node, 0);
            } else {
                distances.put(node, Integer.MAX_VALUE);
            }
            queue.add(node);
        }

        while (!queue.isEmpty()) {
            String current = queue.poll();
            if (current.equals(word2)) break;

            int currentDistance = distances.get(current);
            if (currentDistance == Integer.MAX_VALUE) break;

            Map<String, Integer> neighbors = graph.get(current);
            if (neighbors == null) continue;

            for (Map.Entry<String, Integer> neighbor : neighbors.entrySet()) {
                String neighborKey = neighbor.getKey();
                int alt = currentDistance + neighbor.getValue();

                if (alt < distances.getOrDefault(neighborKey, Integer.MAX_VALUE)) {
                    distances.put(neighborKey, alt);
                    previous.put(neighborKey, current);
                    queue.remove(neighborKey); // 队列更新
                    queue.add(neighborKey);
                }
            }
        }

        if (!previous.containsKey(word2)) {
            return word1 + " and " + word2 + " are not connected!";
        }

        // 路径重构
        List<String> path = new ArrayList<>();
        for (String at = word2; at != null; at = previous.get(at)) {
            path.add(at);
        }
        Collections.reverse(path);

        return String.join(" -> ", path);
    }

    public static String RandomWalk(Map<String, Map<String, Integer>> graph) {
        List<String> nodes = new ArrayList<>(graph.keySet());
        if (nodes.isEmpty()) {
            return "The graph is empty.";
        }

        Random random = new Random();
        String current = nodes.get(random.nextInt(nodes.size()));
        StringBuilder walk = new StringBuilder(current);

        Set<String> visitedEdges = new HashSet<>();

        while (true) {
            Map<String, Integer> neighbors = graph.get(current);
            if (neighbors == null || neighbors.isEmpty()) {
                break;
            }

            List<String> neighborList = new ArrayList<>(neighbors.keySet());
            String next = neighborList.get(random.nextInt(neighborList.size()));
            String edge = current + " -> " + next;

            if (visitedEdges.contains(edge)) {
                break;
            }

            visitedEdges.add(edge);
            walk.append(" ").append(next);
            current = next;
        }

        String output = walk.toString();
        Printout(output,"out.txt");
        return output;
    }
}
