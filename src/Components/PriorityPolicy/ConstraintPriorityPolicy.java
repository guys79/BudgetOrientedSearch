package Components.PriorityPolicy;

import Components.Agent;
import Components.Node;

import java.util.*;

public class ConstraintPriorityPolicy implements IPriorityPolicy {
    @Override
    public Map<Agent, Double> getPriorityDistribution(Set<Agent> agents, Map<Agent, Node> current, Map<Agent, Integer> amountOfBacktracks, Map<Agent, Set<Agent>> conflicted) {
        List<Agent> agentsOrder = topolgicalSort(agents,  conflicted);
        Map<Agent,Double> mapOfPriority = new HashMap<>();
        for(int i = 0; i<agentsOrder.size();i++)
        {
            Agent agent = agentsOrder.get(i);
            if(current.get(agent).equals(agent.getGoal()))
                mapOfPriority.put(agent,1.0);
            else
            {
                mapOfPriority.put(agentsOrder.get(i),2+(1.0/(i+1)));
            }

        }
        return mapOfPriority;
    }

    private List<Agent> topolgicalSort(Set<Agent>agents, Map<Agent, Set<Agent>> conflicted)
    {
        Map<Agent,NodeTopo> agentToNode =getNodes(agents,conflicted);
        Set<NodeTopo> permanent = new HashSet<>();
        Set<NodeTopo> temporary = new HashSet<>();
        Set<NodeTopo> unmarked = new HashSet<>(agentToNode.values());
        NodeTopo node;
        List<NodeTopo> topoOrder = new ArrayList<>();
        while(unmarked.size()>0)
        {
            node = unmarked.iterator().next();
            try {
                visit(node,unmarked,permanent,temporary,topoOrder);
                if(temporary.size()>0)
                {
                    System.out.println("Da Fuck");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        List<Agent> agentOrder = new ArrayList<>();
        for(int i=0; i<topoOrder.size();i++)
        {
            agentOrder.add(topoOrder.get(i).getAgent());
        }
       return agentOrder;
    }

    private void visit(NodeTopo node, Set<NodeTopo> unmarked, Set<NodeTopo> permanent, Set<NodeTopo> temporary, List<NodeTopo> topoOrder) throws Exception {
        if(unmarked.contains(node))
            unmarked.remove(node);
        if(permanent.contains(node))
            return;
        if(temporary.contains(node))
        {
            throw new Exception("not a DAG");
        }

        temporary.add(node);
        Set<NodeTopo> neighbors = node.getNeighbors();
        for(NodeTopo neighbor: neighbors)
        {
            visit(neighbor,unmarked,permanent,temporary, topoOrder);
        }

        temporary.remove(node);
        permanent.add(node);
        topoOrder.add(0,node);


    }

    /**
     * This function receives a set of agents and their dependencies and will create a graph
     * @param agents - The set of agents
     * @param conflicted - The dndencies btween the agents
     * @return - The nodes
     */
    private Map<Agent,NodeTopo>getNodes(Set<Agent>agents, Map<Agent, Set<Agent>> conflicted)
    {
        Map<Agent,NodeTopo> agentToNode = new HashMap<>();
        for( Agent agent: agents)
        {
            NodeTopo curr;
            if(!agentToNode.containsKey(agent)) {
                curr = new NodeTopo(agent);
                agentToNode.put(agent, curr);
            }
            else
            {
                curr = agentToNode.get(agent);
            }

            if(conflicted.containsKey(agent))
            {
                Set<Agent> conflictedAgents = conflicted.get(agent);
                NodeTopo otherAgent;
                for(Agent conflictedAgent : conflictedAgents)
                {
                    if(!agentToNode.containsKey(conflictedAgent))
                    {
                        otherAgent = new NodeTopo(conflictedAgent);
                        agentToNode.put(conflictedAgent, otherAgent);
                    }
                    else
                    {
                        otherAgent = agentToNode.get(conflictedAgent);
                    }

                    curr.addNeigbor(otherAgent);
                }
            }
        }
        return agentToNode;
    }
    public static void main(String[] args) {
        Agent agent0 = new Agent();
        Agent agent1 = new Agent();
        Agent agent2 = new Agent();
        Agent agent3 = new Agent();
        Agent agent4 = new Agent();
        Agent agent5 = new Agent();
        Agent agent6 = new Agent();

        Map<Agent, Set<Agent>> map = new HashMap<>();
        Set<Agent> agents = new HashSet<>();
        agents.add(agent0);
        agents.add(agent1);
        agents.add(agent2);
        agents.add(agent3);
        agents.add(agent4);
        agents.add(agent5);
        agents.add(agent6);
        //5
        Set<Agent> conf5 = new HashSet<>();
        conf5.add(agent2);
        conf5.add(agent0);
        map.put(agent5, conf5);

        //4
        Set<Agent> conf4 = new HashSet<>();
        conf4.add(agent0);
        conf4.add(agent1);
        map.put(agent4, conf4);

        //2
        Set<Agent> conf2 = new HashSet<>();
        conf2.add(agent3);
        conf2.add(agent0);

        map.put(agent2, conf2);

        //3
        Set<Agent> conf3 = new HashSet<>();
        conf3.add(agent1);
        map.put(agent3, conf3);

        ConstraintPriorityPolicy policy = new ConstraintPriorityPolicy();

        List<Agent> agentso = policy.topolgicalSort(agents,map);

        for(int i = 0; i<agentso.size();i++)
        {
            System.out.println(agentso.get(i));
        }


    }
}

class NodeTopo
{
    private Agent agent;
    private Set<NodeTopo> neighbors;
    //private boolean tempMark;
    //private boolean perMark;
    public NodeTopo(Agent agent) {
        this.agent = agent;
        this.neighbors = new HashSet<>();
        //  this.tempMark = false;
        //this.perMark = false;
    }

    @Override
    public String toString() {
        return ""+agent.getId();
    }

    public Agent getAgent() {
        return agent;
    }

    //Add the edge ((this) -> neighbor
    public void addNeigbor(NodeTopo neighbor)
    {
        this.neighbors.add(neighbor);
    }

    public Set<NodeTopo> getNeighbors() {
        return neighbors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NodeTopo nodeTopo = (NodeTopo) o;
        return Objects.equals(agent, nodeTopo.agent);
    }

    @Override
    public int hashCode() {

        return Objects.hash(agent);
    }



}



