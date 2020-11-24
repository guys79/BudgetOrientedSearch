package Components.ExcessBudgetPolicy;

import Components.Agent;
import Components.Prefix;

import java.util.Map;
import java.util.Set;

/**
 * This class represents the min priority agent strategy
 */
public class minPriorityAgent extends AbstractBacktrackingPolicy {
    @Override
    protected Agent getChosenAgent(Set<Agent> problematicAgents, Map<Agent, Double> prioritiesForAgents, Set<Agent> preformingBackTrack, Set<Prefix> solutions) {
        double minPriority = Double.MAX_VALUE;
        Agent minPriorityAgent = null;
        double agentPriority;
        for (Agent agent : problematicAgents) {
            agentPriority = prioritiesForAgents.get(agent);
            if (agentPriority < minPriority) {
                if (!preformingBackTrack.contains(agent)) {
                    minPriority = agentPriority;
                    minPriorityAgent = agent;
                }
            }
        }
        return minPriorityAgent;
    }
}
