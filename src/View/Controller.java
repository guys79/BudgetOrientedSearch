package View;

import Components.Prefix;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.List;

/**
 * For rendering the GUI. This class is not fully commented.
 */
public class Controller {

    @FXML
    public Text timeText;
    public Canvas canvas;
    public Button forwardButton;
    public Slider slider;
    public Button backButton;

    public GraphicsContext context;
    public IntegerProperty time = new SimpleIntegerProperty();
    public int[][] grid;
    public HashMap<Integer, int[]> nodeLocations = new HashMap<>();
    public HashMap<int[], Color> paths = new HashMap<>();
    private int agentCount = 0;
    private Color[] colors = {
            Color.RED,
            Color.DARKRED,
            Color.ORANGE,
            Color.DARKORANGE,
            Color.GOLDENROD,
            Color.DARKGOLDENROD,
            Color.GREEN,
            Color.DARKGREEN,
            Color.BLUE,
            Color.DARKBLUE
    };
    private double cellWidth;
    private double cellHeight;
    private int maxTime = 0;

    public void initialize(int[][] grid){
        slider.setBlockIncrement(1);
        slider.valueProperty().addListener((ChangeListener) (arg0, arg1, arg2) -> {
//            time.setValue((int)(slider.getValue()/100*maxTime));
            time.setValue((int)slider.getValue());
        });
        time.addListener((ChangeListener) (arg0, arg1, arg2) -> {
            draw();
        });
        addGrid(grid);
        context = canvas.getGraphicsContext2D();
        cellWidth = canvas.getWidth()/grid[0].length;
        cellHeight = canvas.getHeight()/grid.length;
    }

    public void draw(){
        timeText.setText("t = "+time.getValue());
        drawGrid();
        drawAgents();
    }

    private void drawGrid(){
        context.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                if (grid[row][col] == -1) context.setFill(Color.BLACK);
                else context.setFill(Color.WHITE);
                context.fillRect(col * cellWidth, row * cellHeight, cellWidth+1, cellHeight+1);
            }
        }
    }

    private void drawAgents(){
        if (grid == null) return;
        drawGrid();
        for (HashMap.Entry<int[], Color> entry : paths.entrySet()){
            int[] path = entry.getKey();
            int nodeID = getNode(path, time.getValue());
            int[] pos = idToLoc(nodeID);
            context.setFill(entry.getValue());
            context.fillOval(pos[0] * cellWidth, pos[1] * cellHeight, cellWidth, cellHeight);
        }
    }

    private int getNode(int[] path, int t) {
        if (t > path.length-1) return path[path.length-1];
        else return path[t];
    }

    public void addAgent(int[] path){
        paths.put(path, colors[agentCount++ % colors.length]);
        if (path.length-1 > maxTime){
            maxTime = path.length-1;
            slider.setMax(maxTime);
        }
    }
    public void addAgent(Prefix prefix){
        int [] pathArr = new int[prefix.getSize()];

        int [] loc;
        for(int i=0;i<pathArr.length;i++)
        {
            loc = prefix.getNodeAt(i).getCoordinates();
            pathArr[i] = locToId(loc[0],loc[1]);
        }
        addAgent(pathArr);
    }
    private int locToId(int x, int y)
    {
        int width = this.grid[0].length;
        int id = width*x+y;
        return id;
    }
    private int []  idToLoc(int id)
    {
        int width = this.grid[0].length;
        int [] loc = new int[2];
        loc[0] = id/width;
        loc[1] = id%width;
        return loc;
    }
    private void addGrid(int[][] grid){
        this.grid = grid;
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                if (grid[row][col] != -1){
                    int[] pos = {col, row};
                    nodeLocations.put(grid[row][col], pos);
                }
            }
        }
    }

    public void timeForward() {
        if (time.getValue() < maxTime){
            updateSlider(1);
        }
    }

    public void timeBackward() {
        if (time.getValue() > 0){
            updateSlider(-1);
        }
    }

    private void updateSlider(int delta){
        slider.setValue(slider.getValue()+delta);
    }
}