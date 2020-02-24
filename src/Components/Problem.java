package Components;

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
    private final String filePaths = "";//The path of the scenario and map files
    private String mapPath;//The path to the map file
    private String scenarioPath;//The path to the scenario file
    private int [] size;//The size of the map
    private static Problem instance;//The instance og Problem
    private HashSet<String> validLocations;//The valid locations in the map


    /**
     * This constructor of the class
     *
     */
    private Problem (){

    }

    /**
     * This function will return the instance of the class
     * @return - The instance of the class
     */
    public static Problem getInstance()
    {
        if(instance == null)
            instance = new Problem();
        return instance;
    }
    /**
     * This function will set new problem
     * @param mapName - The name of the map
     * @param scenario - The scenario number
     * @param type - The type of param config
     * @param prefix - The lenght of the prefix that the agents need to calculate in each iteration
     * @param totalBudget - The total amount of budget of all of the agents in each iteration@param totalBudget
     */
    public void setNewProblem(String mapName, int scenario, int type,int prefix,int totalBudget)
    {
        this.mapName = mapName;
        this.scenario = scenario;
        this.type = type;
        this.totalBudget = totalBudget;
        this.prefix = prefix;
        this.agents = new HashSet<>();
        this.mapPath = this.filePaths+"\\maps\\"+this.mapName+".map";
        this.validLocations = new HashSet<>();
        this.scenarioPath = this.filePaths+"\\scenarios\\"+this.mapName+".scen";
        ParamConfig.getInstance().configParamsWithType(type);
        this.size = new int[ParamConfig.getInstance().getNumOfDimensions()];
        buildMap();
        getScenario();

    }
    /**
     * This function will create the scenario
     */
    private void createScenario()
    {
        // TODO: 23/02/2020 Create the scenario . assign agents to their nodes
        
    }

    /**
     * This function will build the map
     */
    private void buildMap()
    {
        int numRow = 4;
        int numCol = 4;
        for(int row =0;row<numRow;row++)
        {
            for(int col =0;col<numCol;col++)
            {
                validLocations.add(row+","+col);
            }
        }

        validLocations.remove("1,1");
        validLocations.remove("2,1");
        validLocations.remove("0,1");
        validLocations.remove("3,1");
        size[0]= numRow;
        size[1]= numCol;

        // TODO: 24/02/2020 Update the validLocations set

    }

    /**
     * This function will return the name of the map
     * @return - The name of the map
     */
    public String getMapName() {
        return mapName;
    }

    /**
     * This function will return the scenario number
     * @return - The scenario number
     */
    public int getScenario() {
        return scenario;
    }

    /**
     * This function will return the param configuration type
     * @return - The param configuration type
     */
    public int getType() {
        return type;
    }

    /**
     * This function will return the agents
     * @return - The agents
     */
    public Set<Agent> getAgents() {
        return agents;
    }

    /**
     * This function will return the length of the prefix
     * @return - The length of the prefix
     */
    public int getPrefix() {
        return prefix;
    }

    /**
     * This function will return the total budget
     * @return - The total budget
     */
    public int getTotalBudget() {
        return totalBudget;
    }

    /**
     * This function will return the size of the map
     * @return - The size of the map
     */
    public int[] getSize() {
        return size;
    }

    /**
     * This function will return whether the location is valid or not
     * @param loc - The given location
     * @return - True if the location is valid
     */
    public boolean isValidLocation(int [] loc)
    {
        String locString = "";
        for(int i=0;i<loc.length;i++)
        {
            locString += loc[i]+",";
        }
        locString = locString.substring(0,locString.length()-1);
        return this.validLocations.contains(locString);
    }
}
