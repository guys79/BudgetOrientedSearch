import Components.*;
import Components.Heuristics.HeuristicWithPersonalDatabase;
import Components.Heuristics.IHeuristic;
import Components.Heuristics.PureOctileDistance;
import Components.Heuristics.UniformCostSearch;
import SearchAlgorithms.BudgetOrientedSearch;
import SearchAlgorithms.IMultiAgentSearchAlgorithm;

import java.util.*;

public class Main {
    public static void main(String[] args) {


        //String mapName = "Berlin_1_256";
        //String mapName = "random-32-32-10";
        String mapName = "empty-8-8";
        int scenario = 2;
        int type = 1;
        int prefixLength =4;
        int totalBudget = 15;
        int numOfAgents = 1;
        Problem.getInstance().setNewProblem(mapName,scenario,type,prefixLength,totalBudget,numOfAgents);
        int height = Problem.getInstance().getSize()[0];
        int width = Problem.getInstance().getSize()[1];
        int [] loc;
        for(int i=0; i<height;i++)
        {
            for(int j=0; j<width;j++)
            {
                loc = new int[2];
                loc[0] = i;
                loc[1] = j;
                if(Problem.getInstance().isValidLocation(loc))
                    System.out.print('.');
                else
                    System.out.print('@');
            }
            System.out.println();
        }

        Set<Agent>agents = Problem.getInstance().getAgents();
        for(Agent agent: agents)
            System.out.println(agent);

        IMultiAgentSearchAlgorithm searchAlgorithm = new BudgetOrientedSearch();


        Map<Agent,Prefix> solutions = searchAlgorithm.getSolution();
        if(solutions!=null) {
            for (Map.Entry<Agent, Prefix> entry : solutions.entrySet()) {
                System.out.println(entry.getKey());
                System.out.println(entry.getValue());
            }
        }


    }
}
