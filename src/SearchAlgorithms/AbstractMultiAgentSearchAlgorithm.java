package SearchAlgorithms;

import Components.Agent;
import Components.Prefix;
import Components.Problem;
import View.Controller;

import java.util.Map;

public abstract class AbstractMultiAgentSearchAlgorithm implements IMultiAgentSearchAlgorithm {

    private Controller controller;

    public AbstractMultiAgentSearchAlgorithm()
    {

    }

    @Override
    public Map<Agent, Prefix> getSolution(Controller controller) {
        this.controller = controller;
        int [][]grid = Problem.getInstance().getGrid();
        this.controller.initialize(grid);
        Map<Agent, Prefix> solutions = getSolution();

        for(Prefix prefix : solutions.values())
        {
            this.controller.addAgent(prefix);
        }
        controller.draw();
        return solutions;
    }


}
