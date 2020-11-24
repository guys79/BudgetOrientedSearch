package Components.ExcessBudgetPolicy;

import Components.Agent;
import Components.BoundedSingleSearchAlgorithms.ALSSLRTAStar;
import Components.BoundedSingleSearchAlgorithms.ALSSLRTAStarNode;
import Components.Prefix;

import java.util.*;

public class GraphBasedAgent extends AbstractBacktrackingPolicy {

    private Map<Agent, AgentNode> agentGraph = null;

    @Override
    protected Agent getChosenAgent(Set<Agent> problematicAgents, Map<Agent, Double> prioritiesForAgents, Set<Agent> preformingBackTrack, Set<Prefix> solutions) {
        if (agentGraph == null)
            agentGraph = new HashMap<>();

        updateGraph(solutions);

        searchInGraph(problematicAgents);
        // TODO: 24/11/2020 Need to make All the agents in the path do te backtrack. I think I can save thelist and when this function actiates than we will take the first agent ff the list and use it 
        return null;
    }


    private void createBiDirectionalEdge(AgentNode node1, AgentNode node2) {
        node1.addNeighbor(node2);
        node2.addNeighbor(node1);
    }

    private List<Agent> searchInGraph(Set<Agent> problematicAgents) {

        List<AgentSearchNode> queue = new ArrayList<>();
        Set<AgentNode> visited = new HashSet<>();
        for (Agent problematic : problematicAgents) {
            queue.add(new AgentSearchNode(this.agentGraph.get(problematic)));
        }

        AgentSearchNode curr;
        while (queue.size() > 0) {
            curr = queue.remove(0);
            if (curr.getAgentNode().isTerminal) {
                return getAgentPath(curr);

            }

            Set<AgentNode> neighbors = curr.getAgentNode().getNeighbors();
            for (AgentNode neighbor : neighbors) {
                if (!visited.contains(neighbor)) {
                    queue.add(queue.size(), new AgentSearchNode(neighbor, curr));
                    visited.add(neighbor);
                }
            }

        }
        return null;
    }

    private List<Agent> getAgentPath(AgentSearchNode curr) {
        List<Agent> agentPath = new ArrayList<>();
        while (curr != null) {
            agentPath.add(0, curr.getAgentNode().agent);
            curr = curr.getParent();
        }
        return agentPath;
    }

    private void updateGraph(Set<Prefix> solutions) {

        Agent agent;
        for (Prefix solution : solutions) {
            agent = solution.getAgent();
            if (!this.agentGraph.containsKey(agent)) {
                createNodeForAgent(agent, solutions);
            }
        }

        Set<Agent> problematicForAgent;
        Agent agentInGraph;
        AgentNode agentInGraphNode;
        for (Map.Entry<Agent, AgentNode> entry : this.agentGraph.entrySet()) {
            agentInGraph = entry.getKey();
            agentInGraphNode = entry.getValue();
            problematicForAgent = agentInGraph.getProblematicAgents();
            for (Agent problematic : problematicForAgent) {
                createBiDirectionalEdge(agentInGraphNode, this.agentGraph.get(problematic));
            }
        }
    }


    private void createNodeForAgent(Agent agent, Set<Prefix> solutions) {

        Set<ALSSLRTAStarNode> leaves = agent.getLeaves();
        Iterator<ALSSLRTAStarNode> iter = leaves.iterator();
        ALSSLRTAStarNode leaf;
        Set<Agent> problematic = agent.getProblematicAgents();
        Set<Agent> problematicForLeaf;
        while (iter.hasNext()) {
            leaf = iter.next();
            problematicForLeaf = ALSSLRTAStar.isStateValid(leaf, solutions, leaf.getParent());
            if (problematicForLeaf.size() != 0) {
                iter.remove();
            }
            problematic.addAll(problematicForLeaf);


        }
        if (leaves.size() != 0)
            this.agentGraph.put(agent, new AgentNode(agent, true));
        else
            this.agentGraph.put(agent, new AgentNode(agent, false));

    }


    class AgentNode {
        private Agent agent;
        private boolean isTerminal;
        private Set<AgentNode> neighbors;

        public AgentNode(Agent agent, boolean isTerminal) {
            this.agent = agent;
            this.isTerminal = isTerminal;
            this.neighbors = new HashSet<>();
        }

        public Set<AgentNode> getNeighbors() {
            return neighbors;
        }

        public void addNeighbor(AgentNode neighbor) {
            this.neighbors.add(neighbor);
        }

        public boolean isTerminal() {
            return isTerminal;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AgentNode agentNode = (AgentNode) o;
            return Objects.equals(agent, agentNode.agent);
        }

        @Override
        public int hashCode() {

            return Objects.hash(agent);
        }
    }

    class AgentSearchNode {
        private AgentNode agentNode;
        private AgentSearchNode parent;

        public AgentSearchNode(AgentNode agentNode, AgentSearchNode parent) {
            this.agentNode = agentNode;
            this.parent = parent;
        }

        public AgentSearchNode(AgentNode agentNode) {
            this.agentNode = agentNode;
            this.parent = null;

        }

        public void setParent(AgentSearchNode parent) {
            this.parent = parent;
        }

        public AgentSearchNode getParent() {
            return parent;
        }

        public AgentNode getAgentNode() {
            return agentNode;
        }
    }
}


