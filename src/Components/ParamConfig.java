package Components;

import Components.BudgetDistributionPolicy.EqualBudgetDistributionPolicy;
import Components.BudgetDistributionPolicy.IBudgetDistributionPolicy;
import Components.CostFunction.ICostFunction;
import Components.CostFunction.OctileGridFunction;
import Components.Heuristics.IHeuristic;
import Components.Heuristics.PureOctileDistance;
import Components.PriorityPolicy.EqualPriorityPolicy;
import Components.PriorityPolicy.IPriorityPolicy;

public class ParamConfig {
    int type;
    private final String filePaths = "";
    private ICostFunction costFunction;
    private IHeuristic heuristic;
    private IBudgetDistributionPolicy budgetDistributionPolicy;
    private IPriorityPolicy priorityPolicy;
    private static ParamConfig instance;

    private ParamConfig() {
    }

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
    }

    public static ParamConfig getInstance()
    {
        if(instance == null)
            instance = new ParamConfig();
        return instance;
    }

    public ICostFunction getCostFunction()
    {
        return costFunction;
    }

    public IHeuristic getHeuristic() {
        return heuristic;
    }

    public int getType() {
        return type;
    }

    public String getFilePaths() {
        return filePaths;
    }
}
