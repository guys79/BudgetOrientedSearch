package SearchAlgorithms;

import Components.Agent;
import Components.Prefix;
import Components.Problem;
import View.View;

import java.util.Map;

public abstract class AbstractMultiAgentSearchAlgorithm implements IMultiAgentSearchAlgorithm {

    private View view;

    public AbstractMultiAgentSearchAlgorithm()
    {

    }

    @Override
    public Map<Agent, Prefix> getSolution(View view) {
        this.view = view;
        int [][]grid = Problem.getInstance().getGrid();
        this.view.initialize(grid);
        Map<Agent, Prefix> solutions = getSolution();

        for(Prefix prefix : solutions.values())
        {
            this.view.addAgent(prefix);
        }
        view.draw();
        return solutions;
    }


}
