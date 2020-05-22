package Components.BudgetDIstributionPolicy;

import Components.Agent;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BudgetExponentialBadpointsFavorPolicy implements IBudgetDistributionPolicy{
    @Override
    public Map<Agent, Integer> getBudgetDistribution(Set<Agent> agents, int totalBudget, Map<Agent, Integer> amountOfBacktracks) {
        double sum =0;
        Integer numberOfBadpoints;
        Map<Agent,Integer> budgetDistribution = new HashMap<>();
        for(Agent agent : agents)
        {
            numberOfBadpoints = agent.getNumOfBadPoints();
            sum += 2 - Math.pow(0.5,numberOfBadpoints);
        }

        double portion;
        int budgetLeft = totalBudget;
        int budgetForAgent;
        for(Agent agent : agents)
        {
            numberOfBadpoints = agent.getNumOfBadPoints();
            portion = (2 - Math.pow(0.5,numberOfBadpoints))/sum;
            budgetForAgent = (int)(totalBudget*portion);
            budgetDistribution.put(agent,budgetForAgent);
            budgetLeft-=budgetForAgent;
        }

        for(Agent agent : agents)
        {
            if(budgetLeft == 0)
                break;
            budgetDistribution.put(agent,budgetDistribution.get(agent)+1);
            budgetLeft--;
        }


        return budgetDistribution;

    }

    @Override
    public String toString() {
        return "BudgetExponentialBadpointsFavorPolicy";
    }
}
