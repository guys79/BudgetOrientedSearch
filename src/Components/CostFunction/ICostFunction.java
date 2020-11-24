package Components.CostFunction;

import Components.Node;

/**
 * This class represents a generic cost function
 */
public interface ICostFunction {

    /**
     * This function will return the cost of moving from origin to destination
     *
     * @param origin      - The origi node
     * @param destination - The destination node
     * @return - The cost of moving from origin to destination
     */
    public double getCost(Node origin, Node destination);
}
