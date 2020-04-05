package Controller;

import Components.*;
import SearchAlgorithms.BudgetOrientedSearch;
import SearchAlgorithms.IMultiAgentSearchAlgorithm;
import View.View;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Controller {

    private View view;
    private List<String> res;
    public Controller(View view)
    {
        this.view = view;
        this.view.setController(this);
        // TODO: 05/04/2020 why did the agent didn't move
        performSingleRun(3,1,5,400,1000,"den520d",false);
       // performTest();
    }

//46881

    private void performTest() {
        this.res = new ArrayList<>();
        int [] types = {1,2};
        int [] scenNumbers = {1,2,3,4,5,6,7};
        int [] prefixLengths = {5};
        String [] mapNames = {"Berlin_1_256","brc202d","lak303d","den520d","lt_gallowstemplar_n","ost003d","w_woundedcoast"};
        int [] budgetPerAgent = {1000};
        //int [] numOfAgents = {500};
        int [] numOfAgents = {50,100,200,300};
       /* int[] types = {1, 2};
        int[] scenNumbers = {1, 2};
        int[] prefixLengths = {5};
        int[] budgetPerAgent = {1000};
        int[] numOfAgents = {10};
        String[] mapNames = {"Berlin_1_256", "brc202d"};*/
        //String result = String.format("%d,&s,%d,%d,&d,%d,%d,%d,%d,%f,%f" , type,mapName,scenNum,numOfAgents,prefixLength,budgetPerAgent,complete,searchTimeOnly,numOfIter,averageSearchTimeForAgents,averageSearchTimeForIteration);
        String headline = "type,Map,Scenario number,Number of agents,Prefix length,Budget per agent,Complete,Search Time,Iterations,Average search time per agent,Average search time per iteration";
        res.add(headline);
        String folderLocation = System.getProperty("user.dir") + "\\Resources\\Test";
        for (int type : types) {
            for (String mapName : mapNames) {
                for (int scenarioNum : scenNumbers) {
                    for (int numOfAgent : numOfAgents) {
                        for (int prefixLength : prefixLengths) {
                            for (int budget : budgetPerAgent) {

                                performSingleRun(scenarioNum, type, prefixLength, numOfAgent, budget, mapName, true);
                            }
                        }
                    }
                }
            }
        }

        saveResults(folderLocation, types);
    }

    private void saveResults(String folderLocation, int[] types)
    {
        try {
            saveExplanationTest(folderLocation,types);
            saveResultsInExcel(folderLocation);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void saveResultsInExcel(String folderLocation) throws IOException {
        String name = "result.csv";
        String path = folderLocation + "\\"+name;



        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(path)));

        String line;
        for(int i=0;i<this.res.size();i++)
        {
            line = this.res.get(i);
            bufferedWriter.write(line+"\n");
        }
        bufferedWriter.close();

    }

    private void saveExplanationTest(String folderLocation, int[] types) throws IOException {
        String name = "Explanation.txt";
        String path = folderLocation + "\\"+name;

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(path)));

        String line;
        for(int i=0;i<types.length;i++)
        {
            ParamConfig.getInstance().configParamsWithType(types[i]);
            bufferedWriter.write(ParamConfig.getInstance().toString()+"\n\n");
        }
        bufferedWriter.close();

    }

    public void performSingleRun(int scenNum)
    {
        performSingleRun(scenNum,1,5,500,1000,"lak303d",false);
    }
    private void performSingleRun(int scenNum , int type, int prefixLength, int numOfAgents, int budgetPerAgent,String mapName,boolean save)
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



        try {
            //long before = System.currentTimeMillis();
            //Map<Agent, Prefix> solutions = searchAlgorithm.getSolution();
            Map<Agent, Prefix> solutions = searchAlgorithm.getSolution(view);
            //long after = System.currentTimeMillis();
            //PerformanceTracker.getInstance().setOverAllSearch(after-before);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Asdasda");
            PerformanceTracker.getInstance().setOverAllSearch(Long.MAX_VALUE);
            PerformanceTracker.getInstance().setComplete(false);
        }



        long preCompute = PerformanceTracker.getInstance().getPreCompute();
        long overAllTime = PerformanceTracker.getInstance().getOverAllSearch();
        long searchTimeOnly = PerformanceTracker.getInstance().getSearchTimeNeto();
        System.out.println("Pre computing time " + preCompute + " ms ," + preCompute / 1000.0 + " s");
        System.out.println("Over all time " + overAllTime + " ms ," + overAllTime / 1000.0 + " s");
        System.out.println("Search Time time " + searchTimeOnly + " ms ," + searchTimeOnly / 1000.0 + " s");
        System.out.println("Search Time time per agent " + ((searchTimeOnly*1.0/numOfAgents)) + " ms ," + searchTimeOnly / 1000.0 + " s");

        if(save)
            saveResults(scenNum,type,prefixLength,numOfAgents,budgetPerAgent,mapName);
    }

    private void saveResults(int scenNum , int type, int prefixLength, int numOfAgents, int budgetPerAgent,String mapName)
    {
        int complete = 0;
        if(PerformanceTracker.getInstance().isComplete())
            complete = 1;
        int numOfIter = PerformanceTracker.getInstance().getNumberOFIteration();
     //   long preCompute = PerformanceTracker.getInstance().getPreCompute();
        long searchTimeOnly = PerformanceTracker.getInstance().getSearchTimeNeto();
        double averageSearchTimeForIteration = (searchTimeOnly*1.0)/numOfIter;
        double averageSearchTimeForAgents = (searchTimeOnly*1.0)/numOfAgents;

        String result = String.format("%d,%s,%d,%d,%d,%d,%d,%d,%d,%s,%s" , type,mapName,scenNum,numOfAgents,prefixLength,budgetPerAgent,complete,searchTimeOnly,numOfIter,averageSearchTimeForAgents,averageSearchTimeForIteration);
        this.res.add(result);

    }
}
