package BMAA;

import Components.*;
import Components.Heuristics.HeuristicWithPersonalDatabase;
import SearchAlgorithms.AbstractMultiAgentSearchAlgorithm;
import SearchAlgorithms.IMultiAgentSearchAlgorithm;
import javafx.util.Pair;

import java.util.*;

/**
 * This class represents teh BMAA * algorithm
 */
public class BMAA extends AbstractMultiAgentSearchAlgorithm {

    private boolean push;
    private int expensions;
    private int moves;
    private int time;
    private boolean flow;
    private Map<Agent, Node> curr;
    private Map<Agent, List<Node>> successor;
    private Map<Node, Agent> blocked;
    private Map<Agent, Prefix> solution;
    private Map<Agent, Integer> limitForAgent;
    private HeuristicWithPersonalDatabase heuristic;//The heuristic function
    private Map<Node, Double> gVals;
    private Map<Node, Node> parants;

    /**
     * The constructor of the class
     *
     * @param push - True IFF we are allowed to push agents
     */
    public BMAA(boolean push, int expensions, int moves, boolean flow) {

        this.push = push;
        this.time = 0;
        this.flow = flow;
        this.limitForAgent = new HashMap<>();
        this.expensions = expensions;
        this.moves = moves;
        this.curr = new HashMap<>();
        this.successor = new HashMap<>();
        this.solution = new HashMap<>();
        this.blocked = new HashMap<>();
        this.heuristic = (HeuristicWithPersonalDatabase) (ParamConfig.getInstance().getHeuristic());
    }

    @Override
    public Map<Agent, Prefix> getSolution() {
        Set<Agent> agents = Problem.getInstance().getAgents();
        solution = new HashMap<>();
        Node start;
        for (Agent agent : agents) {
            start = agent.getStart();
            solution.put(agent, new Prefix(start, agent));
            this.curr.put(agent, agent.getStart());
            this.blocked.put(start, agent);
            this.limitForAgent.put(agent, moves);
        }

        Set<Prefix> currSolution = null;
        boolean first = true;
        while (first || !this.isFinished(currSolution, time)) {
            first = false;
            currSolution = this.npcController(agents);
            for (Prefix prefix : currSolution) {
                solution.get(prefix.getAgent()).extendPrefix(prefix);
            }

        }

        PerformanceTracker.getInstance().setComplete(true);
        return solution;

    }

    /**
     * This function will return True IFF the algorithm has finished running.
     *
     * @param prefixes        - The given prefixes
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
     *
     * @param agents - The given set of agents.
     * @return - The prefix for each agent
     */
    private Set<Prefix> npcController(Set<Agent> agents) {
        // TODO: 05/02/2021 This function perhaps finds each time a single node and not a whole prefix.
        Set<Prefix> solution = new HashSet<>();

        for (Agent agent : agents) {
            this.searchPhase(agent);
        }

        Node node;
        for (Agent agent : agents) {
            if (this.successor.containsKey(agent)) {
                node = this.successor.get(agent).get(0);
                if (push && this.blocked.containsKey(node)) {
                    this.pushAgents(this.blocked.get(node), node);
                }

                if (!this.blocked.containsKey(node)) {
                    this.moveAgent(agent, node);
                }

            }


            solution.add(new Prefix(this.curr.get(agent), agent));
        }
        //test
        if (this.successor.size() > 0)
            System.out.println("WTFFFFFF");
        System.out.println(String.format("Iteration = %d", time));
        // TODO: 06/02/2021 time limit

        PerformanceTracker.getInstance().addIteration();//time equals (iterations * prefix size)
        return solution;
    }

    private void moveAgent(Agent agent, Node node) {
        //Remove from successor
        this.successor.get(agent).remove(0);
        if (this.successor.get(agent).size() == 0)
            this.successor.remove(agent);
        //Remove the node he currently occupies
        Node lastCurr = this.curr.get(agent);
        //The node is no longer blocked
        this.blocked.remove(lastCurr);
        //The new node is now blocked
        this.blocked.put(node, agent);
        //The agent occupies the new node
        this.curr.put(agent, node);

    }

