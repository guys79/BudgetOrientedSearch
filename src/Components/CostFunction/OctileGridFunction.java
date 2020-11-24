package Components.CostFunction;

import Components.Node;

/**
 * This class represents the octile cost function
 */
public class OctileGridFunction implements Components.CostFunction.ICostFunction {

    /**
     * The empty constructor
     */
    public OctileGridFunction() {
    }


    @Override
    public double getCost(Node node1, Node node2) {

        int x1 = node1.getCoordinateAt(0);
        int x2 = node2.getCoordinateAt(0);
        int y1 = node1.getCoordinateAt(1);
        int y2 = node2.getCoordinateAt(1);
        if (x1 == x2 && y1 == y2)
            return 0;
        int disX = Math.abs(x1 - x2);
        int disY = Math.abs(y1 - y2);

        if (disX + disY == 1)
            return 1;
        if (disX == 1 && disY == 1)
            return Math.sqrt(2);
        return Double.MAX_VALUE;
    }

    @Override
    public String toString() {
        return "OctileGridFunction";
    }
}
