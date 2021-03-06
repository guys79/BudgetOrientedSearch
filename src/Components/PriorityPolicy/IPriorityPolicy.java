package Components.PriorityPolicy;

import Components.Agent;
import Components.Node;

import java.util.Map;
import java.util.Set;

/**
 * This class represents a generic priority policy
 */
public interface IPriorityPolicy {

    /**
     * This function will return the distribution of priorities of the agents
     * The bigger the priority, the more significant the agent is
     *
     * @param agents     - The agents
     * @param current    - The current locations of the agents
     * @param conflicted
     * @return - A dictionary where the key idthe agent and the value is it's priority
     */
    public Map<Agent, Double> getPriorityDistribution(Set<Agent> agents, Map<Agent, Node> current, Map<Agent, Integer> amountOfBacktracks, Map<Agent, Set<Agent>> conflicted);
}
