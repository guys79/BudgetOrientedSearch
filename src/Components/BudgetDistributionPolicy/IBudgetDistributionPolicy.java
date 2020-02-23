package Components.BudgetDistributionPolicy;

import Components.Agent;

import java.util.Map;
import java.util.Set;

public interface IBudgetDistributionPolicy {

    public Map<Agent,Integer> getBudgetDistribution(Set<Agent> agents, int totalBudget);
}
