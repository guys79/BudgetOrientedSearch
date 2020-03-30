package Controller;

import Components.*;
import SearchAlgorithms.BudgetOrientedSearch;
import SearchAlgorithms.IMultiAgentSearchAlgorithm;
import View.View;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Map;
import java.util.Set;

public class Controller {

    private View view;
    public Controller(View view)
    {
        this.view = view;
        this.view.setController(this);
        performSingleRun(3,1,5,500,1000,"lak303d");
    }



    private void performTest()
    {
        int [] types = {1,2};
        int [] scenNumbers = {1,2,3,4,5,6,7};
        int [] prefixLength = {5};
        int [] numOfAgents = {50,100,200,300,500};
    }

    public void performSingleRun(int scenNum)
    {
        performSingleRun(scenNum,1,5,500,1000,"lak303d");
    }
    private void performSingleRun(int scenNum , int type, int prefixLength, int numOfAgents, int budgetPerAgent,String mapName)
    {

        int scenario = scenNum;
        int totalBudget = numOfAgents * budgetPerAgent;

        Agent.restNumOfAgents();
        Node.restNumOfNodes();

        Problem.getInstance().setNewProblem(mapName, scenario, type, prefixLength, totalBudget, numOfAgents);
        PerformanceTracker.getInstance().reset();

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
        System.out.println("Search Time time per agent " + ((searchTimeOnly*1.0/numOfAgents)) + " ms ," + searchTimeOnly / 1000.0 + " s");

        saveResults(scenNum,type,prefixLength,numOfAgents,budgetPerAgent,mapName);
    }

    private void saveResults(int scenNum , int type, int prefixLength, int numOfAgents, int budgetPerAgent,String mapName)
    {
        throw  new NotImplementedException();
    }
}
