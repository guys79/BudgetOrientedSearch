package SearchAlgorithms;

import Components.*;
import Components.Agent;
import Components.BoundedSingleSearchAlgorithms.IBoundedSingleSearchAlgorithm;
import Components.BudgetDIstributionPolicy.IBudgetDistributionPolicy;
import Components.FailPolicy.IFailPolicy;
import Components.Heuristics.IHeuristic;
import Components.PriorityPolicy.IPriorityPolicy;

import java.util.*;

/**
 * Our algorithm - BudgetOrientedSearch
 */
public class BudgetOrientedSearch extends AbstractMultiAgentSearchAlgorithm {


    private IHeuristic heuristicFunction;
    private IBudgetDistributionPolicy budgetDistributionPolicy;
    private IPriorityPolicy priorityPolicy;
    private IBoundedSingleSearchAlgorithm searchAlgorithm;
    private int totalBudget;
    private Map<Agent,Double> prioritiesForAgents;
    private Map<Agent,Integer> budgetsForAgents;
    private Set<Agent> agents;
    private int budgetPool;
    private boolean backtracking;
    private int lookahead;
    private int prefixSize;
   // private TimeLimiter timeLimiter;
    private PriorityQueue <Agent> prioritizedAgents;
    private Set<Agent> preformingBackTrack;
    private boolean performDeepLookahead;
    private boolean isSharedBudget;
    private IFailPolicy failPolicy;
    private Map<Agent, Integer> amountOfBacktracks;
    private Map<Agent,Set<Agent>> conflicted;

    /**
     * The constructor of the class
     */
    public BudgetOrientedSearch()
    {
        this.preformingBackTrack = new HashSet<>();
        this.budgetDistributionPolicy = ParamConfig.getInstance().getBudgetDistributionPolicy();
        this.heuristicFunction = ParamConfig.getInstance().getHeuristic();
        this.priorityPolicy = ParamConfig.getInstance().getPriorityPolicy();
        this.failPolicy = ParamConfig.getInstance().getFailPolicy();
        this.totalBudget = Problem.getInstance().getTotalBudget();
        this.agents = Problem.getInstance().getAgents();
        this.searchAlgorithm = ParamConfig.getInstance().getSearchAlgorithm();
        this.backtracking = ParamConfig.getInstance().getBacktrack();
        this.lookahead = Problem.getInstance().getLookahead();
        this.prefixSize = Problem.getInstance().getPrefix();
        this.performDeepLookahead = ParamConfig.getInstance().getPerformDeepLookahead();
        this.amountOfBacktracks = new HashMap<>();
        this.isSharedBudget = ParamConfig.getInstance().getSharedBudget();
        this.conflicted = new HashMap<>();
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

        Collection<Prefix> givenSolution=null;
        Agent agent;
        boolean first = true;
        int iterationNumber = 0;
        //Until he search is finished (succeed or failed)
        while (first|| !isFinished(givenSolution,iterationNumber))
        {
            first = false;
            iterationNumber++;

            PerformanceTracker.getInstance().addIteration();

            System.out.println("Start "+iterationNumber);
            if(!failPolicy.isFinishedAfterFailedIteration() && failPolicy.didTheIterationFail())
            {
                givenSolution = failPolicy.determineSolution(currentLocation,prefixSize).values();
                addPrefixToAgent(givenSolution,  currentLocation,currentPaths);
                continue;
            }

            failPolicy.setDidTheIterationFail(false);
            givenSolution = this.getPrefixForIteration(currentLocation);
            //System.out.println(givenSolution);
            if(givenSolution == null)
            {
                failPolicy.setDidTheIterationFail(true);
                if(!failPolicy.isFinishedAfterFailedIteration() && failPolicy.didTheIterationFail())
                {
                    System.out.println("kaljsdaa");
                    givenSolution = failPolicy.determineSolution(currentLocation,prefixSize).values();
                    addPrefixToAgent(givenSolution,  currentLocation,currentPaths);
                    continue;
                }
                else {
                    currentLocation.clear();
                    break;
                }
            }
            if(failPolicy.isFinishedAfterFailedIteration() || !failPolicy.didTheIterationFail()) {
                addPrefixToAgent(givenSolution, currentLocation, currentPaths);
            }

        }

        return currentPaths;
    }

