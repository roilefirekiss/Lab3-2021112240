package com.company;

import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the file path:");
        String filePath = scanner.nextLine();

        // Read the text file and generate the directed graph
        Map<String, Map<String, Integer>> graph = readAndGenerateGraph(filePath);

        // Menu for different functionalities
        while (true) {
            System.out.println("Choose an option:");
            System.out.println("1. Show Directed Graph");
            System.out.println("2. Query Bridge Words");
            System.out.println("3. Generate New Text");
            System.out.println("4. Calculate Shortest Path");
            System.out.println("5. Random Walk");
            System.out.println("6. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume the newline

            switch (choice) {
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
                    System.out.println("Enter new text:");
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
                    System.out.println(randomWalk(graph));
                    break;
                case 6:
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public static Map<String, Map<String, Integer>> readAndGenerateGraph(String filePath) {
        Map<String, Map<String, Integer>> graph = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            String lastWord = null;

            while ((line = br.readLine()) != null) {
                String[] words = line.replaceAll("[^a-zA-Z\\s]", "").toLowerCase().split("\\s+");
                for (String word : words) {
                    if (!word.isEmpty()) {
                        if (lastWord != null) {
                            graph.putIfAbsent(lastWord, new HashMap<>());
                            graph.get(lastWord).put(word, graph.get(lastWord).getOrDefault(word, 0) + 1);
                        }
                        lastWord = word;
                    }
                }
                lastWord = null; // Reset at end of each line
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return graph;
    }

    public static void showDirectedGraph(Map<String, Map<String, Integer>> graph) {
        for (Map.Entry<String, Map<String, Integer>> entry : graph.entrySet()) {
            String from = entry.getKey();
            for (Map.Entry<String, Integer> edge : entry.getValue().entrySet()) {
                String to = edge.getKey();
                int weight = edge.getValue();
                System.out.println(from + " -> " + to + " (weight: " + weight + ")");
            }
        }
    }

    public static String queryBridgeWords(Map<String, Map<String, Integer>> graph, String word1, String word2) {
        if (!graph.containsKey(word1) || !graph.containsKey(word2)) {
            return "No " + word1 + " or " + word2 + " in the graph!";
        }

        Set<String> bridgeWords = new HashSet<>();
        for (String bridge : graph.get(word1).keySet()) {
            if (graph.containsKey(bridge) && graph.get(bridge).containsKey(word2)) {
                bridgeWords.add(bridge);
            }
        }

        if (bridgeWords.isEmpty()) {
            return "No bridge words from " + word1 + " to " + word2 + "!";
        }

        return "The bridge words from " + word1 + " to " + word2 + " are: " + String.join(", ", bridgeWords) + ".";
    }

    public static String generateNewText(Map<String, Map<String, Integer>> graph, String inputText) {
        String[] words = inputText.replaceAll("[^a-zA-Z\\s]", "").toLowerCase().split("\\s+");
        StringBuilder newText = new StringBuilder();

        for (int i = 0; i < words.length - 1; i++) {
            newText.append(words[i]).append(" ");
            Set<String> bridgeWords = new HashSet<>();
            for (String bridge : graph.get(words[i]).keySet()) {
                if (graph.containsKey(bridge) && graph.get(bridge).containsKey(words[i + 1])) {
                    bridgeWords.add(bridge);
                }
            }
            if (!bridgeWords.isEmpty()) {
                String bridgeWord = bridgeWords.iterator().next(); // Randomly choose one (could be improved)
                newText.append(bridgeWord).append(" ");
            }
        }
        newText.append(words[words.length - 1]);

        return newText.toString();
    }

    public static String calcShortestPath(Map<String, Map<String, Integer>> graph, String word1, String word2) {
        if (!graph.containsKey(word1) || !graph.containsKey(word2)) {
            return "No " + word1 + " or " + word2 + " in the graph!";
        }

        Map<String, Integer> distances = new HashMap<>();
        Map<String, String> previous = new HashMap<>();
        PriorityQueue<String> queue = new PriorityQueue<>(Comparator.comparingInt(distances::get));

        // Initialize distances and priority queue
        for (String node : graph.keySet()) {
            if (node.equals(word1)) {
                distances.put(node, 0);
            } else {
                distances.put(node, Integer.MAX_VALUE);
            }
            queue.add(node);
        }

        // Process the queue
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
                    queue.remove(neighborKey); // Update the priority queue
                    queue.add(neighborKey);
                }
            }
        }

        if (!previous.containsKey(word2)) {
            return word1 + " and " + word2 + " are not connected!";
        }

        // Reconstruct the path
        List<String> path = new ArrayList<>();
        for (String at = word2; at != null; at = previous.get(at)) {
            path.add(at);
        }
        Collections.reverse(path);

        return String.join(" -> ", path);
    }

    public static String randomWalk(Map<String, Map<String, Integer>> graph) {
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
                break; // No outgoing edges
            }

            List<String> neighborList = new ArrayList<>(neighbors.keySet());
            String next = neighborList.get(random.nextInt(neighborList.size()));
            String edge = current + " -> " + next;

            if (visitedEdges.contains(edge)) {
                break; // Edge already visited
            }

            visitedEdges.add(edge);
            walk.append(" ").append(next);
            current = next;
        }

        return walk.toString();
    }
}