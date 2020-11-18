package Components.ExcessBudgetPolicy;

import Components.Agent;
import Components.Prefix;

import java.util.HashSet;
import java.util.Set;

/**
 * This
 */
public interface IBacktrackPolicy {

    public void preformBacktrack(Agent agent, Set<Agent> problematicAgents, Set<Prefix> solutions);

}
