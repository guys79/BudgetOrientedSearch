package Components;

import Components.BoundedSingleSearchAlgorithms.ALSSLRTAStarNode;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * This class represents an agent
 */
public class Agent {
    private static int numberOfExistingAgents = 0;//The number of agents
    private int id;//The id of the agent
    private Node start;//The start node
    private Node goal;//The goal node
    private int numOfBadPoints;//The number of bad points
    private Set<ALSSLRTAStarNode> leaves;
    private Set<Agent> problematicAgents;


    /**
     * The constructor of the agent
     *
     * @param start - The start node
     * @param goal  - The goal node
     */
    public Agent(Node start, Node goal) {

        this.id = numberOfExistingAgents;
        numberOfExistingAgents++;
        this.start = start;
        this.goal = goal;
        this.numOfBadPoints = 0;
        this.leaves = null;
        this.problematicAgents = new HashSet<>();

    }

    /**
     * The constructor
     *
     * @param start - The start node
     * @param goal  - The goL node
     * @param id    - The id of the agent
     */
    public Agent(Node start, Node goal, int id) {

        this.id = id;
        this.leaves = null;
        this.start = start;
        this.goal = goal;
        this.problematicAgents = new HashSet<>();

    }

    /**
     * This function will set the problematic agents for this agent inn this iteration
     * @param problematicAgents - The problematic agent
     */
    public void setProblematicAgents(Set<Agent> problematicAgents) {

        this.problematicAgents = problematicAgents;
    }

    /**
     * This function will return the problematic agents for this agent in this iteration
     * @return - The problematic agents for this agent in this iteration
     */
    public Set<Agent> getProblematicAgents() {

        return problematicAgents;
    }

    /**
     * This function will set the leaves from the search tree
     */
    public void setLeaves(Set<ALSSLRTAStarNode> leaves) {
        this.leaves = leaves;
    }

    /**
     * This function will get the leaves from the search tree
     * @return  - The leaves from the search tree
     */
    public Set<ALSSLRTAStarNode> getLeaves() {
        return leaves;
    }

    /**
     * This function will add a bad point to the agent
     */
    public void addBadPoint() {
        this.numOfBadPoints++;
    }

    /**
     * This function will return the number of bad points
     *
     * @return - The number of bad points
     */
    public int getNumOfBadPoints() {
        return numOfBadPoints;
    }

    public void resetBadPoints() {
        numOfBadPoints = 0;
    }

    public static void restNumOfAgents() {
        numberOfExistingAgents = 0;
    }

    /**
     * The empty constructor
     */
    public Agent() {
        this(null, null);
    }

    /**
     * This function will nreturn the number of existing agents
     *
     * @return - The number of existing agents
     */
    public static int getNumberOfExistingAgents() {
        return numberOfExistingAgents;
    }

    /**
     * This function will return the id of the agent
     *
     * @return - The id of the agent
     */
    public int getId() {
        return id;
    }

    /**
     * This funcion will set the agent's id
     *
     * @param id - The agent's id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * This function will return the start node
     *
     * @return - The start node
     */
    public Node getStart() {
        return start;
    }

    /**
     * This function will set the start node
     *
     * @param start - The start node
     */
    public void setStart(Node start) {
        this.start = start;
    }

    /**
     * This function will return the goal njode
     *
     * @return - The goal node
     */
    public Node getGoal() {
        return goal;
    }

    /**
     * This function will set the goal node
     *
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

    @Override
    public String toString() {
        String str = "agent id " + id + " start - " + start + " goal - " + goal;
        return str;
    }
}
