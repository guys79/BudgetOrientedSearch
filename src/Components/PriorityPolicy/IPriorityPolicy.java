package Components.PriorityPolicy;

import Components.Agent;

import java.util.Map;
import java.util.Set;

/**
 * This class represents a generic priority policy
 */
public interface IPriorityPolicy {

    /**
     * This function will return the distribution of priorities of the agents
     * @param agents - The agents
     * @return - A dictionary where the key idthe agent and the value is it's priority
     */
    public Map<Agent,Double> getPriorityDistribution(Set<Agent> agents);
}
