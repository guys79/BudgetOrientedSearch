package Components;

import java.util.Objects;

/**
 * This class represents an agent
 */
public class Agent {
    private static int numberOfExistingAgents = 0;//The number of agents
    private int id;//The id of the agent
    private Node start;//The start node
    private Node goal;//The goal node

    /**
     * The constructor of the agent
     * @param start - The start node
     * @param goal - The goal node
     */
    public Agent(Node start, Node goal) {

        this.id = numberOfExistingAgents;
        numberOfExistingAgents++;
        this.start = start;
        this.goal = goal;

    }

    /**
     * The empty constructor
     */
    public Agent()
    {
        this(null,null);
    }

    /**
     * This function will nreturn the number of existing agents
     * @return - The number of existing agents
     */
    public static int getNumberOfExistingAgents() {
        return numberOfExistingAgents;
    }

    /**
     * This function will return the id of the agent
     * @return - The id of the agent
     */
    public int getId() {
        return id;
    }

    /**
     * This funcion will set the agent's id
     * @param id - The agent's id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * This function will return the start node
     * @return - The start node
     */
    public Node getStart() {
        return start;
    }

    /**
     * This function will set the start node
     * @param start - The start node
     */
    public void setStart(Node start) {
        this.start = start;
    }

    /**
     * This function will return the goal njode
     * @return - The goal node
     */
    public Node getGoal() {
        return goal;
    }

    /**
     * This function will set the goal node
     * @param goal - The goal node
     */
    public void setGoal(Node goal) {
        this.goal = goal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Agent agent = (Agent) o;
        return id == agent.id &&
                Objects.equals(start, agent.start) &&
                Objects.equals(goal, agent.goal);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, start, goal);
    }
}
