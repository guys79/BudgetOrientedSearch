package SearchAlgorithms.BMAA;

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
        Problem.getInstance().setPrefix(2);
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
            List<Node> solList = new ArrayList<>();
            solList.add(this.curr.get(agent));
            if (this.successor.containsKey(agent)) {
                node = this.successor.get(agent).get(0);
                if (push && this.blocked.containsKey(node)) {
                    this.pushAgents(this.blocked.get(node), node);
                }

                if (!this.blocked.containsKey(node)) {
                    this.moveAgent(agent, node);
                }

            }


            solList.add(this.curr.get(agent));
            Prefix sol = new Prefix(solList, agent);
            solution.add(sol);
        }
        //test

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

    private Set<NodeBMAA> getNeighbors(NodeBMAA node) {
        int time = node.getTimeStamp();
        Set<Node> neighbors = getNeighbors(node.getNode());
        Set<NodeBMAA> neighborsContainer = new HashSet<>();
        for (Node neighbor : neighbors) {
            neighborsContainer.add(new NodeBMAA(neighbor, time, node));
        }
        return neighborsContainer;

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
        if (!this.successor.containsKey(agent) || time >= this.limitForAgent.get(agent)) {
            Pair<PriorityQueue<NodeBMAA>, Set<NodeBMAA>> openClosed = this.search(agent);
            PriorityQueue<NodeBMAA> open = openClosed.getKey();
            if (open.size() > 0) {
                NodeBMAA n = open.poll();
                double f_val = getFVal(n, agent);
                Set<NodeBMAA> closed = openClosed.getValue();
                this.updateHeuristicPhase(closed, f_val, agent);
                this.limitForAgent.put(agent, time + moves);
            }
            //The search function will update all the parameters

        }

    }

    private double getFVal(NodeBMAA n, Agent agent) {
        return this.getGVal(n) + this.getHVal(n.getNode(), agent);
    }

    private void updateHeuristicPhase(Set<NodeBMAA> closed, double f_val, Agent agent) {

        double h;
        for (NodeBMAA node : closed) {
            h = f_val - node.getMinGval();
            this.updateHeuristicValue(node.getNode(), h, agent);
        }
    }

    private void updateHeuristicValue(Node node, double h, Agent agent) {
        this.heuristic.storeNewVal(agent, node, agent.getGoal(), h);
    }

    private double getHVal(Node n, Agent agent) {
        return this.heuristic.getHeuristic(n, agent.getGoal(), agent);
    }

    private double getGVal(NodeBMAA n) {

        return n.getGVal();
    }

    private void setGVal(NodeBMAA n, double newGVal, PriorityQueue<NodeBMAA> open) {
        n.setGVal(newGVal);
        if (open.contains(n)) {
            open.remove(n);
            open.add(n);
        }
    }


    private Pair<PriorityQueue<NodeBMAA>, Set<NodeBMAA>> search(Agent agent) {
        PriorityQueue<NodeBMAA> open = new PriorityQueue<>(new CompareFVal(agent));
        Set<NodeBMAA> closed = new HashSet<>();
        Pair<PriorityQueue<NodeBMAA>, Set<NodeBMAA>> openClosed = new Pair<>(open, closed);
        int exp = 0;
        Node current = this.curr.get(agent);
        NodeBMAA currentNode = new NodeBMAA(current, 0);
        open.add(currentNode);
        this.setGVal(currentNode, 0, open);
        NodeBMAA expanded;
        while (open.size() > 0) {
            expanded = open.peek();
            if (expanded.equals(agent.getGoal()) || exp >= expensions) {

                calculatePath(agent, expanded);
                return openClosed;
            }
            expanded = open.poll();
            closed.add(expanded);
            Set<NodeBMAA> neighbors = getNeighbors(expanded);
            double expandedGVal = expanded.getGVal();
            double cost;

            for (NodeBMAA neighbor : neighbors) {

                if (this.blocked.containsKey(neighbor))
                    if (!neighbor.equals(agent.getGoal()))
                        continue;

                if (!closed.contains(neighbor)) {
                    if (!open.contains(neighbor)) {
                        setGVal(neighbor, Double.MAX_VALUE, open);
                    }

                    cost = expandedGVal + ParamConfig.getInstance().getCostFunction().getCost(expanded.getNode(), neighbor.getNode());
                    if (getGVal(neighbor) > cost) {
                        setGVal(neighbor, cost, open);
                        setParent(neighbor, expanded);
                    }
                    if (!open.contains(neighbor)) {
                        open.add(neighbor);
                    }
                }
            }


            exp += 1;

        }
        return openClosed;
    }

    private void setParent(NodeBMAA node, NodeBMAA parent) {
        node.setParent(parent);
    }

    private void calculatePath(Agent agent, NodeBMAA last) {
        List<Node> path = new ArrayList<>();
        while (last.getParent() != null) {
            path.add(0, last.getNode());
            last = last.getParent();
        }
        this.successor.put(agent, path);
    }

    public class CompareFVal implements Comparator<NodeBMAA> {
        private Agent agent;

        public CompareFVal(Agent agent) {
            this.agent = agent;
        }

        @Override
        public int compare(NodeBMAA o1, NodeBMAA o2) {
            double f1 = getFVal(o1, agent);
            double f2 = getFVal(o2, agent);
            if (f1 > f2)
                return 1;
            if (f1 < f2)
                return -1;
            return 0;
        }
    }
}
