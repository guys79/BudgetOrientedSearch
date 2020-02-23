package Components.BudgetDistributionPolicy;

import Components.Agent;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class EqualBudgetDistributionPolicy implements IBudgetDistributionPolicy {
    @Override
    public Map<Agent, Integer> getBudgetDistribution(Set<Agent> agents, int totalBudget) {
        Map<Agent, Integer> distribution = new HashMap<>();
        int numOfAgents = agents.size();
        int budgetPerAgent = totalBudget/numOfAgents;
        for (Agent agent : agents)
        {
            distribution.put(agent,budgetPerAgent);
        }
        int leftover = totalBudget%numOfAgents;
        for (Agent agent : agents)
        {
            distribution.put(agent,budgetPerAgent+1);
            leftover--;
            if(leftover == 0)
                break;
        }
        return distribution;
    }


}
