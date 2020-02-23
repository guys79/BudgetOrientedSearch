package Components.PriorityPolicy;

import Components.Agent;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class EqualPriorityPolicy implements IPriorityPolicy{

    @Override
    public Map<Agent, Double> getPriorityDistribution(Set<Agent> agents) {
        Map<Agent, Double> distribution = new HashMap<>();
        for (Agent agent : agents)
        {
            distribution.put(agent,1.0);
        }
        return distribution;
    }
}