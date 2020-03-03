import Components.*;
import Components.Heuristics.HeuristicWithPersonalDatabase;
import Components.Heuristics.IHeuristic;
import Components.Heuristics.PureOctileDistance;
import Components.Heuristics.UniformCostSearch;
import SearchAlgorithms.BudgetOrientedSearch;
import SearchAlgorithms.IMultiAgentSearchAlgorithm;
import View.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.*;

public class Main  extends Application{

    public static void main(String[] args)

    {
        launch(args);
    }

    public void init(Controller controller)
    {
        String mapName = "Berlin_1_256";
        //String mapName = "random-32-32-10";
        //String mapName = "empty-8-8";
        int scenario = 1;
        int type = 1;
        int prefixLength =5;
        int totalBudget = 100;
        int numOfAgents = 1;
        Problem.getInstance().setNewProblem(mapName,scenario,type,prefixLength,totalBudget,numOfAgents);
        int height = Problem.getInstance().getSize()[0];
        int width = Problem.getInstance().getSize()[1];
        int [] loc;

        Set<Agent>agents = Problem.getInstance().getAgents();
        for(Agent agent: agents)
            System.out.println(agent);

        IMultiAgentSearchAlgorithm searchAlgorithm = new BudgetOrientedSearch();



        Map<Agent,Prefix> solutions = searchAlgorithm.getSolution(controller);
        if(solutions!=null) {
            for (Map.Entry<Agent, Prefix> entry : solutions.entrySet()) {
                System.out.println(entry.getKey());
                System.out.println(entry.getValue());
            }
        }

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        //String path = System.getProperty("user.dir")+"\\src\\View\\view.fxml";
        String path = "View/view.fxml";
        Parent root = fxmlLoader.load(getClass().getResource(path).openStream());
        Controller controller = fxmlLoader.getController();
        primaryStage.setTitle("BOS");
        primaryStage.setScene(new Scene(root, 800, 540));
        primaryStage.show();

        init(controller);

    }
}
