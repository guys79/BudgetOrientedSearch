package SearchAlgorithms;

import Components.*;
import Components.BoundedSingleSearchAlgorithms.ALSSLRTAStar;
import Components.BoundedSingleSearchAlgorithms.IBoundedSingleSearchAlgorithm;
import Components.BudgetDistributionPolicy.IBudgetDistributionPolicy;
import Components.Heuristics.IHeuristic;
import Components.PriorityPolicy.IPriorityPolicy;
import javafx.util.Pair;

import java.util.*;

/**
 * Our algorithm - BudgetOrientedSearch
 */
public class BudgetOrientedSearch implements IMultiAgentSearchAlgorithm {
    // TODO: 27/02/2020 AddBacktrack 

    private IHeuristic heuristicFunction;
    private IBudgetDistributionPolicy budgetDistributionPolicy;
    private IPriorityPolicy priorityPolicy;
    private IBoundedSingleSearchAlgorithm searchAlgorithm;
    private int totalBudget;
    private Map<Agent,Double> prioritiesForAgents;
    private Map<Agent,Integer> budgetsForAgents;
    private Set<Agent> agents;
    private int budgetPool;
    private int prefixSize;

    /**
     * The constructor of the class
     */
    public BudgetOrientedSearch()
    {
        this.budgetDistributionPolicy = ParamConfig.getInstance().getBudgetDistributionPolicy();
        this.heuristicFunction = ParamConfig.getInstance().getHeuristic();
        this.priorityPolicy = ParamConfig.getInstance().getPriorityPolicy();
        this.totalBudget = Problem.getInstance().getTotalBudget();
        this.agents = Problem.getInstance().getAgents();
        this.searchAlgorithm = ParamConfig.getInstance().getSearchAlgorithm();
        this.prefixSize = Problem.getInstance().getPrefix();
    }
    @Override
    public Map<Agent, Prefix> getSolution() {

        Map<Agent,Node> currentLocation = new HashMap<>();
        Map<Agent,Prefix> currentPaths = new HashMap<>();
        //Setting the start node as the current
        for(Agent agent : agents)
        {
            currentLocation.put(agent,agent.getStart());
            currentPaths.put(agent,new Prefix(agent.getStart(),agent));
        }

        Set<Prefix> givenSolution;
        Agent agent;
        //Until he search is finished (succeed or failed)
        while (!isFinished(currentPaths.values()))
        {
            givenSolution = this.getPrefixForIteration(currentLocation);

            currentLocation.clear();
            for(Prefix prefixForAgent : givenSolution)
            {
                if(prefixForAgent == null)
                    break;

                //The agent that the prefix is for
                agent = prefixForAgent.getAgent();
                //Update the current locations
                currentLocation.put(agent,prefixForAgent.getNodeAt(prefixForAgent.getSize()-1));
                //Update the current paths
                currentPaths.get(agent).extendPrefix(prefixForAgent);
            }
        }

        return null;
    }

    /**
     * This function will check if the search is finished or not
     * @param prefixes - The prefixes of the agents
     * @return - True of the search is finished (succeeded or failed)
     */
    public boolean isFinished(Collection<Prefix> prefixes)
    {
        if(prefixes.contains(null)) {
            System.out.println("failed - couldn't find a path");
            return true;
        }

        if(!SolutionChecker.getInstance().checkSolution(prefixes))
        {
            System.out.println("failed - the solution is not valid");
        }


        for(Prefix prefix :prefixes)
        {
            //If there is still an agent that is not in its goal node
            if(!prefix.getNodeAt(prefix.getSize()-1).equals(prefix.getAgent().getGoal()))
                return false;

        }

        System.out.println("Success!");
        return true;
    }

    /**
     * This function will return the prefixes for the agents after an iteration
     * @param currentLocation - The current locations of the agents
     * @return - Prefixes for the agents after an iteration
     */
    private Set<Prefix> getPrefixForIteration(Map<Agent,Node> currentLocation)
    {
        this.budgetPool = 0;
        this.budgetsForAgents = this.budgetDistributionPolicy.getBudgetDistribution(agents,totalBudget);
        this.prioritiesForAgents = this.priorityPolicy.getPriorityDistribution(agents);
        Set<Prefix> solution = new HashSet<>();
        PriorityQueue <Agent> prioritizedAgents = new PriorityQueue<>(new PriorityCompareAgents(this.prioritiesForAgents));

        Agent currAgent;
        Node currentLoc;
        int budget;
        Prefix solutionForAgent;
        while(prioritizedAgents.size()>0)
        {
            currAgent = prioritizedAgents.poll();
            currentLoc = currentLocation.get(currAgent);
            budget = this.budgetsForAgents.get(currAgent);
            currentLocation.remove(currAgent);
            solutionForAgent = getPrefixForAgent(currAgent,currentLoc,budget,solution);
            solution.add(solutionForAgent);
            if(solutionForAgent == null) {
                System.out.println("Failed");
                return solution;
            }
        }



        return solution;
    }


    /**
     * This function will calculate a prefix for a given agent
     * @param agent - The given agent
     * @param current - The current node
     * @param budget - The budget
     * @param solutions - The previous solutions
     * @return - A prefix for a single agent
     */
    private Prefix getPrefixForAgent(Agent agent,Node current,int budget,Set<Prefix> solutions)
    {
        // TODO: 26/02/2020 Insert Backtrack
        Prefix solution = null;

        Pair<Prefix,Integer> prefixAndRemainingBudgetPair = searchForPrefix(agent,current,budget,solutions);

        int remainingBudget = prefixAndRemainingBudgetPair.getValue();
        solution = prefixAndRemainingBudgetPair.getKey();

        this.budgetPool+=remainingBudget;

        return solution;
    }

    /**
     * This function will use a search algorithm to determine the prefix for the agent
     * @param agent - The given agent
     * @param current - The current node
     * @param budget - The budget
     * @param solutions - The previous solutions
     * @return - A prefix for the given agent
     */
    private Pair<Prefix,Integer> searchForPrefix(Agent agent, Node current, int budget,Set<Prefix> solutions)
    {
        return this.searchAlgorithm.searchForPrefix(agent,current,budget,solutions,prefixSize);
    }


    /**
     * This class will compare the agents according to their priorities
     * If the priority of an agent is bigger than the priority of the other agent, The first is more significant
     */
    class PriorityCompareAgents implements Comparator<Agent>
    {
        private Map<Agent,Double> prioritiesForAgents;//The agents' priorities

        /**
         * The constructor of the class
         * @param prioritiesForAgents - The priorities for agents
         */
        public PriorityCompareAgents(Map<Agent,Double> prioritiesForAgents)
        {
            this.prioritiesForAgents = prioritiesForAgents;
        }

        @Override
        public int compare(Agent o1, Agent o2) {
            double p1 = this.prioritiesForAgents.get(o1);
            double p2 = this.prioritiesForAgents.get(o2);

            if(p1>p2)
                return -1;
            if(p1<p2)
                return 1;
            return 0;
        }
    }
}
