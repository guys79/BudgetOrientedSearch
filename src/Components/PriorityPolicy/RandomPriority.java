package Components.PriorityPolicy;

import Components.Agent;
import Components.Node;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RandomPriority implements IPriorityPolicy {
    @Override
    public Map<Agent, Double> getPriorityDistribution(Set<Agent> agents, Map<Agent, Node> current, Map<Agent, Integer> amountOfBacktracks) {
        Map<Agent, Double> distribution = new HashMap<>();
        for (Agent agent : agents)
        {
            if(current.get(agent).equals(agent.getGoal()))
                distribution.put(agent,1.0);
            else
                distribution.put(agent,Math.random()+1);

        }
        return distribution;
    }
}
