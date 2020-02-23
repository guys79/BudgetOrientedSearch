package Components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Node {

    public static  int numOfNodes = 0;
    private int id;
    private int[] coordinates;
    private List<Node> neighbors;

    public Node(int[] coordinates) {

        this.id = numOfNodes;
        numOfNodes++;
        this.coordinates = coordinates;
        this.neighbors = new ArrayList<>();
    }
    public Node()
    {
        this(null);
    }

    public int getCoordinateAt(int index)
    {
        return this.coordinates[index];
    }
    public static int getNumOfNodes() {
        return numOfNodes;
    }

    public static void setNumOfNodes(int numOfNodes) {
        Node.numOfNodes = numOfNodes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(int[] coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return id == node.id &&
                Arrays.equals(coordinates, node.coordinates);
    }

    @Override
    public int hashCode() {

        int result = Objects.hash(id);
        result = 31 * result + Arrays.hashCode(coordinates);
        return result;
    }

    public int getNumOfDimensions()
    {
        return this.coordinates.length;
    }
    public void addNeighbor(Node neigh)
    {
        this.neighbors.add(neigh);
    }

    public List<Node> getNeighbors() {
        return neighbors;
    }
}
