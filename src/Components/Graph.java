package Components;

import Components.CostFunction.ICostFunction;

import java.util.Set;

public class Graph {

    Set<Node> nodes;
    ICostFunction costFunction;

    public Graph(Set<Node> nodes) {
        this.nodes = nodes;
        this.costFunction = ParamConfig.getInstance().getCostFunction();
    }
}
