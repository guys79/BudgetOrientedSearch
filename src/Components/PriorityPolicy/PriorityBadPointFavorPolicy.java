package Components.PriorityPolicy;

import Components.Agent;
import Components.Node;
import Components.ParamConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PriorityBadPointFavorPolicy implements IPriorityPolicy {

    @Override
    public Map<Agent, Double> getPriorityDistribution(Set<Agent> agents, Map<Agent, Node> current, Map<Agent, Integer> amountOfBacktracks, Map<Agent, Set<Agent>> conflicted) {
        Map<Agent, Double> distribution = new HashMap<>();
        int amountBacktrackForAgent;
        boolean isGoalLessPriority = ParamConfig.getInstance().getGoalLessPriority();
        for (Agent agent : agents)
        {
            if(current.get(agent).equals(agent.getGoal()) & isGoalLessPriority)
                distribution.put(agent,1.0);
            else {

                distribution.put(agent, 2d + agent.getNumOfBadPoints());
            }

        }
        return distribution;
    }

    @Override
    public String toString() {
        return "PriorityBadPointFavorPolicy";
    }
}
