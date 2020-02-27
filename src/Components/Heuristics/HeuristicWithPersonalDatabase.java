package Components.Heuristics;

import Components.Agent;
import Components.Node;

import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a heuristic with database
 */
public class HeuristicWithPersonalDatabase  implements IHeuristic{

    private Map<Agent,HeuristicDataBase> databasesForAgents; //The key is the agent, the value is the agent's database
    private IHeuristic heuristic;//The heuristic

    /**
     * This constructor
     * @param heuristic - The heuristic function
     */
    public HeuristicWithPersonalDatabase(IHeuristic heuristic)
    {
        this.heuristic = heuristic;
        this.databasesForAgents = new HashMap<>();
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
                throw new UnsupportedOperationException("The destination of this agent is not as recived");

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
  //          System.out.println("New val");
            val = this.getHeuristic(node,dest);
            dataBase.storeValue(node,val);
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
}
