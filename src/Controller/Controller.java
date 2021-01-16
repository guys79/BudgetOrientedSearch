package Controller;

import Components.*;
import SearchAlgorithms.*;
import View.View;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Controller {

    private View view;
    private List<String> res;
    private String headline;

    /**
     * The constructor of the class
     *
     * @param view - The View component
     */
    public Controller(View view) {
        this.view = view;
        this.view.setController(this);
        //  String [] mapNames = {"Berlin_1_256","brc202d","lak303d","den520d","lt_gallowstemplar_n"};

      /*  this.res = new ArrayList<>();
        String headline = "type,Map,Scenario number,Number of agents,Prefix length,Lookahead,Budget per agent,Complete,Search Time,Iterations,Average search time per agent,Average search time per iteration";
        res.add(headline);
        for(int type = 1; type<=8;type++) {

           performSingleRun(1, type, 6, 200, 500, "lak303d", false, 6);
       }*/
        // performSingleRun(9, 4, 8, 500, 100, "lak303d", false, 8);//Excellent example
        //performSingleRun(1, 1, 9, 300, 200, "lak303d", false, 9);
        //type - 1, mapName - lak303d, scenarioNum - 15, numOfAgent - 300, prefixLength - 9, lookahead - 9, budgetPerAgent - 50
        //performSingleRun(15, 4, 9, 300, 50, "lak303d", false, 9);
        performTest();
    }


    /**
     * This function will preform a test
     */
    private void performTest() {

       // try {
        /*
        int [] types = {1,2,3,4,5,6,7};
        int [] scenNumbers = {1,2};
        int [] prefixLengths = {3,6,9};
        String [] mapNames = {"Berlin_1_256","brc202d","lak303d","den520d","lt_gallowstemplar_n","ost003d","w_woundedcoast"};
        int [] budgetPerAgent = {50,100,150};
        int [] lookaheads = {3,6,9};
        int [] numOfAgents = {400};*/


            int[] types = {1, 2, 3, 4};
            int[] scenNumbers = {1};
            int[] prefixLengths = {3, 6, 9};
            int[] budgetPerAgent = {50,100,150,300};
            //String[] mapNames = {"empty-8-8"};
            String[] mapNames = {"lak303d", "den520d", "lt_gallowstemplar_n", "ost003d"};
            int[] lookaheads = {3, 6, 9};
            int[] numOfAgents = {500};


            this.res = new ArrayList<>();
            this.headline = "";
            Map<String, String> params = ParamConfig.getInstance().getParams();
            for (String param : params.keySet()) {
                headline += param + ",";
            }
            headline += "Map,Scenario number,Number of agents,Prefix length,Lookahead,Budget per agent,Complete,Search Time,Iterations,Average search time per agent,Average search time per iteration, Sum of costs";
            res.add(headline);
            String folderLocation = System.getProperty("user.dir") + "\\Resources\\Test";
            for (int type : types) {
                for (String mapName : mapNames) {
                    String dirPath = System.getProperty("user.dir") + "\\Resources" + "\\Scenarios\\" + mapName;
                    File file = new File(dirPath);
                    int size = Math.min(5, file.listFiles().length);
                    scenNumbers = new int[size];
                    for (int i = 1; i <= size; i++) {
                        scenNumbers[i - 1] = i;
                    }
                    for (int scenarioNum : scenNumbers) {
                        if (!new File(String.format("%s\\result_%s&%s&%d.csv", folderLocation, mapName, type, scenarioNum)).exists()) {
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
                            saveResults(folderLocation, String.format("%s&%s&%d", mapName, type, scenarioNum));
                        }
                    }
                }


            }
            saveExplanationTest(folderLocation, types);
            try {
                mergeTestFiles(folderLocation);
            } catch (IOException e) {
                e.printStackTrace();
            }
      //  }
      /*  catch (java.lang.OutOfMemoryError e)
        {
            System.out.println("OutOfMemory");
            performTest();
        }*/

    }

    private void mergeTestFiles(String folderLocation) throws IOException {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        String path = String.format("%s\\test-%s.csv", folderLocation, dtf.format(now));
        new File(path).createNewFile();
        BufferedReader bufferedReader;
        File folder = new File(folderLocation);
        File[] filesInFolder = folder.listFiles();
        String line;
        String total_file_results = headline;
        for (File file : filesInFolder) {
            System.out.println(file.getName());
            if (file.getName().contains("result_")) {
                bufferedReader = new BufferedReader(new FileReader(file.getAbsolutePath()));
                bufferedReader.readLine();
                while ((line = bufferedReader.readLine()) != null) {
                    total_file_results = String.format("%s\n%s", total_file_results, line);

                }
                bufferedReader.close();
                file.delete();
            }


        }
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(path)));
        bufferedWriter.write(total_file_results);
        bufferedWriter.flush();

    }

    /**
     * This function will save the results in the given folder location
     *
     * @param folderLocation - The folder's location
     * @param additionalName - The additional name
     */
    private void saveResults(String folderLocation, String additionalName) {
        try {
            saveResultsInExcel(folderLocation, additionalName);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * This function will save the results in CSV form
     *
     * @param folderLocation - The folder's location
     * @param additionalName - The additional name
     * @throws IOException
     */
    private void saveResultsInExcel(String folderLocation, String additionalName) throws IOException {
        String name = "result_" + additionalName + ".csv";
        String path = folderLocation + "\\" + name;


        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(path)));

        String line;
        for (int i = 0; i < this.res.size(); i++) {
            line = this.res.get(i);
            bufferedWriter.write(line + ",\n");
        }
        bufferedWriter.close();
        this.res.clear();
        this.res.add(headline);

    }

    /**
     * This function will create the explanation file
     *
     * @param folderLocation - The folder's location
     * @param types          - The array of types
     */
    private void saveExplanationTest(String folderLocation, int[] types) {
        String name = "Explanation.txt";
        String path = folderLocation + "\\" + name;
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(path)));
            String line;
            for (int i = 0; i < types.length; i++) {
                ParamConfig.getInstance().configParamsWithType(types[i]);
                bufferedWriter.write(ParamConfig.getInstance().toString() + "\n\n");
            }

            bufferedWriter.close();
        } catch (Exception e) {
            System.out.println("Failed to save file");
        }


    }

    /**
     * This function will preform a single run
     *
     * @param scenNum   - The scenario number
     * @param lookahead - The lookahead
     */
    public void performSingleRun(int scenNum, int lookahead) {
        performSingleRun(scenNum, 1, 5, 500, 1000, "lak303d", false, lookahead);
    }

    /**
     * This function will preform a single run
     *
     * @param scenNum        - The scenario number
     * @param type           - The Algorithm type
     * @param prefixLength   - The prefix's length
     * @param numOfAgents    - The number of agents
     * @param budgetPerAgent - The budget per agent
     * @param mapName        - The map name
     * @param save           - True IFF we want to save the results
     * @param lookahead      - The lookahead
     */
    private void performSingleRun(int scenNum, int type, int prefixLength, int numOfAgents, int budgetPerAgent, String mapName, boolean save, int lookahead) {

        int scenario = scenNum;
        int totalBudget = numOfAgents * budgetPerAgent;

        Agent.restNumOfAgents();
        Node.restNumOfNodes();

        Problem.getInstance().setNewProblem(mapName, scenario, type, prefixLength, totalBudget, numOfAgents, lookahead);
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
            System.out.println("Search Time time per agent " + ((searchTimeOnly * 1.0 / numOfAgents)) + " ms ," + ((searchTimeOnly * 1.0 / numOfAgents)) / 1000.0 + " s");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (save)
            saveResults(scenNum, type, prefixLength, numOfAgents, budgetPerAgent, mapName, lookahead, sumOfCosts);
    }

    /**
     * This function will calculate the sumOfCosts of the given paths
     *
     * @param solutions - The solution
     * @return - The Sum Of Costs
     */
    private double sumOfCosts(Map<Agent, Prefix> solutions) {
        double sumOfCosts = 0;
        double pathCost;
        int prefixLength;
        boolean isAtGoalLast;
        Node currentNode;
        Agent agent;
        Prefix prefix;
        Node lastNode = null;
        //For each solution
        for (Map.Entry<Agent, Prefix> entry : solutions.entrySet()) {
            agent = entry.getKey();
            prefix = entry.getValue();
            prefixLength = prefix.getSize();
            isAtGoalLast = true;
            pathCost = 0;
            for (int i = prefixLength - 1; i >= 0; i--) {
                currentNode = prefix.getNodeAt(i);
                if (isAtGoalLast) {
                    if (!agent.getGoal().equals(currentNode)) {
                        isAtGoalLast = false;
                        lastNode = agent.getGoal();
                        pathCost += ParamConfig.getInstance().getCostFunction().getCost(lastNode, currentNode);
                        lastNode = currentNode;
                    }
                } else {
                    pathCost += ParamConfig.getInstance().getCostFunction().getCost(lastNode, currentNode);
                    lastNode = currentNode;
                }


            }

            sumOfCosts += pathCost;
        }
        return sumOfCosts;

    }

    /**
     * This function will save the results
     *
     * @param scenNum        - The scenario number
     * @param type           - The given algorithm type
     * @param prefixLength   - Tjhe prefix's length
     * @param numOfAgents    -The number of agents
     * @param budgetPerAgent - The budget per agent
     * @param mapName        - The map number
     * @param lookahead      - The lookahead
     * @param sumOfCosts     - The Sum Of Costs
     */
    private void saveResults(int scenNum, int type, int prefixLength, int numOfAgents, int budgetPerAgent, String mapName, int lookahead, double sumOfCosts) {
        int complete = 0;
        if (PerformanceTracker.getInstance().isComplete())
            complete = 1;
        int numOfIter = PerformanceTracker.getInstance().getNumberOFIteration();
        //   long preCompute = PerformanceTracker.getInstance().getPreCompute();
        long searchTimeOnly = PerformanceTracker.getInstance().getSearchTimeNeto();
        double averageSearchTimeForIteration = (searchTimeOnly * 1.0) / numOfIter;
        double averageSearchTimeForAgents = (searchTimeOnly * 1.0) / numOfAgents;
        Map<String, String> params = ParamConfig.getInstance().getParams();
        String result = "";
        String header = this.res.get(0);
        String[] paramNames = header.split(",");
        for (int i = 0; i < params.size(); i++) {
            result += "" + params.get(paramNames[i]) + ",";
        }
        result += String.format("%s,%d,%d,%d,%d,%d,%d,%d,%d,%s,%s,%s", mapName, scenNum, numOfAgents, prefixLength - 1, lookahead, budgetPerAgent, complete, searchTimeOnly, numOfIter, averageSearchTimeForAgents, averageSearchTimeForIteration, sumOfCosts);
        this.res.add(result);

    }
}
