package Components.BoundedSingleSearchAlgorithms;

import Components.*;
import Components.CostFunction.ICostFunction;
import Components.Heuristics.HeuristicWithPersonalDatabase;
import Components.Heuristics.IHeuristic;
import javafx.util.Pair;

import java.util.*;

/**
 * This class represents the alSS-LRTA* algorithm (hernandes et al. , 2011)
 */
public class ALSSLRTAStar implements IBoundedSingleSearchAlgorithm
{
    private ICostFunction costFunction;//The cost function
    private Map<ALSSLRTAStarNode,Double> nodeToGValue;//Key - node, value a pair of the g value
    private HeuristicWithPersonalDatabase heuristic;//The heuristic function
    private Agent agent;//The agent whom we do the search for
    private Node goal;//The goal node
    private Map<Agent,Set<Node>> updated;//The nodes that have been updated
    private Map<ALSSLRTAStarNode,ALSSLRTAStarNode> predMap; // key - node, val - its predecessor
    private Set <ALSSLRTAStarNode> inQueue;//The nodes that are in the open list
    private ALSSLRTAStarNode currentBest;//The current best node

    /**
     * The constructor of the class
     */
    public ALSSLRTAStar(ICostFunction costFunction, HeuristicWithPersonalDatabase heuristic)
    {
        this.costFunction = costFunction;
        this.heuristic = heuristic;
        this.updated = new HashMap<>();
        if(! (heuristic instanceof HeuristicWithPersonalDatabase))
            throw new UnsupportedOperationException("The heuristic function is not with database");



    }

    @Override
    public Pair<Prefix, Integer> searchForPrefix(Agent agent, Node current, int budget, Set<Prefix> solutions,int prefixSize) {

        this.agent = agent;
        this.nodeToGValue = new HashMap<>();
        this.goal = this.agent.getGoal();

        this.predMap = new HashMap<>();
        this.inQueue = new HashSet<>();
        currentBest = null;



        PriorityQueue<ALSSLRTAStarNode> openList = new PriorityQueue<>(new AStartFValueNodeComparator());
        Set<Node> closed = new HashSet<>();

        //The A* procedure
        int remainBudget = AStarProcedure(current,openList,budget,closed,prefixSize,solutions);

        //No solution
        if(openList == null)
            return new Pair<>(null,remainBudget);

        //Get the best Node
        ALSSLRTAStarNode best = getBestState(openList,prefixSize);
        openList.add(best);
        //Update nodes
        dijkstraProcedure(openList,closed);

        List<Node> pathNodes = new ArrayList<>();

        ALSSLRTAStarNode nodeInPath = best;


        //Assemble prefix
        while(nodeInPath != null)
        {
            pathNodes.add(0,nodeInPath.getNode());
            nodeInPath = getPredecessor(nodeInPath);
        }

        int size = pathNodes.size();
        //Stay in place
        while(size<prefixSize)
        {
            pathNodes.add(pathNodes.get(size-1));
            size = pathNodes.size();
        }
        Prefix prefix = new Prefix(pathNodes,agent);

        Pair<Prefix,Integer> sol = new Pair<>(prefix,remainBudget);
        return sol;
    }

