package Components;

import java.util.*;

/**
 * This class represent a node in the graph
 */
public class Node {

    private static  int numOfNodes = 0;//The number of nodes
    private int id;//The id of the nodes
    private int[] coordinates;//The coordination of the node


    /**
     * The constructor of the class
     * @param coordinates - The coordinates of the node
     */
    public Node(int[] coordinates) {

        this.id = numOfNodes;
        numOfNodes++;
        this.coordinates = coordinates;

    }

    /**
     * The empty constructor
     */
    public Node()
    {
        this(null);
    }

    /**
     * This function will return the I'th coordinate of the node
     * @param index - The index of the coordination
     * @return - The coordinate in the I'th location
     */
    public int getCoordinateAt(int index)
    {
        return this.coordinates[index];
    }

    /**
     * This function will return the number of nodes
     * @return - The number of nodes
     */
    public static int getNumOfNodes() {
        return numOfNodes;
    }

    /**
     * This function will return the id of the node
     * @return - The id of the node
     */
    public int getId() {
        return id;
    }

    /**
     * This function will return the coordinates of the node
     * @return - The coordinates of the node
     */
    public int[] getCoordinates() {
        return coordinates;
    }

    /**
     * This function will sett he coordinates of the node
     * @param coordinates - The coordinates of the node
     */
    public void setCoordinates(int[] coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Arrays.equals(coordinates, node.coordinates);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(coordinates);
    }

    /**
     * This function will return the number of dimensions the node represents
     * @return - The number of dimensions the node represents
     */
    public int getNumOfDimensions()
    {
        return this.coordinates.length;
    }

    /**
     * This function check if the given node is a neighbor of the current node
     * @param node - The given node
     * @return - True IFF the nodes are neighbors
     */
    public boolean isNeighbor(Node node)
    {
        int axis = ParamConfig.getInstance().getNumberOfAxisLegalToMoveInOneTurn();
        int sum = 0;

        int [] otherCo = node.coordinates;
        int c1,c2,diff;
        for(int i=0;i<otherCo.length;i++)
        {
            c1 = this.coordinates[i];
            c2 = otherCo[i];
            diff = Math.abs(c1-c2);
            if(diff>1)
                return false;
            sum+=diff;
        }
        if(sum>axis)
            return false;
        return true;
    }
    /**
     * This function will expand the node
     * @return - The neighbors of the node
     */
    public Set<Node> expend()
    {
        Set<Node> neighbors = new HashSet<>();
        int axis = ParamConfig.getInstance().getNumberOfAxisLegalToMoveInOneTurn();
        int [] size = Problem.getInstance().getSize();
        for(int i=0;i<=axis;i++)
        {
            generateAllNeighbors(0,"",neighbors,i,size);
        }
        return neighbors;
    }

    /**
     * This function will generate and return the neighbors of the node recursively
     * @param index - The index in the array of coordinates
     * @param additions - The additions in the coordinates
     * @param neighbors - The neighbors set
     * @param axisAllowed - The nu,ber od axis the agent can move at the same time
     * @param size - The sizes of the graph
     */
    private void generateAllNeighbors(int index, String additions, Set<Node> neighbors, int axisAllowed,int [] size)
    {
        if(axisAllowed == 0)
        {
            int [] coordinates = new int[this.coordinates.length];
            int addLength = additions.length();
            for(int i=0;i<addLength;i++)
            {
                char c = additions.charAt(i);
                if(c=='2')
                {
                    coordinates[i] = this.coordinates[i]-1;
                }
                else {
                    coordinates[i] = this.coordinates[i] + (c - '0');
                }
                if(size[i]<=coordinates[i] || coordinates[i]<0)//Ilegal
                    return;

            }
            for(int i=addLength;i<this.coordinates.length;i++)
            {
                coordinates[i] = this.coordinates[i];
            }
            if(!Problem.getInstance().isValidLocation(coordinates))
                return;
            neighbors.add(new Node(coordinates));
            return;
        }
        if(index == this.coordinates.length)
            return;


        generateAllNeighbors(index+1,additions+"2",neighbors,axisAllowed-1,size);
        generateAllNeighbors(index+1,additions+"1",neighbors,axisAllowed-1,size);
        generateAllNeighbors(index+1,additions+"0",neighbors,axisAllowed,size);
    }

    @Override
    public String toString() {
        String str = "[";
        for(int i=0;i<this.coordinates.length;i++)
        {
            str+=this.coordinates[i]+",";
        }
        str=str.substring(0,str.length()-1)+"]";
        return str;
    }
}
