package Components.Heuristics;

import Components.Agent;
import Components.CostFunction.ICostFunction;
import Components.Node;
import Components.ParamConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class PreComputedUniformCostSearch extends HeuristicWithPersonalDatabase
{

    private Map<Node, Double> costs;

    @Override
    public String toString() {
        return "PreComputedUniformCostSearch";
    }

    /**
     * This constructor
     *
     * @param heuristic - The heuristic function
     */
    public PreComputedUniformCostSearch(IHeuristic heuristic) {
        super(heuristic);
        this.preCompute = true;
    }

    @Override
    protected Map<Node, Double> preCompute(Node dest) {
        costs = new HashMap<>();
        //The cost function
        ICostFunction costFunction = ParamConfig.getInstance().getCostFunction();

        PriorityQueue<Node> priorityQueue = new PriorityQueue<>(new UniformCostSearch.UniformCostSearchNodeComparator(costs));

        //Putting the start state in the priority queue with the value of 0
        putInQueue(priorityQueue,costs,dest,0);

        Node current;
        //While the queue is not empty
        while(priorityQueue.size()>0)
        {
            //Dequeue the node
            current = priorityQueue.poll();


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
        System.out.println(costs.size());
        return costs;

    }

    /**
     * This function will put a node in the queue and update its cost
     * @param priorityQueue - The given priority queue
     * @param costs - The given costs map
     * @param node - The given node
     * @param cost - The given cost
     */
    private void putInQueue(PriorityQueue<Node> priorityQueue,Map<Node,Double> costs,Node node,double cost)
    {
        costs.put(node,cost);
        priorityQueue.add(node);
    }
}
