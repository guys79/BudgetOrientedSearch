package Components.ExcessBudgetPolicy;

import Components.Agent;

import java.util.Map;
import java.util.Set;

public class minPriorityAgent extends AbstractBacktrackingPolicy {
    @Override
    protected  Agent getChosenAgent(Set<Agent> problematicAgents,Map<Agent,Double>prioritiesForAgents,Set<Agent> preformingBackTrack){
        double minPriority = Double.MAX_VALUE;
        Agent minPriorityAgent = null;
        double agentPriority;
        for(Agent agent : problematicAgents)
        {
            agentPriority = prioritiesForAgents.get(agent);
            if(agentPriority< minPriority)
            {
                if(!preformingBackTrack.contains(agent)) {
                    minPriority = agentPriority;
                    minPriorityAgent = agent;
                }
            }
        }
        return minPriorityAgent;
    }
}
