package SearchAlgorithms;

import Components.Agent;
import Components.Prefix;
import View.View;

import java.util.Map;

/**
 * This interface represents a search algorithm
 */
public interface IMultiAgentSearchAlgorithm {

    /**
     * This function will search for a path for each agent
     */
    public Map<Agent, Prefix> getSolution();

    /**
     * This function will search for a path for each agent
     *
     * @param view - The view
     */
    public Map<Agent, Prefix> getSolution(View view);
}
