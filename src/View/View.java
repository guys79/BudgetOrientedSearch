package View;

import Components.Agent;
import Components.Prefix;
import Controller.Controller;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.List;

/**
 * For rendering the GUI. This class is not fully commented.
 */
public class View {

    @FXML
    public Text timeText;
    public Canvas canvas;
    public Button forwardButton;
    public Slider slider;
    public Button backButton;
    public Button nextButton;
    public TextField enter_x_textField;
    public TextField enter_y_textField;
    public TextField acenNumber;
    public TextField mark_agent_id;
    public GraphicsContext context;
    public IntegerProperty time = new SimpleIntegerProperty();
    public int[][] grid;
    public HashMap<Integer, int[]> nodeLocations = new HashMap<>();
    public HashMap<int[], Pair<Color, Agent>> paths = new HashMap<>();
    private int markedAgent;
    private int scenNum;
    private int marked_x;
    private int marked_y;
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
    private Controller controller;
    public void initialize(int[][] grid){
        this.marked_x = -1;
        this.marked_y = -1;
        this.markedAgent = -1;

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

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void unMark()
    {
        if(this.marked_x != -1 && this.marked_y!=-1)
        {
            unMark(this.marked_x,this.marked_y);
            this.marked_x = -1;
            this.marked_y = -1;
        }

    }


    public void setScenario()
    {
        boolean toSetScene;

        try {
            scenNum = Integer.parseInt(this.acenNumber.getText());

            toSetScene = true;
        }
        catch (NumberFormatException e)
        {
            toSetScene = false;
        }
        if(toSetScene)
            controller.performSingleRun(this.scenNum);
    }
    public void draw(){
        timeText.setText("t = "+time.getValue());
        drawGrid();
        drawAgents();
    }
    /**
     * This function will unMark the cell at x,y
     * @param x - The x coordinates
     * @param y - The y coordinates
     */
    private void unMark(int x, int y)
    {
        if(grid[x][y] == -1)
            context.setFill(Color.BLACK);
        else
            context.setFill(Color.WHITE);

        context.fillRect(y * cellWidth, x * cellHeight, cellWidth + 1, cellHeight + 1);
    }
    /**
     * This function will implement the functionallity of the Marj button (The onclick event)
     */
    public void markButton()
    {

        boolean toMark;
        int x = -1,y = -1;
        try {
            x = Integer.parseInt(this.enter_x_textField.getText());
            y = Integer.parseInt(this.enter_y_textField.getText());
            toMark = true;
        }
        catch (NumberFormatException e)
        {
            toMark = false;
        }

        if(toMark)
        {
            marked_x = x;
            marked_y = y;
            mark(marked_x,marked_y);
        }

        this.enter_y_textField.setText("");
        this.enter_x_textField.setText("");
    }
    /**
     * This function will mark the cell at x,y
     * @param x - The x coordinates
     * @param y - The y coordinates
     */
    public void mark (int x,int y)
    {

        if(x<0 || x>= grid.length)
            return;
        if(y<0 || y>=grid[x].length)
            return;
        context.setFill(Color.BLUE);
        context.fillRect(y * cellWidth, x * cellHeight, cellWidth + 1, cellHeight + 1);
        if(grid[x][y] == -1)
            context.setFill(Color.BLACK);
        else
            context.setFill(Color.WHITE);
        context.fillRect(y * cellWidth+(1.0/12)*cellWidth, x * cellHeight+(1.0/12)*cellHeight, cellWidth *5.0/6, cellHeight *5.0/6);

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
    public void markAgent()
    {
        try {
            markedAgent = Integer.parseInt(this.mark_agent_id.getText());
        }
        catch (Exception e)
        {
            markedAgent = -1;
        }
        drawAgents();
    }
    private void drawAgents(){
        if (grid == null) return;
        drawGrid();
        for (HashMap.Entry<int[], Pair<Color,Agent>> entry : paths.entrySet()){
            int[] path = entry.getKey();
            int nodeID = getNode(path, time.getValue());
            //int initialId = getNode(path, 0);
            int endId = getNode(path, path.length-1);
            int[] pos = idToLoc(nodeID);
            //int[] initialPos = idToLoc(initialId);
            int[] endPos = idToLoc(endId);
            Agent agent;
            if(endId != nodeID) {
                agent = entry.getValue().getValue();
                context.setFill(entry.getValue().getKey());
                if(markedAgent != -1 && markedAgent != agent.getId())
                {
                    context.setFill(Color.GRAY);

                }
                context.fillOval(pos[1] * cellWidth, pos[0] * cellHeight, cellWidth, cellHeight);
                // context.fillOval(initialPos[1] * cellWidth, initialPos[0] * cellHeight, cellWidth, cellHeight);

   //             context.fillOval(endPos[1] * cellWidth, endPos[0] * cellHeight, cellWidth, cellHeight);
            }
            else
            {

                context.setFill(Color.GRAY);
                context.fillOval(pos[1] * cellWidth, pos[0] * cellHeight, cellWidth, cellHeight);
            }


        }

    }

    private int getNode(int[] path, int t) {
        if (t > path.length-1) return path[path.length-1];
        else return path[t];
    }

    public void addAgent(int[] path,Agent agent){
        paths.put(path, new Pair<>(colors[agentCount++ % colors.length],agent));
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
        addAgent(pathArr,prefix.getAgent());
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