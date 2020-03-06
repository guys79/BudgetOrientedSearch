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


        if(current.equals(goal))
        {
            boolean needToSearch = false;
            for (int i = 1; i < prefixSize; i++) {
                if (!checkForCollisions(new ALSSLRTAStarNode(current, i), solutions)) {
                    needToSearch = true;
                    break;
                }
            }
            if(!needToSearch)
            {
                List<Node> sol = new ArrayList<>();
                for(int i=0;i<prefixSize;i++)
                {
                    sol.add(current);
                }
                Prefix solP = new Prefix(sol,agent);
                return new Pair<>(solP,budget-1);
            }
        }

        PriorityQueue<ALSSLRTAStarNode> openList = new PriorityQueue<>(new AStartFValueNodeComparator());
        Set<Node> closed = new HashSet<>();

        //The A* procedure
        int remainBudget = AStarProcedure(current,openList,budget,closed,prefixSize,solutions);
      // System.out.println("Remaining Budget "+remainBudget);
        //No solution
        if(openList == null || openList.size() == 0)
        {
            System.out.println("Agent "+agent.getId()+" couldn't find a state to be on");
            return new Pair<>(null,remainBudget);
        }


        //Get the best Node
        ALSSLRTAStarNode best = getBestState(openList,prefixSize);
      //  System.out.println("F - "+getFValue(best));
        try {
            openList.add(best);
        }
        catch (Exception e)
        {
            System.out.println(best);
            System.out.println(openList);
            throw e;
        }
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
        Map<Node,Integer> openListMap = new HashMap<>();//Key - the node, Value - number of occurrences of the node in the open list (different times)

        ALSSLRTAStarNode currentNode = new ALSSLRTAStarNode(current,0);//Create the current node with the time stamp of 0
        openListMap.put(current,1);//Update number of occurrences of node in open list
        setGValue(currentNode,0);//Set the G value of the node to be 0
        addToOpenList(currentNode,openList);//Add the current node to the open list

        int expansions = 0;//Number of expansions
        Set<Node> neighbors;//The neighbor set of the dequeued node
        double neighborGValue;//The G value of the neighbor of the dequeued node
        double costFromCurrentToNeighbor;//The cost from the dequeued node to the neighbor
        double pathCostFromCurrentToNeighbor;//The path cost from the current node through the dequeued node to the neighbor
        double currentGValue;//The G value of the dequeued node
        int currentTimeStamp;//The time stamp of the dequeued node
        int sizePrev;//The size of the close set before adding the dequeued node
        int sizeAfter;//The size of the close set after adding the dequeued node
        ALSSLRTAStarNode neighborNode;//The neighbor of the dequeued node
        Set<ALSSLRTAStarNode> rest = new HashSet<>();//The nodes with timestamp of prefix - 1 that were dequeued from the open list and can still be candidates for the best state
        int restSize = 0;//The sze of unique states in the openList (and rest set)
        ALSSLRTAStarNode lastNode = null;//The unique node
        int numOfOccur;//The number of occurrences of the node in the open list (different times)


        //While:
        //1. The open list is not empty
        //2. The number of expansions is smaller than the budget (while there is still remaining budget)
        //3. While the unique states in the open list and the close list are not the same
        while(openList.size()>0 && expansions<budget && (restSize = getUniqueOpenSize(openListMap,rest)) !=closed.size())
        {

           /* if(agent.getId() == 6 && PerformanceTracker.getInstance().getNumberOFIteration() == 50) {
                System.out.println();
                System.out.println(openListMap.get(current));
                for (ALSSLRTAStarNode node : openList) {
                    System.out.println(node + " F - " + getFValue(node) +" Pred -"+getPredecessor(node));
                }
                System.out.println();
                System.out.println();

            }*/

            currentNode = dequeueOpenList(openList);//Dequeue a node from the open list (Minimal F value)
           /* if(agent.getId() == 6 && PerformanceTracker.getInstance().getNumberOFIteration() == 50) {
                System.out.println("Current -" + currentNode);
            }*/
            numOfOccur = openListMap.get(currentNode.getNode());//Get the number of occurrences of the node in the open list

            //Remove node from open list
            if(numOfOccur == 1)
                openListMap.remove(currentNode.getNode());
            else
            {
                numOfOccur--;
                openListMap.put(currentNode.getNode(),numOfOccur);
            }

            //Add to the number of expansions +1
            expansions++;

            //If the node is the goal node, stop the search
            if(isGoal(currentNode.getNode())) {
                openList.add(currentNode);
                return budget - expansions;
            }

            //Add to close list
            sizePrev = closed.size();
            closed.add(currentNode.getNode());
            sizeAfter = closed.size();

            if(sizeAfter>sizePrev)//If the size of the close list has changed
                lastNode = currentNode;


            //Expend node
            currentTimeStamp = currentNode.getTimeStamp();

            //Will not develop further than the prefix size
            if(currentTimeStamp < prefixSize-1) {

                neighbors = currentNode.getNode().expend();//Expand nodes
                currentGValue = getGValue(currentNode);//Get the g value of the current node

                //For each neighbor
                for (Node neighbor : neighbors) {
                   /* int [] o = {21,16};
                    if(agent.getId() == 26 && neighbor.equals(new Node(o)) && currentTimeStamp +1 ==4) {
                        System.out.println();
                    }*/

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

                            if(!openListMap.containsKey(neighbor))
                                openListMap.put(neighbor,1);
                            else
                            {
                                numOfOccur = openListMap.get(neighbor) + 1;
                                openListMap.put(neighbor,numOfOccur);

                            }

                        }

                    }

                }

            }
            else
            {
                //Add node to the rest set
                rest.add(currentNode);

            }

        }
       /* int [] o = {182,111};
        if(agent.getId() == 6 && PerformanceTracker.getInstance().getNumberOFIteration() == 50 )
            System.out.println();*/
        //If the while stopped because the unique states in the open list and the close list ARE the same
        if(restSize == closed.size())
        {
       //     System.out.println("rest Size");
            closed.remove(lastNode.getNode());
        }

        //Add the rest nodes to the open list
        for(ALSSLRTAStarNode node : rest)
        {
            addToOpenList(node,openList);
        }

        return budget - expansions;

    }

    /**
     * This function will return the amount of unique states in the open list and in the rest set
     * @param openMap - The open map
     * @param rest - The rest set
     * @return - The amount of unique states in the open list and in the rest set
     */
    private int getUniqueOpenSize(Map<Node,Integer> openMap, Set<ALSSLRTAStarNode> rest)
    {
        int size = openMap.size();
        for(ALSSLRTAStarNode node : rest)
        {
            if(!openMap.containsKey(node.getNode()))
                size++;
        }
        return size;
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


        PriorityQueue<Node> openListOrderedByHVal = new PriorityQueue<>(new HValueNodeComparator());
      //  Set<Node> openSet = new HashSet<>();
        Set<Node> openNotInCloseSet = new HashSet<>();
        Node toOpen;

        //Get the openList states (NOT including time).
        //The states that are not in the close list
        for (ALSSLRTAStarNode node : openList) {
            toOpen = node.getNode();
           // openSet.add(toOpen);
            if(!closeList.contains(toOpen)) {
                openNotInCloseSet.add(toOpen);
  //              System.out.println(toOpen+" F - "+getFValue(node));
            }
        }

        if(openNotInCloseSet.size() == 0) {
            System.out.println("wat");
            return;
        }

        openListOrderedByHVal.addAll(openNotInCloseSet);
        Set<Node> toDelete = new HashSet<>();

        //Update closeList nodes
        for(Node node : closeList) {

            this.setHValue(node, Double.MAX_VALUE);
            toDelete.add(node);
        }

        Node current;
        Set<Node> neighbors;
        double hVal,initialHVal,hNeighbor,hValThrpughCurrent;

        while(closeList.size()>0)
        {

            current = openListOrderedByHVal.poll();

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
                           // System.out.println("hello");
                            toDelete.remove(neighbor);
                            openListOrderedByHVal.add(neighbor);
                        }
                    }

            }

        }
        if(toDelete.size()!=0)
            System.out.println("DaFack");
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
        return checkForCollisions( node, solutions) && checkForSwipes( node, predecessor,solutions);
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
            if(sol.getNodeAt(timeStamp).equals(prevNode)) {
                if(sol.getNodeAt(timeStamp-1).equals(actualNode))
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
            /*if(agent.getId() == 26 && sol.getAgent().getId() == 79)
                System.out.println();*/
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
     /*   int [] o = {181,110};
        if(agent.getId() == 6 && PerformanceTracker.getInstance().getNumberOFIteration() == 50 && node.getNode().equals(new Node(o)))
            System.out.println();*/
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
       // int [] o = {182,111};
        /*if(agent.getId() == 6 && PerformanceTracker.getInstance().getNumberOFIteration() == 50 && node.getNode().equals(new Node(o)))
            System.out.println();*/
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


            if(agent.getId() == 6 && PerformanceTracker.getInstance().getNumberOFIteration() == 50) {
               /*int [] o1 = {183,112};
                int [] o2 = {183,110};
                if(node1.getNode().equals(new Node(o1)) &&node2.getNode().equals(new Node(o2)))
                {
                    System.out.println(node1+"F - "+getFValue(node1)+"G - "+getGValue(node1)+"H - "+getHValue(node1));
                    System.out.println(node2+"F - "+getFValue(node2)+"G - "+getGValue(node2)+"H - "+getHValue(node2));
                    System.out.println();
                }
                if(node2.getNode().equals(new Node(o1)) &&node1.getNode().equals(new Node(o2)))
                {
                    System.out.println(node1+"F - "+getFValue(node1)+"G - "+getGValue(node1)+"H - "+getHValue(node1));
                    System.out.println(node2+"F - "+getFValue(node2)+"G - "+getGValue(node2)+"H - "+getHValue(node2));
                    System.out.println();
                }
                System.out.println();
                System.out.println(node1+"F - "+getFValue(node1)+"G - "+getGValue(node1)+"H - "+getHValue(node1));
                System.out.println(node2+"F - "+getFValue(node2)+"G - "+getGValue(node2)+"H - "+getHValue(node2));
                System.out.println();*/

            }
            int maxDigitNum = 14;
            long numberReducer = (long)Math.pow(10,maxDigitNum);
            double f1 = ((long)(getFValue(node1)*numberReducer))/numberReducer;
            double f2 = ((long)(getFValue(node2)*numberReducer))/numberReducer;

            //double f2 = getFValue(node2);

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

/*
             double g1 = getGValue(node1);
             double g2 = getGValue(node1);

            if(g1>g2)
                return -1;
            if(g1<g2)
                return 1;
*/
            boolean isSameAsBack1 = getPredecessor(node1).getNode().equals(node1.getNode());
            boolean isSameAsBack2 = getPredecessor(node2).getNode().equals(node2.getNode());

            if(!isSameAsBack1 && isSameAsBack2)
                return -1;

            if(isSameAsBack1 && !isSameAsBack2)
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
