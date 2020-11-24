import Components.*;
import Controller.Controller;
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

public class Main extends Application {

    public static void main(String[] args)

    {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {


        FXMLLoader fxmlLoader = new FXMLLoader();
        //String path = System.getProperty("user.dir")+"\\src\\View\\view.fxml";
        String path = "View/view.fxml";
        Parent root = fxmlLoader.load(getClass().getResource(path).openStream());
        View view = fxmlLoader.getController();
        primaryStage.setTitle("BOS");
        double ratio = 975 / 1600.0;
        double width = 1200;
        double height = width * ratio;

        primaryStage.setScene(new Scene(root, width, height));
        primaryStage.show();

        Controller controller = new Controller(view);

    }
}
