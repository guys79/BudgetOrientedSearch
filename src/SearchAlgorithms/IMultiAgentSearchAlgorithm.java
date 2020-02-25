package SearchAlgorithms;

import Components.Agent;
import Components.Node;
import Components.Prefix;

import java.util.Map;

/**
 * This interface represents a search algorithm
 */
public interface IMultiAgentSearchAlgorithm {

    /**
     * This function will sarch for a path for
     * @param current
     * @param goa
     * @return
     */
    public Map<Agent, Prefix> getSolution(Map<Agent, Node> current, Map<Agent, Node> goa);
}