    /**
     * This function represents the A* procedure
     * @param current - The current node
     * @param openList - The open list
     * @param budget - The budget
     * @param closed - The close list
     * @param prefixSize - The prefix size
     * @param solutions - The prev solutions
     * @return - The remaining budget
     */
    private int AStarProcedure(Node current,PriorityQueue<ALSSLRTAStarNode> openList,int budget,Set<Node> closed,int prefixSize,Set<Prefix> solutions)
    {

        //Set current node's gVal to 0 and add to open

        ALSSLRTAStarNode currentNode = new ALSSLRTAStarNode(current,0);
        setGValue(currentNode,0);
        addToOpenList(currentNode,openList);

        int expansions = 0;
        Set<Node> neighbors;
        double neighborGValue;
        double costFromCurrentToNeighbor;
        double pathCostFromCurrentToNeighbor;
        double currentGValue;
        int currentTimeStamp;
        ALSSLRTAStarNode neighborNode;
        Set<ALSSLRTAStarNode> rest = new HashSet<>();
       // int [] t = {4,5};
       // Node test = new Node(t);
        while(openList.size()>0 && expansions<budget)
        {
         //   for(ALSSLRTAStarNode n : openList)
          //  {
            //    System.out.println(n +" F - "+getFValue(n));
           // }
            currentNode = dequeueOpenList(openList);
          //  System.out.println(currentNode);
            expansions++;

            //If the node is the goal node, stop the search
            if(isGoal(currentNode.getNode())) {
                openList.add(currentNode);
                return budget - expansions;
            }

            //Add to close list
            closed.add(currentNode.getNode());


            //Expend node
            currentTimeStamp = currentNode.getTimeStamp();

            //Will not develop further than the prefix size
            if(currentTimeStamp < prefixSize-1) {

                neighbors = currentNode.getNode().expend();
                currentGValue = getGValue(currentNode);

                for (Node neighbor : neighbors) {
                    neighborNode = new ALSSLRTAStarNode(neighbor, currentTimeStamp + 1);

                    //Only if the state is valid we will insert is to the open
                    if(isStateValid(neighborNode,solutions,currentNode)) {


                        neighborGValue = getGValue(neighborNode);
                        costFromCurrentToNeighbor = this.costFunction.getCost(currentNode.getNode(), neighbor);
                        pathCostFromCurrentToNeighbor = currentGValue + costFromCurrentToNeighbor;

                        //If the cost through the current node is cheaper than the current g val of the node
                        if (neighborGValue > pathCostFromCurrentToNeighbor) {
                            //Set the new g val
                            setGValue(neighborNode, pathCostFromCurrentToNeighbor);
                            //Set the current node as the predecessor
                            setPredecessor(neighborNode, currentNode);
                            //Insert neighbor into open
                            addToOpenList(neighborNode, openList);

                        }

                    }

                }

            }
            else
            {
                rest.add(currentNode);
            }
        }
        for(ALSSLRTAStarNode node : rest)
        {
            addToOpenList(node,openList);
        }

        return budget - expansions;

    }

    /**
     * This function wll return the best state from the open
     * @param openList - The openList
     * @param prefixSize
     * @return - The best state
     */
    private ALSSLRTAStarNode getBestState(PriorityQueue<ALSSLRTAStarNode> openList, int prefixSize)
    {
        ALSSLRTAStarNode best;
        PriorityQueue<ALSSLRTAStarNode> heap = new PriorityQueue<>(new AStartFValueUpdatedNodeComparator(prefixSize));
        heap.addAll(openList);
     //   for(ALSSLRTAStarNode s : heap)
       //     if(s.getTimeStamp() == prefixSize -1)
         //       System.out.println(s+" "+getFValue(s));
        best = heap.poll();
        if(best == null)
            best = currentBest;
        else
            openList.remove(best);
        return best;
    }

    /**
     * This function will update both the heuristics of the nodes and the update mark
     * @param openList - The openList
     * @param closeList - The closed list
     */
    private void dijkstraProcedure(PriorityQueue<ALSSLRTAStarNode> openList,Set<Node> closeList)
    {

        for(Node node : closeList) {
            this.setHValue(node, Double.MAX_VALUE);

        }

        if(PerformanceTracker.getInstance().getNumberOFIteration() == 217)
            System.out.println();
        PriorityQueue<Node> openListOrederedByHVal = new PriorityQueue<>(new HValueNodeComparator());
        Set<Node> openSet = new HashSet<>();
        for (ALSSLRTAStarNode node : openList) {
            openSet.add(node.getNode());
        }
        openListOrederedByHVal.addAll(openSet);


        Node current;
        double hVal,initialHVal,hNeighbor,hValThrpughCurrent;
        Set<Node> neighbors;

        while(closeList.size()>0)
        {
            current = openListOrederedByHVal.poll();

            hVal = getHValue(current);
            initialHVal = getInitialHValue(current);

            if(hVal>initialHVal)
                updateNode(current);

            closeList.remove(current);

            neighbors = current.expend();

            for(Node neighbor : neighbors)
            {
                if (closeList.contains(neighbor)) {
                        hNeighbor = getHValue(neighbor);
                        hValThrpughCurrent = hVal + this.costFunction.getCost(current, neighbor);
                        if (hNeighbor > hValThrpughCurrent) {
                            setHValue(neighbor, hValThrpughCurrent);
                            openListOrederedByHVal.add(neighbor);
                        }
                    }

            }
        }
    }

