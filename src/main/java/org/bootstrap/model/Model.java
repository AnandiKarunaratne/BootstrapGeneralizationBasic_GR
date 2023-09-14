package org.bootstrap.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Model {

    private final Map<String, List<String>> model = new HashMap<>();

    private void createSampleModel() {
        addNode("a");
        addNode("b");
        addNode("c");
        addNode("d");
        addNode("e");
        addNode("f");

        addEdge("a", "b");
        addEdge("a", "d");
        addEdge("b", "c");
        addEdge("c", "f");
        addEdge("d", "e");
        addEdge("e", "e");
        addEdge("e", "f");
        addEdge("f", "a");
    }

    public void addNode(String node) {
        if (!model.containsKey(node)) {
            model.put(node, new ArrayList<>());
        }
    }

    public void addEdge(String sourceNode, String targetNode) {
        if (model.containsKey(sourceNode) && model.containsKey(targetNode)) {
            model.get(sourceNode).add(targetNode);
        }
    }

    public Map<String, List<String>> getModel() {
        createSampleModel();
        return model;
    }

    public double[][] getModelAsMatrix() {
        createSampleModel();
        int vertexCount = model.size();
        String[] nodes = model.keySet().toArray(new String[vertexCount]);
        double[][] adjacencyMatrix = new double[vertexCount][vertexCount];

        for (int i = 0; i < vertexCount; i++) {
            String vertex1 = nodes[i];
            for (int j = 0; j < vertexCount; j++) {
                String vertex2 = nodes[j];
                List<String> neighbors = model.get(vertex1);
                if (neighbors.contains(vertex2)) {
                    adjacencyMatrix[i][j] = 1;
                } else {
                    adjacencyMatrix[i][j] = 0;
                }
            }
        }
        return adjacencyMatrix;
    }

}
