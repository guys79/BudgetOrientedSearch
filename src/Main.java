import Components.*;
import Components.Heuristics.HeuristicWithPersonalDatabase;
import Components.Heuristics.IHeuristic;
import Components.Heuristics.PureOctileDistance;
import Components.Heuristics.UniformCostSearch;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) {

        Agent agent1 = new Agent(null,null);
        Agent agent2 = new Agent(null,null);

        int [] loc1 = {0,0};
        int [] loc2 = {0,1};
        int [] loc3 = {0,2};
        int [] loc4 = {1,1};

        Node node1 = new Node(loc1);
        Node node2 = new Node(loc2);
        Node node3 = new Node(loc3);
        Node node4 = new Node(loc4);

        List<Node> list1 = new ArrayList<>();
        List<Node> list2 = new ArrayList<>();

        list1.add(node1);
        list1.add(node2);
        list1.add(node3);




        list2.add(node2);
        list2.add(node1);
        list2.add(node4);


        Prefix p1 = new Prefix(list1,agent1);
        Prefix p2 = new Prefix(list2,agent2);

        Set<Prefix> prefixes = new HashSet<>();
        prefixes.add(p1);
        prefixes.add(p2);

        System.out.println(SolutionChecker.getInstance().checkSolution(prefixes));

    }
}
