package Components.FailPolicy;

import Components.Node;
import Components.Prefix;
import Components.Agent;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdditionAttempt  extends AbstractFailPolicy{
    public AdditionAttempt(Map<Agent, Prefix> currentPaths,int prefixSize) {
        super(currentPaths,prefixSize);
    }

    @Override
    public boolean isFinishedAfterFailedIteration() {
        return false;
    }

    @Override
    public Map<Agent, Prefix> determineSolution() {
        Map<Agent, Prefix> sol = new HashMap<>();

        Agent agent;
        Node currNode;
        Prefix agentSol;
        List<Node> nodes;
        for(Map.Entry<Agent,Prefix> agent_prefix : this.currentPaths.entrySet())
        {
            agent = agent_prefix.getKey();
            currNode = agent_prefix.getValue().getNodeAt(agent_prefix.getValue().getSize()-1);
            nodes = new ArrayList<>();
            for (int i=0; i<this.prefixSize;i++)
            {
                nodes.add(currNode);
            }
            agentSol = new Prefix(nodes,agent);
            sol.put(agent,agentSol);
        }
        return sol;
    }
}