    /**
     * This function will return if the state is valid
     * @param node - The given state
     * @param solutions - The prev solutions
     * @param predecessor - The predecessor
     * @return - True IFF the state is valid
     */
    private boolean isStateValid(ALSSLRTAStarNode node,Set<Prefix> solutions,ALSSLRTAStarNode predecessor)
    {
        // TODO: 02/03/2020 Check Map  
        if(Problem.getInstance().isValidLocation(node.getNode().getCoordinates()))
            return checkForCollisions( node, solutions) && checkForSwipes( node, predecessor,solutions);
        return false;
    }

    /**
     * This function will return if in this state the agent will not swipe with other agents
     * @param node - The given state
     * @param solutions - The prev solutions
     * @param predecessor - The predecessor
     * @return - True IFF the agent will not swipe with other agents
     */
    private boolean checkForSwipes(ALSSLRTAStarNode node,ALSSLRTAStarNode predecessor, Set<Prefix> solutions) {


        int timeStamp = node.getTimeStamp();
        if(timeStamp == 0)
            return true;
        Node actualNode = node.getNode();
        Node prevNode = predecessor.getNode();
        for(Prefix sol : solutions)
        {
            if(sol.getNodeAt(timeStamp).equals(actualNode)) {
                if(sol.getNodeAt(timeStamp-1).equals(prevNode))
                    return false;
            }
        }
        return true;

    }

    /**
     * This function will return if in this state the agent will not collide with other agents
     * @param node - The given state
     * @param solutions - The prev solutions
     * @return - True IFF the agent will not collide with other agents
     */
    private boolean checkForCollisions(ALSSLRTAStarNode node, Set<Prefix> solutions) {
        int timeStamp = node.getTimeStamp();
        Node actualNode = node.getNode();

        for(Prefix sol : solutions)
        {
            if(sol.getNodeAt(timeStamp).equals(actualNode))
                return false;
        }

        return true;
    }

