package SearchAlgorithms;

import Components.Agent;
import Components.PerformanceTracker;
import Components.Prefix;
import Components.Problem;
import View.View;

import java.util.Map;

/**
 * This class represents an abstract Nulti Agent Search Algorithm
 */
public abstract class AbstractMultiAgentSearchAlgorithm implements IMultiAgentSearchAlgorithm {

    private View view;

    public AbstractMultiAgentSearchAlgorithm() {

    }

    @Override
    public Map<Agent, Prefix> getSolution(View view) {
        this.view = view;
        int[][] grid = Problem.getInstance().getGrid();
        this.view.initialize(grid);
        long before = System.currentTimeMillis();
        Map<Agent, Prefix> solutions = getSolution();
        long after = System.currentTimeMillis();
        PerformanceTracker.getInstance().setOverAllSearch(after - before);
        for (Prefix prefix : solutions.values()) {
            this.view.addAgent(prefix);
        }
        view.draw();
        return solutions;
    }


}
