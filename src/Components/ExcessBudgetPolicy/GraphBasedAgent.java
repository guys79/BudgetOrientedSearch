package Components.ExcessBudgetPolicy;

import Components.Agent;
import Components.Prefix;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GraphBasedAgent extends AbstractBacktrackingPolicy {

    private Map<Agent, AgentNode> agentGraph = null;

    @Override
    protected Agent getChosenAgent(Set<Agent> problematicAgents, Map<Agent, Double> prioritiesForAgents, Set<Agent> preformingBackTrack, Set<Prefix> solutions) {
        if (agentGraph == null)
            agentGraph = new HashMap<>();

        updateGraph(problematicAgents, prioritiesForAgents, solutions);
        return null;
    }

    private void updateGraph(Set<Agent> problematicAgents, Map<Agent, Double> prioritiesForAgents, Set<Prefix> solutions) {

        Agent agent;
        for (Prefix solution : solutions) {
            agent = solution.getAgent();
            if (!this.agentGraph.containsKey(agent)) {

            }
        }
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


        public void addNeighbor(AgentNode neighbor) {
            this.neighbors.add(neighbor);
        }

        public boolean isTerminal() {
            return isTerminal;
        }
    }
}


