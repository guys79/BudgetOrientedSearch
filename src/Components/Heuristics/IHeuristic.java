package Components.Heuristics;

import Components.Node;

public interface IHeuristic {

    public double getHeuristic(Node node, Node dest);
}