    /**
     * This function will return the g value of the node
     * @param node - The g value of the node
     * @return - The g value of the node
     */
    private double getGValue(ALSSLRTAStarNode node)
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
    private double getHValue(ALSSLRTAStarNode node)
    {
        return this.heuristic.getHeuristic(node.getNode(),this.goal,agent);
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
     * @param node - The h value of the node
     * @return - The h value of the node
     */
    private double getInitialHValue(Node node)
    {
        return this.heuristic.getHeuristic(node,this.goal);
    }

    /**
     * This function will return the h value of the node
     * @param node - The f value of the node
     * @return - The f value of the node
     */
    private double getFValue(ALSSLRTAStarNode node)
    {
        return getGValue(node) + getHValue(node);
    }

    /**
     * This function will set the g value for the
     * @param node - The given node
     * @param newVal - The new gVal
     */
    private void setGValue(ALSSLRTAStarNode node, double newVal)
    {
        this.nodeToGValue.put(node,newVal);
    }

    /**
     * This function will set the h value for the
     * @param node - The given node
     * @param newVal - The new hVal
     */
    private void setHValue(ALSSLRTAStarNode node, double newVal)
    {
        this.heuristic.storeNewVal(agent,node.getNode(),goal,newVal);
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
    /**
     * This function will return if the given node is a goal node
     * @param node - The given node
     * @return - True IFF the given node is the goal node
     */
    private boolean isGoal(Node node)
    {
        return this.goal.equals(node);
    }

    /**
     * This function will add to the open queue the given node
     * @param node - The node
     * @param openList - The open list
     */
    private void addToOpenList(ALSSLRTAStarNode node,PriorityQueue<ALSSLRTAStarNode> openList)
    {
        if(!this.inQueue.contains(node))
        {
            this.inQueue.add(node);
            openList.add(node);
        }
        else
        {
            openList.remove(node);
            openList.add(node);
        }
    }

    /**
     * This function will dequeue the queue
     * @param openList - The openList
     * @return - The node
     */
    private ALSSLRTAStarNode dequeueOpenList(PriorityQueue<ALSSLRTAStarNode> openList)
    {

        ALSSLRTAStarNode node = openList.poll();
        this.inQueue.remove(node);
        return node;
    }
    /**
     * This function will mark the node as updated
     * @param node - The given node
     */
    private void updateNode(Node node)
    {

        if(!this.updated.containsKey(agent))
            this.updated.put(agent,new HashSet<>());

        Set<Node>updatedAgents = this.updated.get(agent);
        updatedAgents.add(node);
    }

    /**
     * This function will un mark a node as updated
     * @param node - The given node
     */
    private void unUpdateNode(Node node)
    {
        this.updated.remove(node);
    }

    /**
     * This function will check if the node is marked as updated or not
     * @param node - The given node
     * @return - True IFF the node is marked as updated
     */
    private boolean isNodeUpdated(Node node)
    {
        if(!this.updated.containsKey(agent))
            return false;

        Set<Node> updatedAgents = this.updated.get(agent);

        return updatedAgents.contains(node);
    }

    /**
     * This function will set the given node's predecessor
     * @param node - The given node
     * @param predecessor - The predecessor
     */
    private void setPredecessor(ALSSLRTAStarNode node , ALSSLRTAStarNode predecessor)
    {
        this.predMap.put(node,predecessor);
    }

    /**
     * This function will return the predecessor of the given node
     * @param node - The given node
     * @return - The predecessor of the given node
     */
    private ALSSLRTAStarNode getPredecessor(ALSSLRTAStarNode node)
    {
        return this.predMap.get(node);
    }
    /**
     * This class will compare between two Nodes (the node with the minimum F value is first)
     */
    public class AStartFValueNodeComparator implements Comparator<ALSSLRTAStarNode>
    {



        @Override
        public int compare(ALSSLRTAStarNode node1, ALSSLRTAStarNode node2) {

            double f1 = getFValue(node1);
            double f2 = getFValue(node2);

            if(f1<f2)
                return -1;
            if(f1>f2)
                return 1;

            //The F value is equal

            boolean isGoal1 = isGoal(node1.getNode());
            boolean isGoal2 = isGoal(node2.getNode());
            if(isGoal1 && ! isGoal2)
                return -1;

            if(!isGoal1 && isGoal2)
                return 1;

            int timeStamp1 = node1.getTimeStamp();
            int timeStamp2 = node2.getTimeStamp();

            return timeStamp1 - timeStamp2;

        }
    }

    /**
     * This class will compare between two Nodes (the node with the minimum F value is first) including consideration in the updated sign
     */
    public class AStartFValueUpdatedNodeComparator extends AStartFValueNodeComparator
    {
        private int prefixSize;
        public AStartFValueUpdatedNodeComparator(int prefixSize)
        {
            this.prefixSize = prefixSize;
        }
        @Override
        public int compare(ALSSLRTAStarNode node1, ALSSLRTAStarNode node2) {

            int t1 = node1.getTimeStamp();
            int t2 = node2.getTimeStamp();

            if(t2<prefixSize - 1 && t1==prefixSize-1)
                return -1;
           if(t1<prefixSize - 1 && t2==prefixSize-1)
               return 1;
           boolean isUpdated1 = isNodeUpdated(node1.getNode());
           boolean isUpdated2 = isNodeUpdated(node2.getNode());

           if(!isUpdated1 && isUpdated2)
               return -1;

            if(isUpdated1 && !isUpdated2)
                return 1;

            int superScore = super.compare(node1,node2);
            if(superScore!=0)
                return superScore;

            double h1 = getHValue(node1);
            double h2 = getHValue(node2);

            if(h1<h2)
                return -1;
            if(h1>h2)
                return 1;
            return 0;
        }
    }

    /**
     * This class will compare between two Nodes (the node with the minimum H value is first)
     */
    public class HValueNodeComparator implements Comparator<Node>
    {
        @Override
        public int compare(Node node1, Node node2) {
            double h1 = getHValue(node1);
            double h2 = getHValue(node2);

            if(h1<h2)
                return -1;
            if(h1>h2)
                return 1;
            return 0;
        }
    }
}
