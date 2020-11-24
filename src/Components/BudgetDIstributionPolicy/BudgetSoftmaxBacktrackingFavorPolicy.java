package Components.BudgetDIstributionPolicy;

import Components.Agent;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This class represents a budget distribution policy that gives each agent e^#backtracking
 */
public class BudgetSoftmaxBacktrackingFavorPolicy implements IBudgetDistributionPolicy {

    @Override
    public Map<Agent, Integer> getBudgetDistribution(Set<Agent> agents, int totalBudget, Map<Agent, Integer> amountOfBacktracks) {
        double sum = 0;
        Integer numberOfBacktracks;
        Map<Agent, Integer> budgetDistribution = new HashMap<>();
        double e = Math.E;
        for (Agent agent : agents) {
            numberOfBacktracks = amountOfBacktracks.get(agent);
            if (numberOfBacktracks == null) {
                numberOfBacktracks = 0;
            }
            sum += Math.pow(e, numberOfBacktracks);
        }

        double portion;
        int budgetLeft = totalBudget;
        int budgetForAgent;
        for (Agent agent : agents) {
            numberOfBacktracks = amountOfBacktracks.get(agent);
            if (numberOfBacktracks == null) {
                numberOfBacktracks = 0;
            }
            portion = (Math.pow(e, numberOfBacktracks)) / sum;
            budgetForAgent = (int) (totalBudget * portion);
            budgetDistribution.put(agent, budgetForAgent);
            budgetLeft -= budgetForAgent;
        }

        for (Agent agent : agents) {
            if (budgetLeft == 0)
                break;
            budgetDistribution.put(agent, budgetDistribution.get(agent) + 1);
            budgetLeft--;
        }


        return budgetDistribution;

    }

    @Override
    public String toString() {
        return "BudgetSoftmaxBacktrackingFavorPolicy";
    }
}
