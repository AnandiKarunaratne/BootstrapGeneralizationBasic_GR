package org.bootstrap.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Model {

    private Map<String, List<String>> model = new HashMap<>();

    private void createSampleModel() {
        addNode("i");
        addNode("a");
        addNode("b");
        addNode("c");
        addNode("d");
        addNode("e");
        addNode("f");
        addNode("o");

        addEdge("i", "a");
        addEdge("a", "b");
        addEdge("a", "d");
        addEdge("b", "c");
        addEdge("c", "f");
        addEdge("d", "e");
        addEdge("e", "e");
        addEdge("e", "f");
        addEdge("f", "a");
        addEdge("f", "o");
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

}
