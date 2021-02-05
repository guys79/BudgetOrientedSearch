package BMAA;

import Components.*;
import SearchAlgorithms.AbstractMultiAgentSearchAlgorithm;
import SearchAlgorithms.IMultiAgentSearchAlgorithm;

import java.util.*;

/**
 * This class represents teh BMAA * algorithm
 */
public class BMAA extends AbstractMultiAgentSearchAlgorithm {

    @Override
    public Map<Agent, Prefix> getSolution() {
        Set<Agent> agents = Problem.getInstance().getAgents();
        Map<Agent, Prefix> solution = new HashMap<>();
        for (Agent agent : agents) {
            solution.put(agent, new Prefix(agent.getStart(), agent));
        }
        int time = 0; //Time and iterations needs to be synchronized
        Set<Prefix> currSolution = null;
        boolean first = true;
        while (first || !this.isFinished(currSolution, time)) {
            first = false;
            currSolution = this.npcController(agents);
            for (Prefix prefix : currSolution) {
                solution.get(prefix.getAgent()).extendPrefix(prefix);
            }
            time += 1;
            System.out.println(String.format("Iteration = %d", time));
            PerformanceTracker.getInstance().addIteration();
        }

        PerformanceTracker.getInstance().setComplete(true);
        return solution;

    }

    /**
     * This function will return True IFF the algorithm has finished running.
     * @param prefixes - The given prefixes
     * @param iterationNumber - The iteration number
     * @return - True IFF the algorithm has finished running.
     */
    private boolean isFinished(Set<Prefix> prefixes, int iterationNumber) {


        if (prefixes.contains(null)) {
            System.out.println("failed - couldn't find a path");
            return false;
        }

        System.out.println(PerformanceTracker.getInstance().getNumberOFIteration());
        if (PerformanceTracker.getInstance().getNumberOFIteration() >= 500) {
            System.out.println("Max iteration allowed");
            return true;
        }

        if (!SolutionChecker.getInstance().checkSolution(prefixes, iterationNumber)) {
            System.out.println("failed - the solution is not valid");
            return false;
        }

        for (Prefix prefix : prefixes) {

            //If there is still an agent that is not in its goal node
            if (!prefix.getNodeAt(prefix.getSize() - 1).equals(prefix.getAgent().getGoal())) {
                System.out.println("Agent " + prefix.getAgent().getId() + " didn't finish he is here - " + prefix.getNodeAt(prefix.getSize() - 1) + " instead of here - " + prefix.getAgent().getGoal());
                return false;
            }

        }
        PerformanceTracker.getInstance().setComplete(true);
        System.out.println("Success!");
        return true;
    }

    /**
     * The NPC-Controller precedure of the BMAA*
     * @param agents - The given set of agents.
     * @return - The prefix for each agent
     */
    private Set<Prefix> npcController(Set<Agent> agents) {
        // TODO: 05/02/2021 This function perhaps finds each time a single node and not a whole prefix.
        Set<Prefix> solution = new HashSet<>();
        Prefix prefixForAgent;
        for (Agent agent : agents) {
            prefixForAgent = this.searchPhase(agent);
            solution.add(prefixForAgent);
        }
        return solution;
    }

    /**
     * The search-phase of the BMAA*
     * @param agent - A given agent.
     * @return - The prefix for the given agent.
     */
    private Prefix searchPhase(Agent agent) {
        return null;
    }

}
