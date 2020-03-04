import Components.*;
import SearchAlgorithms.BudgetOrientedSearch;
import SearchAlgorithms.IMultiAgentSearchAlgorithm;
import View.View;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sun.misc.Perf;

import java.util.*;

public class Main  extends Application{

    public static void main(String[] args)

    {
        launch(args);
    }

    public void init(View view)
    {
        // TODO: 04/03/2020 check colissions and swaps 
        //String mapName = "Berlin_1_256";
        String mapName = "random-32-32-10";
        //String mapName = "empty-8-8";
        int scenario = 1;
        int type = 1;
        int prefixLength =5;
        int numOfAgents = 50 ;
        int budgetPerAgent = 200;
        int totalBudget = numOfAgents*budgetPerAgent;

        Problem.getInstance().setNewProblem(mapName,scenario,type,prefixLength,totalBudget,numOfAgents);
        int height = Problem.getInstance().getSize()[0];
        int width = Problem.getInstance().getSize()[1];
        int [] loc;

        Set<Agent>agents = Problem.getInstance().getAgents();
        for(Agent agent: agents)
            System.out.println(agent);

        IMultiAgentSearchAlgorithm searchAlgorithm = new BudgetOrientedSearch();


        long before = System.currentTimeMillis();
        Map<Agent,Prefix> solutions = searchAlgorithm.getSolution(view);
        long after = System.currentTimeMillis();
        if(solutions!=null) {
            for (Map.Entry<Agent, Prefix> entry : solutions.entrySet()) {
                System.out.println(entry.getKey());
       //         System.out.println(entry.getValue());
            }
        }
        PerformanceTracker.getInstance().setOverAllSearch(after-before);
        long preCompute = PerformanceTracker.getInstance().getPreCompute();
        long overAllTime = PerformanceTracker.getInstance().getOverAllSearch();
        long searchTimeOnly = PerformanceTracker.getInstance().getSearchTimeNeto();
        System.out.println("Pre computing time "+preCompute+" ms ,"+preCompute/1000.0 +" s");
        System.out.println("Over all time "+overAllTime+" ms ,"+overAllTime/1000.0 +" s");
        System.out.println("Search Time time "+searchTimeOnly+" ms ,"+searchTimeOnly/1000.0 +" s");

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        //String path = System.getProperty("user.dir")+"\\src\\View\\view.fxml";
        String path = "View/view.fxml";
        Parent root = fxmlLoader.load(getClass().getResource(path).openStream());
        View view = fxmlLoader.getController();
        primaryStage.setTitle("BOS");
        primaryStage.setScene(new Scene(root, 800, 540));
        primaryStage.show();

        init(view);

    }
}
