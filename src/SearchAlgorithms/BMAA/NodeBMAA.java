package SearchAlgorithms.BMAA;

import Components.Node;

import java.util.HashMap;
import java.util.Map;

public class NodeBMAA {
    private Node node;//The represented node
    private int timeStamp;//The time stamp
    private NodeBMAA parent;//The parent node
    private static Map<Node,Double> minGVal;
    private double gVal;

    public NodeBMAA(Node node, int timeStamp, NodeBMAA parent) {
        this.node = node;
        this.timeStamp = timeStamp;
        this.parent = parent;
        gVal = Double.MAX_VALUE;
        if(minGVal == null)
            minGVal = new HashMap<>();

    }

    public NodeBMAA(Node node, int timeStamp) {

        this(node, timeStamp, null);

    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public int getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(int timeStamp) {
        this.timeStamp = timeStamp;
    }

    public NodeBMAA getParent() {
        return parent;
    }

    public void setParent(NodeBMAA parent) {
        this.parent = parent;
    }

    public void setGVal(double gVal)
    {
        this.gVal = gVal;

        if(minGVal.containsKey(this.getNode()))
        {
            minGVal.put(node,Math.min(gVal,minGVal.get(node)));
        }
        else
        {
            minGVal.put(node,gVal);
        }
    }
    public double getGVal()
    {
        return this.gVal;
    }
    public double getMinGval()
    {
        if(minGVal.containsKey(node))
        {
            return minGVal.get(node);
        }
        return Double.MAX_VALUE;
    }

}
