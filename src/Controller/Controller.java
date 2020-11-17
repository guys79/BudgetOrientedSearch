package Controller;

import Components.*;
import Components.Agent;
import SearchAlgorithms.BudgetOrientedSearch;
import SearchAlgorithms.IMultiAgentSearchAlgorithm;
import View.View;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Controller {

    private View view;
    private List<String> res;
    private String headline;
    public Controller(View view)
    {
        this.view = view;
        this.view.setController(this);
      //  String [] mapNames = {"Berlin_1_256","brc202d","lak303d","den520d","lt_gallowstemplar_n"};

      /*  this.res = new ArrayList<>();
        String headline = "type,Map,Scenario number,Number of agents,Prefix length,Lookahead,Budget per agent,Complete,Search Time,Iterations,Average search time per agent,Average search time per iteration";
        res.add(headline);
        for(int type = 1; type<=8;type++) {

           performSingleRun(1, type, 6, 200, 500, "lak303d", false, 6);
       }*/
        performSingleRun(1, 1, 8, 55, 100, "empty-8-8", false, 8);
      // performTest();
    }



    private void performTest() {

        /*
        int [] types = {1,2,3,4,5,6,7};
        int [] scenNumbers = {1,2};
        int [] prefixLengths = {3,6,9};
        String [] mapNames = {"Berlin_1_256","brc202d","lak303d","den520d","lt_gallowstemplar_n","ost003d","w_woundedcoast"};
        int [] budgetPerAgent = {50,100,150};
        int [] lookaheads = {3,6,9};
        int [] numOfAgents = {400};*/


        int [] types = {1,2,3};
        int [] scenNumbers = {1};
        int [] prefixLengths = {3,6,9};
        int [] budgetPerAgent = {50,100,150};
        String [] mapNames = {"empty-8-8"};
        //String [] mapNames = {"lak303d","den520d","lt_gallowstemplar_n","ost003d"};
        int [] lookaheads = {2,3,5};
        int [] numOfAgents = {6};

        this.res = new ArrayList<>();
        this.headline = "";
        Map<String,String> params = ParamConfig.getInstance().getParams();
        for(String param : params.keySet())
        {
            headline+=param+",";
        }
        headline += "Map,Scenario number,Number of agents,Prefix length,Lookahead,Budget per agent,Complete,Search Time,Iterations,Average search time per agent,Average search time per iteration, Sum of costs";
        res.add(headline);
        String folderLocation = System.getProperty("user.dir") + "\\Resources\\Test";
        for (int type : types) {
            for (String mapName : mapNames) {
               String dirPath = System.getProperty("user.dir")+"\\Resources"+"\\Scenarios\\"+mapName;
                File file = new File(dirPath);
                int size = Math.min(15,file.listFiles().length);
                scenNumbers = new int[size];
                for(int i=1;i<=size;i++)
                {
                    scenNumbers[i-1] = i;
                }
                for (int scenarioNum : scenNumbers) {
                    for (int numOfAgent : numOfAgents) {
                        for (int prefixLength : prefixLengths) {
                            for (int lookahead : lookaheads) {
                                if (lookahead == prefixLength) {
                                    for (int budget : budgetPerAgent) {

                                            System.out.println(String.format("type - %d, mapName - %s, scenarioNum - %d, numOfAgent - %d, prefixLength - %d, lookahead - %d, budgetPerAgent - %d", type, mapName, scenarioNum, numOfAgent, prefixLength, lookahead, budget));
                                            performSingleRun(scenarioNum, type, prefixLength, numOfAgent, budget, mapName, true, lookahead);

                                    }
                                }
                            }
                        }
                    }
                }
            }
            saveResults(folderLocation, types,""+type);
        }
       // performSingleRun(1,3,4,100,50,"lak303d",true,4);

    }

    private void saveResults(String folderLocation, int[] types,String additionalName)
    {
        try {
            saveExplanationTest(folderLocation,types);
            saveResultsInExcel(folderLocation,additionalName);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void saveResultsInExcel(String folderLocation,String additionalName) throws IOException {
        String name = "result_"+additionalName+".csv";
        String path = folderLocation + "\\"+name;



        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(path)));

        String line;
        for(int i=0;i<this.res.size();i++)
        {
            line = this.res.get(i);
            bufferedWriter.write(line+"\n");
        }
        bufferedWriter.close();
        this.res.clear();
        this.res.add(headline);

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


        double sumOfCosts = 0;
        try {

           Map<Agent, Prefix> solutions = searchAlgorithm.getSolution(view);
           // long before = System.currentTimeMillis();
            //Map<Agent, Prefix> solutions = searchAlgorithm.getSolution();
            sumOfCosts = sumOfCosts(solutions);
            //long after = System.currentTimeMillis();
            //PerformanceTracker.getInstance().setOverAllSearch(after-before);




        long preCompute = PerformanceTracker.getInstance().getPreCompute();
        long overAllTime = PerformanceTracker.getInstance().getOverAllSearch();
        long searchTimeOnly = PerformanceTracker.getInstance().getSearchTimeNeto();
        System.out.println("Pre computing time " + preCompute + " ms ," + preCompute / 1000.0 + " s");
        System.out.println("Over all time " + overAllTime + " ms ," + overAllTime / 1000.0 + " s");
        System.out.println("Search Time time " + searchTimeOnly + " ms ," + searchTimeOnly / 1000.0 + " s");
        System.out.println("Search Time time per agent " + ((searchTimeOnly*1.0/numOfAgents)) + " ms ," + ((searchTimeOnly*1.0/numOfAgents)) / 1000.0 + " s");
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        if(save)
            saveResults(scenNum,type,prefixLength,numOfAgents,budgetPerAgent,mapName,lookahead,sumOfCosts);
    }

    private double sumOfCosts(Map<Agent, Prefix> solutions)
    {
        double sumOfCosts = 0;
        double pathCost;
        int prefixLength;
        boolean isAtGoalLast;
        Node currentNode;
        Agent agent;
        Prefix prefix;
        Node lastNode = null;
        //For each solution
        for(Map.Entry<Agent,Prefix> entry : solutions.entrySet())
        {
            agent = entry.getKey();
            prefix = entry.getValue();
            prefixLength = prefix.getSize();
            isAtGoalLast = true;
            pathCost = 0;
            for( int i=prefixLength - 1; i>=0; i--)
            {
                currentNode = prefix.getNodeAt(i);
                if(isAtGoalLast) {
                    if (!agent.getGoal().equals(currentNode)) {
                        isAtGoalLast = false;
                        lastNode = agent.getGoal();
                        pathCost += ParamConfig.getInstance().getCostFunction().getCost(lastNode,currentNode);
                        lastNode = currentNode;
                    }
                }
                else
                {
                    pathCost += ParamConfig.getInstance().getCostFunction().getCost(lastNode,currentNode);
                    lastNode = currentNode;
                }



            }

            sumOfCosts+= pathCost;
        }
        return sumOfCosts;

    }

    private void saveResults(int scenNum, int type, int prefixLength, int numOfAgents, int budgetPerAgent, String mapName, int lookahead, double sumOfCosts)
    {
        int complete = 0;
        if(PerformanceTracker.getInstance().isComplete())
            complete = 1;
        int numOfIter = PerformanceTracker.getInstance().getNumberOFIteration();
     //   long preCompute = PerformanceTracker.getInstance().getPreCompute();
        long searchTimeOnly = PerformanceTracker.getInstance().getSearchTimeNeto();
        double averageSearchTimeForIteration = (searchTimeOnly*1.0)/numOfIter;
        double averageSearchTimeForAgents = (searchTimeOnly*1.0)/numOfAgents;
        Map<String,String> params = ParamConfig.getInstance().getParams();
        String result = "";
        String header = this.res.get(0);
        String [] paramNames = header.split(",");
        for(int i=0;i<params.size();i++)
        {
            result+=""+params.get(paramNames[i])+",";
        }
        result += String.format("%s,%d,%d,%d,%d,%d,%d,%d,%d,%s,%s,%s" ,mapName,scenNum,numOfAgents,prefixLength-1,lookahead,budgetPerAgent,complete,searchTimeOnly,numOfIter,averageSearchTimeForAgents,averageSearchTimeForIteration,sumOfCosts);
        this.res.add(result);

    }
}
