import Components.Agent;
import Components.Heuristics.HeuristicWithPersonalDatabase;
import Components.Heuristics.IHeuristic;
import Components.Heuristics.PureOctileDistance;
import Components.Heuristics.UniformCostSearch;
import Components.Node;
import Components.Problem;

import java.util.Set;

public class Main {
    public static void main(String[] args) {

        int [] loc1 = {0,0};
        int [] loc2 = {3,2};

        Node start = new Node(loc1);
        Node goal = new Node(loc2);
        Agent agent1 = new Agent(start,goal);
        Agent agent2 = new Agent(start,goal);

        HeuristicWithPersonalDatabase heuristic = new HeuristicWithPersonalDatabase(new PureOctileDistance());
        double res = heuristic.getHeuristic(start,goal,agent1);
        System.out.println(res);
        res = heuristic.getHeuristic(start,goal,agent1);
        System.out.println(res);
        heuristic.storeNewVal(agent1,start,goal,2d);
        res = heuristic.getHeuristic(start,goal,agent1);
        System.out.println(res);

        res = heuristic.getHeuristic(goal,start,agent2);
        System.out.println(res);
        res = heuristic.getHeuristic(goal,start,agent2);
        System.out.println(res);
        heuristic.storeNewVal(agent2,goal,start,1d);
        res = heuristic.getHeuristic(goal,start,agent2);
        System.out.println(res);

        res = heuristic.getHeuristic(goal,goal,agent2);
        System.out.println(res);

    }
}
