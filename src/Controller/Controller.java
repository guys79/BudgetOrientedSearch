package Controller;

import Components.*;
import SearchAlgorithms.BudgetOrientedSearch;
import SearchAlgorithms.IMultiAgentSearchAlgorithm;
import View.View;

import java.util.Map;
import java.util.Set;

public class Controller {

    private View view;
    public Controller(View view)
    {
        this.view = view;
        this.view.setController(this);
        init(3);
    }

    public void init(int scenNum) {

        String mapName = "lak303d";
        //String mapName = "random-32-32-10";
        //String mapName = "empty-8-8";
        int scenario = scenNum;
        int type = 1;
        int prefixLength = 5;
        int numOfAgents = 350;
        int budgetPerAgent = 150;
        int totalBudget = numOfAgents * budgetPerAgent;

        Agent.restNumOfAgents();
        Node.restNumOfNodes();

        Problem.getInstance().setNewProblem(mapName, scenario, type, prefixLength, totalBudget, numOfAgents);
        int height = Problem.getInstance().getSize()[0];
        int width = Problem.getInstance().getSize()[1];
        int[] loc;

        Set<Agent> agents = Problem.getInstance().getAgents();
        for (Agent agent : agents)
            System.out.println(agent);

        IMultiAgentSearchAlgorithm searchAlgorithm = new BudgetOrientedSearch();


        Map<Agent, Prefix> solutions = searchAlgorithm.getSolution(view);


        long preCompute = PerformanceTracker.getInstance().getPreCompute();
        long overAllTime = PerformanceTracker.getInstance().getOverAllSearch();
        long searchTimeOnly = PerformanceTracker.getInstance().getSearchTimeNeto();
        System.out.println("Pre computing time " + preCompute + " ms ," + preCompute / 1000.0 + " s");
        System.out.println("Over all time " + overAllTime + " ms ," + overAllTime / 1000.0 + " s");
        System.out.println("Search Time time " + searchTimeOnly + " ms ," + searchTimeOnly / 1000.0 + " s");
    }
}
