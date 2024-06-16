package test;

import static com.company.Method.*;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import java.util.Map;

public class test_b {
    String filePath = "test.txt";
    Map<String, Map<String, Integer>> graph = genGraph(filePath);
    Map<String, Map<String, Integer>> emptygraph = genGraph("kong.txt");

    @Test
    public void testLuWordse() {
        // 正常情况：两个单词都在图中，且存在路径
        String result = calcShortestPath(graph,"to", "and");
        assertEquals("to -> explore -> strange -> new -> life -> and", result);
    }

    @Test
    public void testNoLuWords() {
        // 正常情况：两个单词没有桥接词
        String result = calcShortestPath(graph,"document", "add");
        assertEquals("document and add are not connected!", result);
    }

    @Test
    public void testWordNoInGraph() {
        // 异常情况：第一个单词不在图中
        String result = calcShortestPath(graph,"non", "and");
        assertEquals("No non in the graph!", result);

        // 异常情况：第二个单词不在图中
        result = calcShortestPath(graph,"to", "non");
        assertEquals("No non in the graph!", result);
    }

    @Test
    public void testBothWordsNoInGraph() {
        // 异常情况：两个单词都不在图中
        String result = calcShortestPath(graph,"non", "ono");
        assertEquals("No non and ono in the graph!", result);
    }

    @Test
    public void testSameInputWords() {
        // 边界情况：输入单词相同
        String result = calcShortestPath(graph,"to", "to");
        assertEquals("to and to are not connected!", result);
    }

    @Test
    public void testEmptyInput() {
        // 边界情况：输入为空字符串
        String result = calcShortestPath(graph,"to", "");
        assertEquals("No  in the graph!", result);

        result = calcShortestPath(graph,"", "to");
        assertEquals("No  in the graph!", result);

        result = calcShortestPath(graph,"", "");
        assertEquals("No  and  in the graph!", result);
    }

    @Test
    public void testLongestWords() {
            // 边界情况：输入最长单词
        String longWord1 = "iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii"; // 假设100为最大长度
        String longWord2 = "t";
        String result = calcShortestPath(graph,longWord1, longWord2);
        assertEquals("No iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii and t in the graph!", result);
    }

    @Test
    public void testEmptyGraph() {
        String result = calcShortestPath(emptygraph,"i", "t");
        assertEquals("The graph is empty!", result);
    }
}
