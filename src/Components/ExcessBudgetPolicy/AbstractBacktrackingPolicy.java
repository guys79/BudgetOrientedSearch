package Components.ExcessBudgetPolicy;

import Components.Agent;
import Components.FailPolicy.IFailPolicy;
import Components.Prefix;

import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public abstract class AbstractBacktrackingPolicy implements IBacktrackPolicy {
    @Override
    public void preformBacktrack(Agent agent, Set<Agent> problematicAgents, Set<Prefix> solutions, Map<Agent, Integer> amountOfBacktracks,
                                 IFailPolicy failPolicy, PriorityQueue<Agent> prioritizedAgents, Map<Agent, Double> prioritiesForAgents, Set<Agent> preformingBackTrack, Map<Agent, Set<Agent>> conflicted, Map<Agent, Integer> budgetsForAgents) {
        if (amountOfBacktracks.containsKey(agent))
            amountOfBacktracks.put(agent, amountOfBacktracks.get(agent) + 1);
        else
            amountOfBacktracks.put(agent, 1);

        if (problematicAgents.size() == 0) {
            failPolicy.setDidTheIterationFail(true);
            System.out.println("There are no other agents");
            return;
        }
        Agent chosenAgent = this.getChosenAgent(problematicAgents, prioritiesForAgents, preformingBackTrack, solutions);
        System.out.println("The problematic agent is " + agent);
        if (chosenAgent == null) {
            failPolicy.setDidTheIterationFail(true);
            System.out.println("There are no agents that are not backtracking");
            return;
        }
        double newPriorityForAgent, newPriorityForProblematicAgent;
        if (prioritizedAgents.size() == 0) {
            newPriorityForAgent = 1;
            newPriorityForProblematicAgent = 0;

        } else {
            Agent nextAgent = prioritizedAgents.peek();
            double nextAgentPriority = prioritiesForAgents.get(nextAgent);
            newPriorityForAgent = nextAgentPriority + 2;
            newPriorityForProblematicAgent = nextAgentPriority + 1;

        }

        prioritiesForAgents.put(agent, newPriorityForAgent);
        prioritiesForAgents.put(chosenAgent, newPriorityForProblematicAgent);
        prioritizedAgents.add(agent);
        prioritizedAgents.add(chosenAgent);


        budgetsForAgents.put(agent, -1);
        budgetsForAgents.put(chosenAgent, -1);
        Prefix toDelete = null;
        for (Prefix sol : solutions) {
            if (sol.getAgent().equals(chosenAgent)) {
                toDelete = sol;
                break;
            }
        }
        solutions.remove(toDelete);
        preformingBackTrack.add(agent);
        preformingBackTrack.add(chosenAgent);

        //Check if there is a constraint where min > agent priority-wise
        //If so, remove the constraint
        if (conflicted.containsKey(chosenAgent)) {
            Set<Agent> conflictedForMin = conflicted.get(chosenAgent);
            //Remove constraint if exists
            if (conflictedForMin.contains(agent)) {
                conflictedForMin.remove(agent);
                if (conflictedForMin.size() == 0)
                    conflicted.remove(chosenAgent);
            }
        }
        //Else, add a new constraint
        else {
            //Add the conflicted agents to the conflicted map
            Set<Agent> conflictedForAgent;
            if (conflicted.containsKey(agent))
                conflictedForAgent = conflicted.get(agent);
            else
                conflictedForAgent = new HashSet<>();

            conflictedForAgent.add(chosenAgent);
            conflicted.put(agent, conflictedForAgent);
        }

    }

    /**
     * This funnction will choose the agent to perform backtrack with
     *
     * @param problematicAgents   - The problematic agents
     * @param prioritiesForAgents - Map - key - agent, val - budget for agent
     * @param preformingBackTrack - number of agents that are currently preforming backtrack
     * @param solutions           - The solutions so far
     * @return - The problematic agents
     */
    protected abstract Agent getChosenAgent(Set<Agent> problematicAgents, Map<Agent, Double> prioritiesForAgents, Set<Agent> preformingBackTrack, Set<Prefix> solutions);
}
