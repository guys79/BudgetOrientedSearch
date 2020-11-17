package Components;

import Components.BoundedSingleSearchAlgorithms.ALSSLRTAStar;
import Components.BoundedSingleSearchAlgorithms.IBoundedSingleSearchAlgorithm;
import Components.BudgetDIstributionPolicy.*;
import Components.CostFunction.ICostFunction;
import Components.CostFunction.OctileGridFunction;
import Components.FailPolicy.AdditionAttempt;
import Components.FailPolicy.IFailPolicy;
import Components.Heuristics.HeuristicWithPersonalDatabase;
import Components.Heuristics.IHeuristic;
import Components.Heuristics.PreComputedUniformCostSearch;
import Components.Heuristics.PureOctileDistance;
import Components.PriorityPolicy.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private IFailPolicy failPolicy;//The fail policy
    private IBoundedSingleSearchAlgorithm searchAlgorithm;
    private static ParamConfig instance;//The instance
    private final int numOfDimensions=2;//The number of dimensions;
    private final int numberOfAxisLegalToMoveInOneTurn = 2;//The number of axis that the agent can move simultaneously in a single move
    private Boolean backtrack;
    private Boolean performDeepLookahead;
    private Boolean isSharedBudget;
    private Boolean isGoalLessPriority;



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
        System.out.println("New config");
        switch (type)
        {

            case 1:
                costFunction = new OctileGridFunction();
                //heuristic = new HeuristicWithPersonalDatabase(new PureOctileDistance());
                heuristic = new PreComputedUniformCostSearch(new PureOctileDistance());
                priorityPolicy = new PriorityBadPointFavorPolicy();
                budgetDistributionPolicy = new BudgetExponentialBadpointsFavorPolicy();
                searchAlgorithm = new ALSSLRTAStar(costFunction,(HeuristicWithPersonalDatabase)heuristic);
                this.backtrack = true;
                performDeepLookahead = false;
                isSharedBudget = false;
                isGoalLessPriority = true;
                failPolicy = AdditionAttempt()
                break;

            case 2:
                costFunction = new OctileGridFunction();
                //heuristic = new HeuristicWithPersonalDatabase(new PureOctileDistance());
                heuristic = new PreComputedUniformCostSearch(new PureOctileDistance());
                priorityPolicy = new ConstraintPriorityPolicy();
                budgetDistributionPolicy = new EqualBudgetDistributionPolicy();
                searchAlgorithm = new ALSSLRTAStar(costFunction,(HeuristicWithPersonalDatabase)heuristic);
                this.backtrack = true;
                performDeepLookahead = false;
                isSharedBudget = false;
                isGoalLessPriority = true;
                break;
            case 3:
                costFunction = new OctileGridFunction();
                //heuristic = new HeuristicWithPersonalDatabase(new PureOctileDistance());
                heuristic = new PreComputedUniformCostSearch(new PureOctileDistance());
                priorityPolicy = new ConstraintPriorityPolicy();
                budgetDistributionPolicy = new BudgetExponentialBadpointsFavorPolicy();
                searchAlgorithm = new ALSSLRTAStar(costFunction,(HeuristicWithPersonalDatabase)heuristic);
                this.backtrack = true;
                performDeepLookahead = false;
                isSharedBudget = false;
                isGoalLessPriority = true;
                break;

            default:
                costFunction = null;
                heuristic = null;
                priorityPolicy = null;
                budgetDistributionPolicy = null;
                searchAlgorithm = null;
                backtrack = null;
                performDeepLookahead =null;
                isSharedBudget = null;
                isGoalLessPriority = null;
                failPolicy = null;
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
        return this.costFunction == null || this.heuristic == null || this.priorityPolicy == null || this.budgetDistributionPolicy == null || searchAlgorithm ==null|| backtrack == null || performDeepLookahead == null || isSharedBudget == null || this.isGoalLessPriority == null || this.failPolicy == null;
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
     * This function will return true if agents at the goal node get less priority
     * @return - True if agents at the goal node get less priority
     */
    public Boolean getGoalLessPriority() {
        return isGoalLessPriority;
    }

    /**
     * This function will return the bounded search algorithm
     * @return - The bounded search algorithm
     */
    public IBoundedSingleSearchAlgorithm getSearchAlgorithm() {
        return searchAlgorithm;
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
     * This function will return True if the algorithm preforms backtracking
     * @return - True IFF the algorithm preforms backtracking
     */
    public Boolean getBacktrack() {
        return backtrack;
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

    public Boolean getPerformDeepLookahead() {
        return performDeepLookahead;
    }

    public Boolean getSharedBudget() {
        return isSharedBudget;
    }

    @Override
    public String toString() {
        return "ParamConfig{" +
                "type=" + type +
                ", costFunction=" + costFunction +
                ", heuristic=" + heuristic +
                ", budgetDistributionPolicy=" + budgetDistributionPolicy +
                ", priorityPolicy=" + priorityPolicy +
                ", searchAlgorithm=" + searchAlgorithm +
                ", numOfDimensions=" + numOfDimensions +
                ", numberOfAxisLegalToMoveInOneTurn=" + numberOfAxisLegalToMoveInOneTurn +
                ", backtrack=" + backtrack +
                ", performDeepLookahead=" + performDeepLookahead +
                ", isSharedBudget=" + isSharedBudget +
                ", isGoalLessPriority=" + isGoalLessPriority +
                '}';
    }

    public Map<String,String> getParams()
    {
        Map<String,String> params = new HashMap<>();
        params.put("type",""+type);
        params.put("costFunction",""+costFunction);
        params.put("heuristic",""+heuristic);
        params.put("budgetDistributionPolicy",""+budgetDistributionPolicy);
        params.put("priorityPolicy",""+priorityPolicy);
        params.put("searchAlgorithm",""+searchAlgorithm);
        params.put("numOfDimensions",""+numOfDimensions);
        params.put("numberOfAxisLegalToMoveInOneTurn",""+numberOfAxisLegalToMoveInOneTurn);
        params.put("backtrack",""+backtrack);
        params.put("performDeepLookahead",""+performDeepLookahead);
        params.put("isSharedBudget",""+isSharedBudget);
        params.put("isGoalLessPriority",""+isGoalLessPriority);
        return params;
    }

    public IFailPolicy getFailPolicy() {
        return this.failPolicy;
    }
}
