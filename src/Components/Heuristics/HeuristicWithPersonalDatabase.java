package Components.Heuristics;

import Components.Agent;
import Components.Node;
import Components.PerformanceTracker;
import Components.Problem;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a heuristic with database
 */
public class HeuristicWithPersonalDatabase  implements IHeuristic{

    private Map<Agent,HeuristicDataBase> databasesForAgents; //The key is the agent, the value is the agent's database
    private IHeuristic heuristic;//The heuristic
    protected boolean preCompute;//True if we want to pre-compute the heuristics

    /**
     * This constructor
     * @param heuristic - The heuristic function
     */
    public HeuristicWithPersonalDatabase(IHeuristic heuristic)
    {
        this.heuristic = heuristic;
        this.databasesForAgents = new HashMap<>();
        this.preCompute = false;
    }


    @Override
    public double getHeuristic(Node node, Node dest) {

        return this.heuristic.getHeuristic(node,dest);
    }

    @Override
    public double getHeuristic(Node node, Node dest, Agent agent) {
        double val=-100;
        boolean flag = false;
        HeuristicDataBase dataBase;
        if(this.databasesForAgents.containsKey(agent))
        {

            dataBase = this.databasesForAgents.get(agent);
            if(!dest.equals(dataBase.getDestination()))
                throw new UnsupportedOperationException("The destination of this agent is not as received");

            if(dataBase.isNodeStoredInDataBase(node))
            {
          //      System.out.println("Get val from dataBase");
                val = dataBase.getStoredValueForNode(node);
                flag = true;
            }

        }
        else
        {
            dataBase = new HeuristicDataBase(agent,dest);
            databasesForAgents.put(agent,dataBase);
        }
        if(!flag)
        {
            if(this.preCompute)
            {

                long before = System.currentTimeMillis();
                System.out.println("PreComputing.. "+(PerformanceTracker.getInstance().getNumOfPreComputedAgents()+1)+"/"+Problem.getInstance().getNumOfAgents());
                Map<Node,Double> nodeToHeuristic = preCompute(dest);
                PerformanceTracker.getInstance().addNumOfPreComputedAgents();
                long after = System.currentTimeMillis();
                PerformanceTracker.getInstance().addPreCompute(after-before);
                for(Map.Entry<Node,Double> entry : nodeToHeuristic.entrySet())
                {
                    dataBase.storeValue(entry.getKey(),entry.getValue());
                }
                val = dataBase.getStoredValueForNode(node);
            }
            else {
                val = this.getHeuristic(node, dest);
                dataBase.storeValue(node, val);
            }
        }
        return val;
    }

    /**
     * This function will store a new value for the node for the given agent
     * @param agent - The given agent
     * @param node - The given node
     * @param val - The given value
     * @param dest - The destination node
     */
    public void storeNewVal(Agent agent,Node node,Node dest,double val)
    {
        HeuristicDataBase dataBase;
        if(this.databasesForAgents.containsKey(agent))
            dataBase = this.databasesForAgents.get(agent);
        else
        {
            dataBase = new HeuristicDataBase(agent,dest);
            this.databasesForAgents.put(agent,dataBase);
        }
        dataBase.storeValue(node,val);
    }

    /**
     * This function will pre compute the heuristics of the nodes to the destination
     * @param dest - The destination
     * @return - The heuristics of the nodes to the destination
     */
    protected Map<Node,Double> preCompute(Node dest)
    {
        return null;
    }
}
