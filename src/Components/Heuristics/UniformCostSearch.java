package Components.Heuristics;

import Components.CostFunction.ICostFunction;
import Components.Node;
import Components.ParamConfig;

import java.util.*;

/**
 * This class represents a Uniform Cost Search for a single agent
 */
public class UniformCostSearch implements IHeuristic {

    private HashMap<Node,Double> costs;//The costs

    /**
     * The constructor
     */
    public UniformCostSearch()
    {
        costs = new HashMap<>();
    }

    /**
     * This function will get the costs map
     * @return - The costs map
     */
    public HashMap<Node, Double> getCosts() {
        return costs;
    }

    @Override
    public double getHeuristic(Node node, Node dest) {


        //The cost function
        ICostFunction costFunction = ParamConfig.getInstance().getCostFunction();

        PriorityQueue<Node> priorityQueue = new PriorityQueue<>(new UniformCostSearchNodeComparator(costs));

        //Putting the start state in the priority queue with the value of 0
        putInQueue(priorityQueue,costs,node,0);

        Node current;
        //While the queue is not empty
        while(priorityQueue.size()>0)
        {
            //Dequeue the node
            current = priorityQueue.poll();

            //If the node is the goal node
            if(current.equals(dest))
            {
                return costs.get(current);
            }

            Set<Node> neighbors = current.expend();
            double currCost = costs.get(current);
            double newCost;

            //Add all the neighbors that we have not reached to yet
            for (Node neigh : neighbors)
            {
                //If we have not reached the neighbor before
                if(!costs.containsKey(neigh)) {
                    newCost = currCost + costFunction.getCost(current,neigh);
                    putInQueue(priorityQueue, costs, neigh, newCost);
                }
            }

        }

        //If we couldn't find the node
        return -1;

    }

    /**
     * This function will put a node in the queue and update its cost
     * @param priorityQueue - The given priority queue
     * @param costs - The given costs map
     * @param node - The given node
     * @param cost - The given cost
     */
    private void putInQueue(PriorityQueue<Node> priorityQueue,HashMap<Node,Double> costs,Node node,double cost)
    {
        costs.put(node,cost);
        priorityQueue.add(node);
    }

    /**
     * The comparator of the nodes. The node with the lower cost gets the higher priority
     */
    static class UniformCostSearchNodeComparator implements Comparator<Node>
    {
        private Map<Node,Double> costs;

        /**
         * The constructor
         * @param costs - The costs map
         */
        public UniformCostSearchNodeComparator(Map<Node,Double> costs)
        {
            this.costs = costs;
        }

        @Override
        public int compare(Node o1, Node o2) {
            Double cost1 = this.costs.get(o1);
            Double cost2 = this.costs.get(o2);
            if(cost1 == null && cost2 ==null)
                return 0;
            if(cost1 == null && cost2 !=null)
                return 1;
            if(cost1 != null && cost2 ==null)
                return -1;
            if(cost1-cost2>0)
                return 1;
            if(cost1-cost2<0)
                return -1;
            return 0;
        }
    }
}
