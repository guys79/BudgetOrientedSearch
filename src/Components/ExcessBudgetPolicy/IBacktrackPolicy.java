package Components.ExcessBudgetPolicy;

import Components.Agent;
import Components.Prefix;

import java.util.HashSet;
import java.util.Set;

public interface IBacktrackPolicy {
    /**
     * This function will preform backtracking
     * @param agent - The problematic agent
     * @param problematicAgents
     * @param solutions
     */
    public void preformBacktrack(Agent agent, Set<Agent> problematicAgents, Set<Prefix> solutions);

}
