package Components.FailPolicy;

import Components.Node;
import Components.Prefix;
import Components.Agent;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This fail policy determines that in case of a failure, all the agents will stay in their current location
 */
public class AdditionAttempt  extends AbstractFailPolicy{
    /**
     * The empty constructor
     */
    public AdditionAttempt() {
        super();
    }

    @Override
    public boolean isFinishedAfterFailedIteration() {
        return false;
    }

    @Override
    public Map<Agent, Prefix> determineSolution(Map<Agent, Node> currentPaths,int prefixSize) {
        Map<Agent, Prefix> sol = new HashMap<>();

        Agent agent;
        Node currNode;
        Prefix agentSol;
        List<Node> nodes;
        for(Map.Entry<Agent,Node> agent_prefix : currentPaths.entrySet())
        {
            agent = agent_prefix.getKey();
            currNode = agent_prefix.getValue();
            nodes = new ArrayList<>();
            for (int i=0; i<prefixSize;i++)
            {
                nodes.add(currNode);
            }
            agentSol = new Prefix(nodes,agent);
            sol.put(agent,agentSol);
        }
        return sol;
    }
}
