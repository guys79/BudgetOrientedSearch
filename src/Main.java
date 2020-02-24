import Components.Heuristics.UniformCostSearch;
import Components.Node;
import Components.Problem;

import java.util.Set;

public class Main {
    public static void main(String[] args) {
        Problem.getInstance().setNewProblem("",1,1,1,1);
        UniformCostSearch uniformCostSearch = new UniformCostSearch();
        int [] loc1 = {0,0};
        int [] loc2 = {3,2};

        Node start = new Node(loc1);
        Node goal = new Node(loc2);

        double res = uniformCostSearch.getHeuristic(start,goal);
        double sqrt2 = Math.sqrt(2);
        int counter = 0;
        System.out.println(res);
        while(res>=sqrt2 && Math.abs(res - (Math.round(res))*1.0) > 0.001)
        {
            res-=sqrt2;
            counter++;
        }
        System.out.println("moved "+counter+" times diagonally and "+Math.round(res) +" times normally");

    }
}
