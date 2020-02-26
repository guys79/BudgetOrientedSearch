package Components.BoundedSingleSearchAlgorithms;

import Components.Agent;
import Components.Node;
import Components.Prefix;
import javafx.util.Pair;

import java.util.Map;
import java.util.Set;

/**
 * This interface represents a search algorithm for a single agent that considers past paths
 */
public interface IBoundedSingleSearchAlgorithm {
    public Pair<Prefix,Integer> searchForPrefix(Agent agent, Node current, int budget, Set<Prefix> solutions, Map<Agent,Node> currentLocation);
}
