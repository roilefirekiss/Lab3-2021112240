package test;

import static com.company.Method.genGraph;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class test_w {
    String filePath = "test.txt";
    Map<String, Map<String, Integer>> graph = genGraph(filePath);

    @Test
    public void testNoWord1AndWord2InGraph() {
        // 路径1: 1 -> 2
        String result = queryBridgeWords(graph, "non", "ono");
        assertEquals("No non and ono in the graph!", result);
    }

    @Test
    public void testNoWord1InGraph() {
        // 路径2: 1 -> 3 -> 4
        String result = queryBridgeWords(graph, "non", "to");
        assertEquals("No non in the graph!", result);
    }

    @Test
    public void testNoWord2InGraph() {
        // 路径3: 1 -> 3 -> 5 -> 6
        String result = queryBridgeWords(graph, "to", "non");
        assertEquals("No non in the graph!", result);
    }

    @Test
    public void testNoBridgeWords() {
        // 路径4: 1 -> 3 -> 5 -> 7 -> 8 -> 9 -> 10 -> 11 -> 12
        String result = queryBridgeWords(graph, "to", "and");
        assertEquals("No bridge from to to and!", result);
    }

    @Test
    public void testBridgeWordsExist() {
        // 路径5: 1 -> 3 -> 5 -> 7 -> 8 -> 9 -> 10 -> 11 -> 13
        String result = queryBridgeWords(graph, "to", "out");
        assertEquals("The bridge from to to out are: seek.", result);
    }

    public static String queryBridgeWords(Map<String, Map<String, Integer>> graph, String word1, String word2) {
        if (!graph.containsKey(word1) && !graph.containsKey(word2)) {
            return "No " + word1 + " and " + word2 + " in the graph!";
        } else if (!graph.containsKey(word1)) {
            return "No " + word1 + " in the graph!";
        } else if (!graph.containsKey(word2)) {
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
}