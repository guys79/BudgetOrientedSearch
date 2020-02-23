package Components;

import java.util.HashSet;
import java.util.Set;

/**
 * This class represents an isnstance of the problem
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
    /**
     * This constructor of the class
     * @param mapName - The name of the map
     * @param scenario - The scenario number
     * @param type - The type of param config
     * @param prefix - The lenght of the prefix that the agents need to calculate in each iteration
     * @param totalBudget - The total amount of budget of all of the agents in each iteration
     */
    public Problem(String mapName, int scenario, int type,int prefix,int totalBudget) {
        this.mapName = mapName;
        this.scenario = scenario;
        this.type = type;
        this.totalBudget = totalBudget;
        this.prefix = prefix;
        this.agents = new HashSet<>();
        this.mapPath = this.filePaths+"\\maps\\"+this.mapName+".map";
        this.scenarioPath = this.filePaths+"\\scenarios\\"+this.mapName+".scen";
        ParamConfig.getInstance().configParamsWithType(type);
        this.size = new int[ParamConfig.getInstance().getNumOfDimensions()];

    }


    /**
     * This function will create the scenario
     */
    private void createScenario()
    {
        // TODO: 23/02/2020 Create the scenario . assign agents to their nodes
        
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
}
