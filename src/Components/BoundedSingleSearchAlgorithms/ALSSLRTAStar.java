package Components.BoundedSingleSearchAlgorithms;

import Components.Agent;
import Components.CostFunction.ICostFunction;
import Components.Heuristics.HeuristicWithPersonalDatabase;
import Components.Heuristics.IHeuristic;
import Components.Node;
import Components.Prefix;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This class represents the alSS-LRTA* algorithm (hernandes et al. , 2011)
 */
public class ALSSLRTAStar implements IBoundedSingleSearchAlgorithm
{
    private ICostFunction costFunction;//The cost function
    private Map<Node,Double> nodeToGValue;//Key - node, value a pair of the g value
    private HeuristicWithPersonalDatabase heuristic;//The heuristic function
    private Agent agent;//The agent whom we do the search for
    public Node goal;//The goal node

    
    /**
     * The constructor of the class
     */
    public ALSSLRTAStar(ICostFunction costFunction, HeuristicWithPersonalDatabase heuristic)
    {
        this.costFunction = costFunction;
        this.nodeToGValue = new HashMap<>();
        this.heuristic = heuristic;
        this.goal = this.agent.getGoal();
        if(! (heuristic instanceof HeuristicWithPersonalDatabase))
            throw new UnsupportedOperationException("The heuristic function is not with database");


    }

    @Override
    public Pair<Prefix, Integer> searchForPrefix(Agent agent, Node current, int budget, Set<Prefix> solutions, Map<Agent, Node> currentLocation) {
        this.agent = agent;
        // TODO: 26/02/2020 complete
        return null;
    }

    /**
     * This function will return the g value of the node
     * @param node - The g value of the node
     * @return - The g value of the node
     */
    private double getGValue(Node node)
    {
        if(this.nodeToGValue.containsKey(node))
            return this.nodeToGValue.get(node);
        return Double.MAX_VALUE;
    }

    /**
     * This function will return the h value of the node
     * @param node - The h value of the node
     * @return - The h value of the node
     */
    private double getHValue(Node node)
    {
        return this.heuristic.getHeuristic(node,this.goal,agent);
    }

    /**
     * This function will return the h value of the node
     * @param node - The f value of the node
     * @return - The f value of the node
     */
    private double getFValue(Node node)
    {
        return getGValue(node) + getHValue(node);
    }

    /**
     * This function will set the g value for the
     * @param node - The given node
     * @param newVal - The new gVal
     */
    private void setGValue(Node node, double newVal)
    {
        this.nodeToGValue.put(node,newVal);
    }

    /**
     * This function will set the h value for the
     * @param node - The given node
     * @param newVal - The new hVal
     */
    private void setHValue(Node node, double newVal)
    {
        this.heuristic.storeNewVal(agent,node,goal,newVal);
    }


}
