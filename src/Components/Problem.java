package Components;

import java.util.Set;

public class Problem {
    private String mapName;
    private int scenario;
    private int type;
    private Graph graph;
    private Set<Agent> agents;
    private int prefix;
    private int totalBudget;

    public Problem(String mapName, int scenario, int type,int prefix,int totalBudget) {
        this.mapName = mapName;
        this.scenario = scenario;
        this.type = type;
        this.totalBudget = totalBudget;
        this.prefix = prefix;
        ParamConfig.getInstance().configParamsWithType(type);
        this.graph = buildGraph();
    }

    private Graph buildGraph()
    {
        Graph graph = null;
        return graph;
    }


}
