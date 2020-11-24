package Components.ExcessBudgetPolicy;

import Components.Agent;
import Components.FailPolicy.IFailPolicy;
import Components.Prefix;

import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * This interface represents a generic Backtracking policy
 */
public interface IBacktrackPolicy {

    /**
     * This function will preform the backtrack mechanism
     *
     * @param agent               _ The agent that needs to preform the backtrack
     * @param problematicAgents   - The agents that conflict with the given agent
     * @param solutions           - The given solutions
     * @param amountOfBacktracks  - Map - key - agent, val - num of backtrack
     * @param failPolicy          = The fail policy
     * @param prioritizedAgents   - The priority queue of agents (planning order)
     * @param prioritiesForAgents - Map - key - agent, val - budget for agent
     * @param preformingBackTrack - number of agents that are currently preforming backtrack
     * @param conflicted          - Map - key - agent, val - a set of all the agent that have conflicted with the agents throughout the search
     * @param budgetsForAgents    - Map - key - agent, val - the budget of the agent
     */
    public void preformBacktrack(Agent agent, Set<Agent> problematicAgents, Set<Prefix> solutions, Map<Agent, Integer> amountOfBacktracks,
                                 IFailPolicy failPolicy, PriorityQueue<Agent> prioritizedAgents, Map<Agent, Double> prioritiesForAgents, Set<Agent> preformingBackTrack, Map<Agent, Set<Agent>> conflicted, Map<Agent, Integer> budgetsForAgents);

}
