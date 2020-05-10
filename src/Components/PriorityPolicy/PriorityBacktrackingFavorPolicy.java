package Components.PriorityPolicy;

import Components.Agent;
import Components.Node;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PriorityBacktrackingFavorPolicy implements IPriorityPolicy {
    @Override
    public Map<Agent, Double> getPriorityDistribution(Set<Agent> agents, Map<Agent, Node> current,Map<Agent, Integer> amountOfBacktracks) {
        Map<Agent, Double> distribution = new HashMap<>();
        int amountBacktrackForAgent;
        for (Agent agent : agents)
        {
            if(current.get(agent).equals(agent.getGoal()))
                distribution.put(agent,1.0);
            else {
                if(amountOfBacktracks.containsKey(agent))
                    amountBacktrackForAgent = amountOfBacktracks.get(agent);
                else
                    amountBacktrackForAgent = 0;

                double backtrackBonus = 2 - Math.pow(0.5,amountBacktrackForAgent);
                distribution.put(agent, 2.0*backtrackBonus);
            }

        }
        return distribution;
    }

    @Override
    public String toString() {
        return "PriorityBacktrackingFavorPolicy";
    }
}