    /**
     * This function will ass the prefixes to the agent's solutions
     * @param givenSolution - The given prefixes
     * @param currentLocation - The current locations of the agents
     * @param currentPaths - The solution so far
     */
    private void addPrefixToAgent(Collection <Prefix>givenSolution,  Map<Agent,Node> currentLocation,Map<Agent,Prefix> currentPaths)
    {
        Agent agent;
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
    /**
     * This function will check if the search is finished or not
     * @param prefixes - The prefixes of the agents
     * @param iterationNumber
     * @return - True of the search is finished (succeeded or failed)
     */
    public boolean isFinished(Collection<Prefix> prefixes, int iterationNumber)
    {


        if(prefixes.contains(null)) {
            System.out.println("failed - couldn't find a path");
          //  this.timeLimiter.stop();
            failPolicy.setDidTheIterationFail(true);
            if(failPolicy.isFinishedAfterFailedIteration())
                return true;
            return false;
        }

        if(PerformanceTracker.getInstance().getNumberOFIteration() == 1000) {
        //if(PerformanceTracker.getInstance().getNumberOFIteration() == 10000) {
            System.out.println("Max iteration allowed");
            return true;
        }

        if(!SolutionChecker.getInstance().checkSolution(prefixes,iterationNumber))
        {
            System.out.println("failed - the solution is not valid");
            failPolicy.setDidTheIterationFail(true);
            if(failPolicy.isFinishedAfterFailedIteration())
                return true;
            return false;
        }

        failPolicy.setDidTheIterationFail(false);
        for(Prefix prefix :prefixes)
        {

            //If there is still an agent that is not in its goal node
            if(!prefix.getNodeAt(prefix.getSize()-1).equals(prefix.getAgent().getGoal())) {

                System.out.println("Agent "+prefix.getAgent().getId()+" didn't finish he is here - "+prefix.getNodeAt(prefix.getSize()-1)+" instead of here - "+prefix.getAgent().getGoal());

                return false;
            }

        }
 //       this.timeLimiter.stop();
        PerformanceTracker.getInstance().setComplete(true);
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
        if(!isSharedBudget)
            this.budgetsForAgents = this.budgetDistributionPolicy.getBudgetDistribution(agents,totalBudget,amountOfBacktracks);
        else
            this.totalBudget = Problem.getInstance().getTotalBudget();

        this.prioritiesForAgents = this.priorityPolicy.getPriorityDistribution(agents,currentLocation,this.amountOfBacktracks,this.conflicted);
        Set<Prefix> solution = new HashSet<>();

        prioritizedAgents = new PriorityQueue<>(new PriorityCompareAgents(this.prioritiesForAgents));
        prioritizedAgents.addAll(agents);

        Agent currAgent;
        Node currentLoc;
        int budget;
        Prefix solutionForAgent;
        while(prioritizedAgents.size()>0)
        {

            currAgent = prioritizedAgents.poll();
            currentLoc = currentLocation.get(currAgent);

            if(!isSharedBudget)
                budget = this.budgetsForAgents.get(currAgent);
            else
                budget = totalBudget;
            if(budget == -1)//Use Budget pool
            {
                if(!isSharedBudget) {
                    if (budgetPool == 0) {
                        //The agents that tries to do backtrack doesn't have budget
                        System.out.println("The agents that tries to do backtrack doesn't have budget");
                        return null;
                    }
                    budget = budgetPool;
                    budgetPool = 0;
                }
                else
                {
                    if (totalBudget == 0) {
                        //The agents that tries to do backtrack doesn't have budget
                        System.out.println("The agents that tries to do backtrack doesn't have budget");
                        return null;
                    }
                    budget = totalBudget;
                    totalBudget = 0;
                }
            }
            //currentLocation.remove(currAgent);

            solutionForAgent = getPrefixForAgent(currAgent,currentLoc,budget,solution);

           // System.out.println(solutionForAgent);
            if(solutionForAgent == null && (!backtracking || budgetPool == 0)) {
                System.out.println("Failed");
                solution.add(null);
                return solution;
            }
            if(!(solutionForAgent == null && backtracking))
            {
                solution.add(solutionForAgent);
            }




        }
        //System.out.println("After use - "+budgetPool);


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
        Prefix solution = null;

        Triplet<Prefix,Integer,Set<Agent>> prefixAndRemainingBudgetPair = searchForPrefix(agent,current,budget,solutions);


        int remainingBudget = prefixAndRemainingBudgetPair.getSecond();
        solution = prefixAndRemainingBudgetPair.getFirst();
        Set<Agent> problematicAgents = prefixAndRemainingBudgetPair.getThird();
        if(!isSharedBudget)
            this.budgetPool+=remainingBudget;
        else
        {
            this.totalBudget = remainingBudget;
        }

        boolean didTheAgentSucceeded =  problematicAgents== null;
        //Dafuck is this??
//        if(didTheAgentSucceeded && solution.getNodeAt(solution.getSize()-1).equals(agent.getGoal()))
  //          agent.resetBadPoints();
        if(!didTheAgentSucceeded && backtracking && ((budgetPool!=0 && !isSharedBudget) || (isSharedBudget && totalBudget!=0)))
        {
            preformBacktrack(agent,problematicAgents,solutions);
        }


        return solution;
    }

    /**
     * This function will preform backtracking
     * @param agent - The problematic agent
     * @param problematicAgents
     * @param solutions
     */
    private void preformBacktrack(Agent agent, Set<Agent> problematicAgents, Set<Prefix> solutions) {

        if(this.amountOfBacktracks.containsKey(agent))
            this.amountOfBacktracks.put(agent,this.amountOfBacktracks.get(agent)+1);
        else
            this.amountOfBacktracks.put(agent,1);

        if(problematicAgents.size() == 0)
        {
            failPolicy.setDidTheIterationFail(true);
            System.out.println("There are no other agents");
            return;
        }
        Agent minPriorityAgent = findMinPriorityAgent(problematicAgents);
        System.out.println("The problematic agent is "+agent);
        if(minPriorityAgent == null)
        {
            failPolicy.setDidTheIterationFail(true);
            System.out.println("There are no agents that are not backtracking");
            return;
        }
        double newPriorityForAgent, newPriorityForProblematicAgent;
        if(this.prioritizedAgents.size() == 0)
        {
            newPriorityForAgent = 1;
            newPriorityForProblematicAgent = 0;

        }
        else
        {
            Agent nextAgent = this.prioritizedAgents.peek();
            double nextAgentPriority = this.prioritiesForAgents.get(nextAgent);
            newPriorityForAgent = nextAgentPriority + 2;
            newPriorityForProblematicAgent = nextAgentPriority + 1;

        }

        this.prioritiesForAgents.put(agent,newPriorityForAgent);
        this.prioritiesForAgents.put(minPriorityAgent,newPriorityForProblematicAgent);
        this.prioritizedAgents.add(agent);
        this.prioritizedAgents.add(minPriorityAgent);


        this.budgetsForAgents.put(agent,-1);
        this.budgetsForAgents.put(minPriorityAgent,-1);
        Prefix toDelete = null;
        for(Prefix sol : solutions)
        {
            if(sol.getAgent().equals(minPriorityAgent))
            {
                toDelete = sol;
                break;
            }
        }
        solutions.remove(toDelete);
        this.preformingBackTrack.add(agent);
        this.preformingBackTrack.add(minPriorityAgent);

        //Check if there is a constraint where min > agent priority-wise
        //If so, remove the constraint
        if(this.conflicted.containsKey(minPriorityAgent))
        {
            Set<Agent> conflictedForMin = this.conflicted.get(minPriorityAgent);
            //Remove constraint if exists
            if(conflictedForMin.contains(agent))
            {
                conflictedForMin.remove(agent);
                if(conflictedForMin.size() == 0)
                    this.conflicted.remove(minPriorityAgent);
            }
        }
        //Else, add a new constraint
        else {
            //Add the conflicted agents to the conflicted map
            Set<Agent> conflictedForAgent;
            if (this.conflicted.containsKey(agent))
                conflictedForAgent = this.conflicted.get(agent);
            else
                conflictedForAgent = new HashSet<>();

            conflictedForAgent.add(minPriorityAgent);
            this.conflicted.put(agent,conflictedForAgent);
        }





    }




    /**
     * This function will return the agent with the minimal priority out of the agents that
     * @param problematicAgents - The set of problematic agents
     * @return - The agent with the minimum priority
     */
    private Agent findMinPriorityAgent(Set<Agent> problematicAgents) {

        double minPriority = Double.MAX_VALUE;
        Agent minPriorityAgent = null;
        double agentPriority;
        for(Agent agent : problematicAgents)
        {
            agentPriority = this.prioritiesForAgents.get(agent);
            if(agentPriority< minPriority)
            {
                if(!this.preformingBackTrack.contains(agent)) {
                    minPriority = agentPriority;
                    minPriorityAgent = agent;
                }
            }
        }
        return minPriorityAgent;
    }

    /**
     * This function will use a search algorithm to determine the prefix for the agent
     * @param agent - The given agent
     * @param current - The current node
     * @param budget - The budget
     * @param solutions - The previous solutions
     * @return - A prefix for the given agent
     */
    private Triplet<Prefix,Integer,Set<Agent>> searchForPrefix(Agent agent, Node current, int budget,Set<Prefix> solutions)
    {
        if(performDeepLookahead)
            return this.searchAlgorithm.searchForPrefix(agent,current,budget,solutions,prefixSize,-1);
        return this.searchAlgorithm.searchForPrefix(agent,current,budget,solutions,prefixSize,lookahead);
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
