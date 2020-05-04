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
      //  String [] mapNames = {"Berlin_1_256","brc202d","lak303d","den520d","lt_gallowstemplar_n"};
       // for(String map : mapNames)
         //  this.res = new ArrayList<>();
       // String headline = "type,Map,Scenario number,Number of agents,Prefix length,Lookahead,Budget per agent,Complete,Search Time,Iterations,Average search time per agent,Average search time per iteration";
        //res.add(headline);
        performSingleRun(1,1,4,100,500,"lak303d",false,4);

       //performTest();
    }



    private void performTest() {

        int [] types = {2};
        int [] scenNumbers = {1,2,3};
        int [] prefixLengths = {3,6,9};
 //       String [] mapNames = {"Berlin_1_256","brc202d","lak303d","den520d","lt_gallowstemplar_n","ost003d","w_woundedcoast"};
        String [] mapNames = {"lak303d","den520d","lt_gallowstemplar_n","ost003d"};

        int [] budgetPerAgent = {50,100,150};
        //int [] lookaheads = {4,5,6};
        int [] lookaheads = {6};
        //int [] numOfAgents = {500};
        int [] numOfAgents = {100,300,400};
       /* int[] types = {1, 2};
        int[] scenNumbers = {1, 2};
        int[] prefixLengths = {5};
        int[] budgetPerAgent = {1000};
        int[] numOfAgents = {10};
        String[] mapNames = {"Berlin_1_256", "brc202d"};*/
        //String result = String.format("%d,&s,%d,%d,&d,%d,%d,%d,%d,%f,%f" , type,mapName,scenNum,numOfAgents,prefixLength,budgetPerAgent,complete,searchTimeOnly,numOfIter,averageSearchTimeForAgents,averageSearchTimeForIteration);
        this.res = new ArrayList<>();
        String headline = "type,Map,Scenario number,Number of agents,Prefix length,Lookahead,Budget per agent,Complete,Search Time,Iterations,Average search time per agent,Average search time per iteration";
        res.add(headline);
        String folderLocation = System.getProperty("user.dir") + "\\Resources\\Test";
        for (int type : types) {
            for (String mapName : mapNames) {
                for (int scenarioNum : scenNumbers) {
                    for (int numOfAgent : numOfAgents) {
                        for (int prefixLength : prefixLengths) {
                            for (int lookahead : lookaheads) {
                                if (lookahead >= prefixLength) {
                                    for (int budget : budgetPerAgent) {
                                        System.out.println(String.format("type - %d, mapName - %s, scenarioNum - %d, numOfAgent - %d, prefixLength - %d, lookahead - %d, budgetPerAgent - %d" ,type,mapName,scenarioNum,numOfAgent,prefixLength,lookahead,budget));
                                        performSingleRun(scenarioNum, type, prefixLength, numOfAgent, budget, mapName, true, prefixLength);// TODO: 20/04/2020 change lookahed value
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
       // performSingleRun(1,3,4,100,50,"lak303d",true,4);
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

    public void performSingleRun(int scenNum,int lookahead)
    {
        performSingleRun(scenNum,1,5,500,1000,"lak303d",false,lookahead);
    }
    private void performSingleRun(int scenNum , int type, int prefixLength, int numOfAgents, int budgetPerAgent,String mapName,boolean save,int lookahead)
    {

        int scenario = scenNum;
        int totalBudget = numOfAgents * budgetPerAgent;

        Agent.restNumOfAgents();
        Node.restNumOfNodes();

        Problem.getInstance().setNewProblem(mapName, scenario, type, prefixLength, totalBudget, numOfAgents,lookahead);
        PerformanceTracker.getInstance().reset();

        Set<Agent> agents = Problem.getInstance().getAgents();
        for (Agent agent : agents)
            System.out.println(agent);

        IMultiAgentSearchAlgorithm searchAlgorithm = new BudgetOrientedSearch();



        try {
            //long before = System.currentTimeMillis();
            //Map<Agent, Prefix> solutions = searchAlgorithm.getSolution();
          Map<Agent, Prefix> solutions = searchAlgorithm.getSolution(view);
          // long after = System.currentTimeMillis();
           //PerformanceTracker.getInstance().setOverAllSearch(after-before);
        }
        catch (Exception e) {
            e.printStackTrace();
            PerformanceTracker.getInstance().setOverAllSearch(Long.MAX_VALUE);
            PerformanceTracker.getInstance().setComplete(false);
        }



        long preCompute = PerformanceTracker.getInstance().getPreCompute();
        long overAllTime = PerformanceTracker.getInstance().getOverAllSearch();
        long searchTimeOnly = PerformanceTracker.getInstance().getSearchTimeNeto();
        System.out.println("Pre computing time " + preCompute + " ms ," + preCompute / 1000.0 + " s");
        System.out.println("Over all time " + overAllTime + " ms ," + overAllTime / 1000.0 + " s");
        System.out.println("Search Time time " + searchTimeOnly + " ms ," + searchTimeOnly / 1000.0 + " s");
        System.out.println("Search Time time per agent " + ((searchTimeOnly*1.0/numOfAgents)) + " ms ," + ((searchTimeOnly*1.0/numOfAgents)) / 1000.0 + " s");

        if(save)
            saveResults(scenNum,type,prefixLength,numOfAgents,budgetPerAgent,mapName,lookahead);
    }

    private void saveResults(int scenNum , int type, int prefixLength, int numOfAgents, int budgetPerAgent,String mapName,int lookahead)
    {
        int complete = 0;
        if(PerformanceTracker.getInstance().isComplete())
            complete = 1;
        int numOfIter = PerformanceTracker.getInstance().getNumberOFIteration();
     //   long preCompute = PerformanceTracker.getInstance().getPreCompute();
        long searchTimeOnly = PerformanceTracker.getInstance().getSearchTimeNeto();
        double averageSearchTimeForIteration = (searchTimeOnly*1.0)/numOfIter;
        double averageSearchTimeForAgents = (searchTimeOnly*1.0)/numOfAgents;

        String result = String.format("%d,%s,%d,%d,%d,%d,%d,%d,%d,%d,%s,%s" , type,mapName,scenNum,numOfAgents,prefixLength,lookahead,budgetPerAgent,complete,searchTimeOnly,numOfIter,averageSearchTimeForAgents,averageSearchTimeForIteration);
        this.res.add(result);

    }
}
