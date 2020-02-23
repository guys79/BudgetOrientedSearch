package Components.BudgetDistributionPolicy;

import Components.Agent;

import java.util.Map;
import java.util.Set;

/**
 * This class represents a generic budget distribution policy
 */
public interface IBudgetDistributionPolicy {

    /**
     * This fnction will return the distribution of the total budget over all of the agents
     * @param agents - The given agents
     * @param totalBudget - The total amount of budget for all of the agents
     * @return - A dictionary where the key is the agent and the value is the budget of the agent
     */
    public Map<Agent,Integer> getBudgetDistribution(Set<Agent> agents, int totalBudget);
}
