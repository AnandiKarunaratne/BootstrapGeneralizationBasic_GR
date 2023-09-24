package org.bootstrap.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Future Updates: Update this to convert a DFG into a PNML
 */
public class Model {

    private final Map<String, List<String>> model = new HashMap<>();

    public Model() {
        createSampleModel();
    }

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

    public Map<String, List<String>> getModel() { return model; }

}
