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
     * This function will search for a path for each agent
     */
    public Map<Agent, Prefix> getSolution();
}
