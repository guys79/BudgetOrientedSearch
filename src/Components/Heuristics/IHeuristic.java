package Components.Heuristics;

import Components.Node;

/**
 * This class represents a generic heuristic function
 */
public interface IHeuristic {

    /**
     * This function will return the heuristic of the given node where the second node is considered to be the goal node
     * @param node - the node
     * @param dest - the goal node
     * @return - The heuristic value
     */
    public double getHeuristic(Node node, Node dest);
}