    private void pushAgents(Agent currentOccupier, Node node) {

        Set<Node> neighbors = getNeighbors(node);
        for (Node neighbor : neighbors) {
            if (!this.blocked.containsKey(neighbor)) {
                //Needs to recalculate
                this.successor.remove(currentOccupier);
                //The node is no longer blocked
                this.blocked.remove(node);
                //The new node is now blocked
                this.blocked.put(node, currentOccupier);
                //The agent occupies the new node
                this.curr.put(currentOccupier, node);
            }
        }

    }

    private Set<Node> getNeighbors(Node node) {
        if (flow) {
            // TODO: 06/02/2021
            return null;
        } else {
            return node.expend();
        }

    }

    /**
     * The search-phase of the BMAA*
     *
     * @param agent - A given agent.
     * @return - The prefix for the given agent.
     */
    private void searchPhase(Agent agent) {
        if (this.curr.get(agent).equals(agent.getGoal()))
            return;
        if (!this.successor.containsKey(agent) || time > this.limitForAgent.get(agent)) {
            Pair<PriorityQueue<Node>, PriorityQueue<Node>> openClosed = this.search(agent);
            PriorityQueue<Node> open = openClosed.getKey();
            if (open.size() > 0) {
                Node n = open.poll();
                double f_val = this.getGVal(n) + this.getHVal(n, agent);
                PriorityQueue<Node> closed = openClosed.getValue();
                this.updateHeuristicPhase(closed, f_val, agent);
                this.limitForAgent.put(agent, time + moves);
            }
            //The search function will update all the parameters

        }
    }

    private void updateHeuristicPhase(PriorityQueue<Node> closed, double f_val, Agent agent) {

        double h;
        for (Node node : closed) {
            h = f_val - getGVal(node);
            this.updateHeuristicValue(node, h, agent);
        }
    }

    private void updateHeuristicValue(Node node, double h, Agent agent) {
        this.heuristic.storeNewVal(agent, node, agent.getGoal(), h);
    }

    private double getHVal(Node n, Agent agent) {
        // TODO: 06/02/2021 Maybe a problem in the first time.
        return this.heuristic.getHeuristic(n, agent.getGoal(), agent);
    }

    private double getGVal(Node n) {

        return this.gVals.get(n);
    }

    private void setGVal(Node n, double newGVal) {
        this.gVals.put(n, newGVal);
    }


    private Pair<PriorityQueue<Node>, PriorityQueue<Node>> search(Agent agent) {
        PriorityQueue<Node> open = new PriorityQueue<>();//add comparator
        PriorityQueue<Node> closed = new PriorityQueue<>();//add comparator
        this.gVals = new HashMap<>();
        this.parants = new HashMap<>();
        Pair<PriorityQueue<Node>, PriorityQueue<Node>> openClosed = new Pair<>(open, closed);
        int exp = 0;
        Node currentNode = this.curr.get(agent);
        open.add(currentNode);
        this.setGVal(currentNode, 0);
        Node expanded;
        while (open.size() > 0) {
            expanded = open.peek();
            if (expanded.equals(agent.getGoal()) || exp >= expensions) {
                calculatePath(agent, expanded);
                return openClosed;
            }
            expanded = open.poll();
            closed.add(expanded);
            Set<Node> neighbors = getNeighbors(expanded);
            double expandedGVal = getGVal(expanded);
            double cost;
            for (Node neighbor : neighbors) {
                if (this.blocked.containsKey(neighbor))
                    if (!neighbor.equals(agent.getGoal()))
                        continue;

                if (!closed.contains(neighbor)) {
                    if (!open.contains(neighbor)) {
                        setGVal(neighbor, Double.MAX_VALUE);
                    }

                    cost = expandedGVal + ParamConfig.getInstance().getCostFunction().getCost(expanded, neighbor);
                    if (getGVal(neighbor) > cost) {
                        setGVal(neighbor, cost);
                        setParent(neighbor, expanded);
                    }
                    if(!open.contains(expanded))
                    {
                        open.add(expanded);
                    }
                }
            }

            exp += 1;
        }
        return openClosed;
    }

    private void setParent(Node node, Node parent) {
        this.parants.put(node,parent);
    }

    private void calculatePath(Agent agent, Node last) {
        List<Node> path = new ArrayList<>();
        while(!this.parants.containsKey(last)) {
            path.add(0, last);
            last = this.parants.get(last);
        }
        this.successor.put(agent, path);
    }


}
