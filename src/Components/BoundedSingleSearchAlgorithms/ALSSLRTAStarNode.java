package Components.BoundedSingleSearchAlgorithms;

import Components.Node;

import java.util.Objects;

/**
 * This class represents the node in the aLSS-LRTA* algorithm
 */
public class ALSSLRTAStarNode {

    private Node node;//The represented node
    private int timeStamp;//The time stamp



    /**
     * The constructor of the class
     * @param node - The given node
     * @param timeStamp - The time stamp
     */
    public ALSSLRTAStarNode(Node node,int timeStamp)
    {
        this.node = node;
        this.timeStamp = timeStamp;
    }

    /**
     * This function will return the time stamp
     * @return - The time stamp
     */
    public int getTimeStamp() {
        return timeStamp;
    }

    /**
     * This function will return the node
     * @return - The node
     */
    public Node getNode() {
        return node;
    }


    @Override
    public String toString() {
        return node +" time = "+this.timeStamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ALSSLRTAStarNode that = (ALSSLRTAStarNode) o;
        return timeStamp == that.timeStamp &&
                Objects.equals(node, that.node);
    }

    @Override
    public int hashCode() {

        return Objects.hash(node, timeStamp);
    }
}
