package Components;

import java.util.Objects;

public class Agent {
    public static int numberOfExistingAgents = 0;
    private int id;
    private Node start;
    private Node end;
    private int priority;

    public Agent(Node start, Node end,int priority) {

        this.id = numberOfExistingAgents;
        numberOfExistingAgents++;
        this.start = start;
        this.end = end;
        this.priority = priority;
    }
    public Agent(Node start, Node end)
    {
        this(start,end,-1);
    }
    public Agent()
    {
        this(null,null);
    }

    public static int getNumberOfExistingAgents() {
        return numberOfExistingAgents;
    }

    public static void setNumberOfExistingAgents(int numberOfExistingAgents) {
        Agent.numberOfExistingAgents = numberOfExistingAgents;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Node getStart() {
        return start;
    }

    public void setStart(Node start) {
        this.start = start;
    }

    public Node getEnd() {
        return end;
    }

    public void setEnd(Node end) {
        this.end = end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Agent agent = (Agent) o;
        return id == agent.id &&
                Objects.equals(start, agent.start) &&
                Objects.equals(end, agent.end);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, start, end);
    }
}
