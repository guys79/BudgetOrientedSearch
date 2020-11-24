package Components;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

/**
 * This class represents an instance of the problem
 * This class will use the Singleton design pattern
 */
public class Problem {
    private String mapName;// The name of the map
    private int scenario;//The number of scenario
    private int type;//the type of param config
    private Set<Agent> agents;//The agents
    private int prefix;//the prefix length that the agents need to calculate in each iteration
    private int totalBudget;//The total budget for all of the agents
    private String filePaths;//The path of the scenario and map files
    private String mapPath;//The path to the map file
    private String scenarioPath;//The path to the scenario file
    private int[] size;//The size of the map
    private static Problem instance;//The instance og Problem
    private HashSet<String> validLocations;//The valid locations in the map
    private int numOfAgents; // The number of agents
    private int lookahead;


    /**
     * This constructor of the class
     */
    private Problem() {

    }

    /**
     * This function will return the instance of the class
     *
     * @return - The instance of the class
     */
    public static Problem getInstance() {
        if (instance == null)
            instance = new Problem();
        return instance;
    }

    /**
     * This function will set new problem
     *
     * @param mapName     - The name of the map
     * @param scenario    - The scenario number
     * @param type        - The type of param config
     * @param prefix      - The lenght of the prefix that the agents need to calculate in each iteration
     * @param totalBudget - The total amount of budget of all of the agents in each iteration@param totalBudget
     * @param numOfAgents - The number of agents
     */
    public void setNewProblem(String mapName, int scenario, int type, int prefix, int totalBudget, int numOfAgents, int lookahead) {
        this.mapName = mapName;
        this.scenario = scenario;
        filePaths = System.getProperty("user.dir") + "/Resources";
        this.numOfAgents = numOfAgents;
        this.type = type;
        this.lookahead = lookahead;
        this.totalBudget = totalBudget;
        this.prefix = prefix;
        this.agents = new HashSet<>();
        this.mapPath = this.filePaths + "/Maps/" + this.mapName + ".map";
        this.validLocations = new HashSet<>();
        this.scenarioPath = this.filePaths + "/Scenarios/" + this.mapName + "/" + this.mapName + "-" + scenario + ".scen";
        ParamConfig.getInstance().configParamsWithType(type);
        this.size = new int[ParamConfig.getInstance().getNumOfDimensions()];
        buildMap();
        createScenario();

    }

    /**
     * This function will create the scenario
     */
    private void createScenario() {
        create2DScenarios();
    }

    private void create2DScenarios() {

        // System.out.println(this.scenarioPath);
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(this.scenarioPath)));
            String line;
            bufferedReader.readLine();

            int count = 0;
            String[] agentScenario;
            int[] startLoc, goalLoc;
            Node start, goal;
            Agent agent;
            while ((line = bufferedReader.readLine()) != null && count < numOfAgents) {
                agentScenario = parseScenerio(line);
                startLoc = new int[2];
                goalLoc = new int[2];
                startLoc[0] = Integer.parseInt(agentScenario[1]);
                startLoc[1] = Integer.parseInt(agentScenario[2]);
                goalLoc[0] = Integer.parseInt(agentScenario[3]);
                goalLoc[1] = Integer.parseInt(agentScenario[4]);
                start = new Node(startLoc);
                goal = new Node(goalLoc);
                agent = new Agent(start, goal);
                this.agents.add(agent);
                count++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Thgis function will take a scenario represented by a string and will parse it
     *
     * @param scen - The scenario
     * @return - An array of Strings that describes the scenario
     */
    private String[] parseScenerio(String scen) {
        String[] split = scen.split("\t");
        String[] scenario = new String[6];

        scenario[0] = split[1];//The name of the map
        scenario[1] = split[7];//Start - x
        scenario[2] = split[6];//Start - y
        scenario[3] = split[5];//End - x
        scenario[4] = split[4];//End - y
        scenario[5] = split[8];//Optimal length
        return scenario;
    }

    /**
     * This function will build the map
     */
    private void buildMap() {
        build2DMap();
    }

    /**
     * This function will build a 2D map
     */
    private void build2DMap() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(this.mapPath)));
            String line;
            bufferedReader.readLine();

            line = bufferedReader.readLine();
            size[0] = Integer.parseInt(line.substring(line.indexOf(" ") + 1));//Height (number of rows)

            line = bufferedReader.readLine();
            size[1] = Integer.parseInt(line.substring(line.indexOf(" ") + 1));//Width (number of columns)

            bufferedReader.readLine();

            int rowNumber = 0;

            while ((line = bufferedReader.readLine()) != null) {
                for (int colNumber = 0; colNumber < size[1]; colNumber++) {
                    if (this.isSymboleAClearPath(line.charAt(colNumber))) {
                        this.validLocations.add(rowNumber + "," + colNumber);
                    }
                }
                rowNumber++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This function will return if the symbol represents a dot
     *
     * @param symbol - The symbol
     * @return - True if the symbol represents a clear path
     */
    private boolean isSymboleAClearPath(char symbol) {
        return symbol == '.';
    }

    /**
     * This function will return the name of the map
     *
     * @return - The name of the map
     */
    public String getMapName() {
        return mapName;
    }

    public int getLookahead() {
        return lookahead;
    }

    /**
     * This function will return the scenario number
     *
     * @return - The scenario number
     */
    public int getScenario() {
        return scenario;
    }

    /**
     * This function will return the param configuration type
     *
     * @return - The param configuration type
     */
    public int getType() {
        return type;
    }

    /**
     * This function will return the agents
     *
     * @return - The agents
     */
    public Set<Agent> getAgents() {
        return agents;
    }

    /**
     * This function will return the number of agents
     *
     * @return - The number of agents
     */
    public int getNumOfAgents() {
        return numOfAgents;
    }

    /**
     * This function will return the length of the prefix
     *
     * @return - The length of the prefix
     */
    public int getPrefix() {
        return prefix;
    }

    /**
     * This function will return the total budget
     *
     * @return - The total budget
     */
    public int getTotalBudget() {
        return totalBudget;
    }

    /**
     * This function will return the size of the map
     *
     * @return - The size of the map
     */
    public int[] getSize() {
        return size;
    }

    /**
     * This function will return whether the location is valid or not
     *
     * @param loc - The given location
     * @return - True if the location is valid
     */
    public boolean isValidLocation(int[] loc) {
        String locString = "";
        for (int i = 0; i < loc.length; i++) {
            locString += loc[i] + ",";
        }
        locString = locString.substring(0, locString.length() - 1);
        return this.validLocations.contains(locString);
    }

    public int[][] getGrid() {
        int height = size[0];
        int width = size[1];

        int[][] grid = new int[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (this.validLocations.contains(i + "," + j))
                    grid[i][j] = 1;
                else
                    grid[i][j] = -1;
            }
        }
        return grid;
    }
}
