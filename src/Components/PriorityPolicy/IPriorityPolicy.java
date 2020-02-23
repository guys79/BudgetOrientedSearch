package Components.PriorityPolicy;

import Components.Agent;

import java.util.Map;
import java.util.Set;

public interface IPriorityPolicy {

    public Map<Agent,Double> getPriorityDistribution(Set<Agent> agents);
}
