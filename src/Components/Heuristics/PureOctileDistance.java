package Components.Heuristics;

import Components.Node;

/**
 * This class represents the classic octile distance heuristic
 */
public class PureOctileDistance implements IHeuristic{

    @Override
    public double getHeuristic(Node node, Node dest) {
        int x1 = node.getCoordinateAt(0);
        int x2 = dest.getCoordinateAt(0);
        int y1 = node.getCoordinateAt(1);
        int y2 = dest.getCoordinateAt(1);
        int disX = Math.abs(x1-x2);
        int disY = Math.abs(y1-y2);
        int min = Math.min(disX,disY);
        int max = Math.max(disX,disY);
        double sqrt2 = Math.sqrt(2);
        double res = min*sqrt2  + (max-min)*1;
        return res;
    }
}
