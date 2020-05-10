package Components.BudgetDistributionPolicy;

import Components.Agent;

import java.rmi.UnexpectedException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This class represents a budget distribution policy that gives each agent 2-0.5^#backtracking
 */
public class BudgetBacktrackingFavorPolicy implements IBudgetDistributionPolicy{

    @Override
    public Map<Agent, Integer> getBudgetDistribution(Set<Agent> agents, int totalBudget, Map<Agent, Integer> amountOfBacktracks) {
        double sum =0;
        Integer numberOfBacktracks;
        Map<Agent,Integer> budgetDistribution = new HashMap<>();
        for(Agent agent : agents)
        {
            numberOfBacktracks = amountOfBacktracks.get(agent);
            if(numberOfBacktracks == null)
            {
                numberOfBacktracks = 0;
            }
            sum += 2 - Math.pow(0.5,numberOfBacktracks);
        }

        double portion;
        int budgetLeft = totalBudget;
        int budgetForAgent;
        for(Agent agent : agents)
        {
            numberOfBacktracks = amountOfBacktracks.get(agent);
            if(numberOfBacktracks == null)
            {
                numberOfBacktracks = 0;
            }
            portion = (2 - Math.pow(0.5,numberOfBacktracks))/sum;
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
        return "BudgetBacktrackingFavorPolicy";
    }
}
