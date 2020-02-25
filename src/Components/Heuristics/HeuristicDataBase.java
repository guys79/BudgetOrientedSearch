package Components.Heuristics;

import Components.Agent;
import Components.Node;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * This class represents a database for a single agent
 */
public class HeuristicDataBase {

    private int id; // The id of the dataBase
    private Map<Node,Double> dataBaseValues; //The stored Values for the node's heuristics for a single agent
    private Node destination;//The destination of the agent
    /**
     * The constructor of the class
     * @param agent - The agent
     * @param dest - The destination of the agent
     */
    public HeuristicDataBase(Agent agent,Node dest)
    {
        this.id = agent.getId();
        this.dataBaseValues = new HashMap<>();
        this.destination = dest;
    }

    /**
     * This function will return weather the node exists in the database
     * @param node - The given node
     * @return - Weather the node exists in the database
     */
    public boolean isNodeStoredInDataBase(Node node)
    {
        return this.dataBaseValues.containsKey(node);
    }

    /**
     * This function will return the stored heuristic for the node
     * @param node - The given node
     * @return - The stored heuristic for the node
     */
    public Double getStoredValueForNode(Node node)
    {
        return this.dataBaseValues.get(node);
    }

    /**
     * This function will store the value for the given node. If the node already exists in the database, the new value will override the old one
     * @param node - The given node
     * @param value - The given value
     */
    public void storeValue(Node node,double value)
    {
        this.dataBaseValues.put(node,value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HeuristicDataBase that = (HeuristicDataBase) o;
        return id == that.id &&
                Objects.equals(dataBaseValues, that.dataBaseValues);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, dataBaseValues);
    }

    /**
     * This function will get the id of the database
     * @return - The id of the database
     */
    public int getId() {
        return id;
    }

    /**
     * This function will return the destination node
     * @return - The destination node
     */
    public Node getDestination() {
        return destination;
    }
}
