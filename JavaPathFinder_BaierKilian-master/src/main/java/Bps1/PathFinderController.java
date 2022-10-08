package Bps1;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;


import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.TreeSet;

public class PathFinderController implements Initializable {
    boolean setStart = false;
    boolean setZiel = false;
    @FXML
    private AnchorPane anchorpane;

    @FXML
    private Canvas canvas;

    @FXML
    private CheckBox checkBox;

    @FXML
    private Slider fxSlider;

    @FXML
    private RadioButton zielRadioButton;

    @FXML
    private ToggleGroup RadioButon;

    @FXML
    private RadioButton lightBarrierRadioButton;

    @FXML
    private RadioButton startRadioButton;

    @FXML
    private RadioButton loeschenRadioButton;

    @FXML
    private RadioButton radioButonBarrier;
    ArrayList<Node> allNodes = new ArrayList<>();

    public void clearCanvas() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getWidth());
    }

    public void drawGrid(int size) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setLineWidth(2);
        gc.setFill(Color.WHITE);
        gc.fillRect(0,0,canvas.getWidth(), canvas.getHeight());
        for (int i = 0; i <= canvas.getWidth(); i += size) {
            for (int j = 0; j <= canvas.getHeight(); j += size) {
                gc.strokeRect(0, 0, i, j);
                allNodes.add(new Node(i, j, NodeStates.EMPTY,  (int) fxSlider.getValue()));
            }
        }
    }

    public void stillDraw(){
        for(Node node : allNodes){
            if(node.getNodeStates() != NodeStates.EMPTY){
                drawInGrid(node);
            }
        }
    }

    public void drawInGrid(Node node){
        Color color = Color.WHITE;
        int x = (int) ((node.getX() / fxSlider.getValue()) * fxSlider.getValue());
        int y = (int) ((node.getY() / fxSlider.getValue()) * fxSlider.getValue());

        GraphicsContext gc = canvas.getGraphicsContext2D();
        if(node.getNodeStates() == NodeStates.BARRIER){
            color = Color.GREY;
        }
        else if(node.getNodeStates() == NodeStates.LIGHTBARRIER){
            color = Color.DARKGRAY;
        }
        else if(node.getNodeStates() == NodeStates.TARGET){
            color = Color.RED;
        }
        else if(node.getNodeStates() == NodeStates.PLAYER){
            color = Color.GREEN;
        }
        gc.setFill(color);
        gc.fillRect(x+1,y+1,fxSlider.getValue() - 2, fxSlider.getValue() - 2);
    }





    public void drawInGrid(Color color,  javafx.scene.input.MouseEvent mouseEvent, NodeStates nodeStates) {
        int x = (int) ((int) (mouseEvent.getX() / fxSlider.getValue()) * fxSlider.getValue());
        int y = (int) ((int) (mouseEvent.getY() / fxSlider.getValue()) * fxSlider.getValue());
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(color);
        gc.fillRect(x+1,y+1,fxSlider.getValue() - 2, fxSlider.getValue() - 2);
        Node node = new Node(x,y, nodeStates, (int) fxSlider.getValue() );
        if(allNodes.contains(node)){
            int idx = allNodes.indexOf(node);
            if(allNodes.get(idx).getNodeStates() == NodeStates.PLAYER){
                setStart = false;
            }
            if(allNodes.get(idx).getNodeStates() == NodeStates.TARGET){
                setZiel = false;
            }
            allNodes.set(idx, node);
        }
        System.out.println(allNodes);
    }

    public void delteWrongRowsCollumns(int maxRow, int maxColumn){
        ArrayList<Node> toDel = new ArrayList<>();
        for(Node node : allNodes){
            if(node.getRow() > maxRow ){
                toDel.add(node);
            }
            else if(node.getCollum() > maxColumn){
                toDel.add(node);
            }
        }
        allNodes.removeAll(toDel);
    }


    public void drawInNode(){
        canvas.setOnMouseDragged(mouseEvent -> {
            if(radioButonBarrier.isSelected()) {
                drawInGrid(Color.GREY, mouseEvent, NodeStates.BARRIER);
            }
            else if(lightBarrierRadioButton.isSelected()){
                    drawInGrid(Color.DARKGRAY, mouseEvent, NodeStates.LIGHTBARRIER);
                }

            else if(loeschenRadioButton.isSelected()){
                drawInGrid(Color.WHITE, mouseEvent, NodeStates.EMPTY);
            }
            });

        canvas.setOnMouseClicked(mouseEvent -> {
            if(radioButonBarrier.isSelected()) {
                drawInGrid(Color.GREY, mouseEvent, NodeStates.BARRIER);
            }
            else if(lightBarrierRadioButton.isSelected()){
                drawInGrid(Color.DARKGRAY, mouseEvent, NodeStates.LIGHTBARRIER);
            }
            else if(loeschenRadioButton.isSelected()){
                drawInGrid(Color.WHITE, mouseEvent, NodeStates.EMPTY);
            }
            if(setStart == false) {
                if (startRadioButton.isSelected()) {
                    drawInGrid(Color.GREEN, mouseEvent, NodeStates.PLAYER);
                    setStart = true;
                }
            }
            if(setZiel == false) {
                if (zielRadioButton.isSelected()) {
                    drawInGrid(Color.RED, mouseEvent, NodeStates.TARGET);
                    setZiel = true;
                }
            }

        });
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        stillDraw();
        drawInNode();
        checkBox.selectedProperty().addListener((o, oldV, newV) -> {
            if (checkBox.isSelected()) {
                drawGrid((int) fxSlider.getValue());
                stillDraw();
                int maxRow = (int) (canvas.getWidth() / fxSlider.getValue() - 1);
                int maxColumn = (int) (canvas.getHeight() / fxSlider.getValue() - 1);
                delteWrongRowsCollumns(maxRow, maxColumn);
                fxSlider.valueProperty().addListener((observableValue, number, t1) -> {
                    if (!fxSlider.isValueChanging()) {
                        clearCanvas();
                        drawGrid((int) fxSlider.getValue());
                        stillDraw();
                    }
                });
            }
                else{
                    clearCanvas();
                    stillDraw();
                }
            });
        }
    }