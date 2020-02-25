package Components;

import Components.BudgetDistributionPolicy.EqualBudgetDistributionPolicy;
import Components.BudgetDistributionPolicy.IBudgetDistributionPolicy;
import Components.CostFunction.ICostFunction;
import Components.CostFunction.OctileGridFunction;
import Components.Heuristics.IHeuristic;
import Components.Heuristics.PureOctileDistance;
import Components.PriorityPolicy.EqualPriorityPolicy;
import Components.PriorityPolicy.IPriorityPolicy;

/**
 * This class is responsible to configure the search algorithm with the params
 * This class uses the Singleton design pattern
 */
public class ParamConfig {
    int type;//The type of config
    private ICostFunction costFunction;//The cost function
    private IHeuristic heuristic;//The heuristic function
    private IBudgetDistributionPolicy budgetDistributionPolicy;//The budget distribution policy
    private IPriorityPolicy priorityPolicy;//The priority policy
    private static ParamConfig instance;//The instance
    private final int numOfDimensions=2;//The number of dimensions;
    private final int numberOfAxisLegalToMoveInOneTurn = 2;


    /**
     * The private constructor
     */
    private ParamConfig() {
    }

    /**
     * The function that configs the parameters according to the given type
     * @param type - The given type
     */
    public void configParamsWithType(int type) {
        this.type = type;

        switch (type)
        {
            case 1:
                costFunction = new OctileGridFunction();
                heuristic = new PureOctileDistance();
                priorityPolicy = new EqualPriorityPolicy();
                budgetDistributionPolicy = new EqualBudgetDistributionPolicy();
                break;

            default:
                costFunction = null;
                heuristic = null;
                priorityPolicy = null;
                budgetDistributionPolicy = null;
        }
        if(checkIfNull())
            throw new UnsupportedOperationException("Some of the params are null in ParamConfig Class");
    }

    /**
     * This function will check if one of the parameters is null
     * @return - True IFF one of the parameters is null
     */
    private boolean checkIfNull()
    {
        return this.costFunction == null || this.heuristic == null || this.priorityPolicy == null || this.budgetDistributionPolicy == null;
    }

    /**
     * This function will return the instance
     * @return - The given instance
     */
    public static ParamConfig getInstance()
    {
        if(instance == null)
            instance = new ParamConfig();
        return instance;
    }

    /**
     * This function will return the number of axis the agent can move simultaneously in the same turn
     * @return - The number of axis the agent can move simultaneously in the same turn
     */
    public int getNumberOfAxisLegalToMoveInOneTurn() {
        return numberOfAxisLegalToMoveInOneTurn;
    }

    /**
     * TYhis function will return the number of dimensions
     * @return - The number of dimensions
     */
    public int getNumOfDimensions() {
        return numOfDimensions;
    }

    /**
     * This function will return the cost function
     * @return - The cost function
     */
    public ICostFunction getCostFunction()
    {
        return costFunction;
    }

    /**
     * This function will return the heuristic function
     * @return - The heuristic function
     */
    public IHeuristic getHeuristic() {
        return heuristic;
    }

    /**
     * This function will return the config type
     * @return - The config type
     */
    public int getType() {
        return type;
    }

    /**
     * This function will return the budget distribution policy
     * @return - The budget distribution policy
     */
    public IBudgetDistributionPolicy getBudgetDistributionPolicy() {
        return budgetDistributionPolicy;
    }

    /**
     * This function will return the priority policy
     * @return - The priority policy
     */
    public IPriorityPolicy getPriorityPolicy() {
        return priorityPolicy;
    }
}
