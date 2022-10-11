package Bps1;


import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;


import java.net.URL;
import java.util.*;

public class PathFinderController implements Initializable {
    private boolean setStart = false;
    private boolean setZiel = false;

    @FXML
    private VBox vBoxFXML;

    @FXML
    private Canvas canvas;

    @FXML
    private Button clearButton;

    @FXML
    private CheckBox checkBox;

    @FXML
    private RadioButton loeschenRadioButton;

    @FXML
    private ToggleGroup RadioButon;

    @FXML
    private RadioButton radioButonBarrier;

    @FXML
    private RadioButton startRadioButton;

    @FXML
    private RadioButton lightBarrierRadioButton;

    @FXML
    private RadioButton zielRadioButton;

    @FXML
    private Slider fxSlider;
    private final ArrayList<Node> allNodes = new ArrayList<>();
    private final ArrayList<Node> rightNodes = new ArrayList<>();
    int id = 0;

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
                allNodes.add(new Node(i, j, NodeStates.EMPTY,  (int) fxSlider.getValue(), id));
            }
        }
        int maxRow = (int) (canvas.getWidth() / 24 - 1);
        int maxColumn = (int) (canvas.getHeight() / 24 - 1);
        delteWrongRowsCollumns(maxRow,maxColumn);
        for(Node node : allNodes){
            if(allNodes.contains(node)){
                rightNodes.add(new Node(node.getX(), node.getY(), node.getNodeStates(), (int) fxSlider.getValue(), id));
                id++;
            }
        }
        getNeighbors();
    }


    public Node getRowsAndColls(int x, int y){
        for(Node node : rightNodes){
            if(x == node.getCollum() && y == node.getRow()){
                return node;
            }
        }
        return null;
    }

    public void getNeighbors(){
        for(Node node : rightNodes){
                ArrayList<Node> neighbors = new ArrayList<>();
                neighbors.add(getRowsAndColls(node.getCollum(), node.getRow() + 1 ));
                neighbors.add(getRowsAndColls(node.getCollum(), node.getRow() - 1 ));
                neighbors.add(getRowsAndColls(node.getCollum() + 1 , node.getRow()));
                neighbors.add(getRowsAndColls(node.getCollum() - 1, node.getRow()));
                neighbors.add(getRowsAndColls(node.getCollum() - 1, node.getRow() - 1));
                neighbors.add(getRowsAndColls(node.getCollum() + 1, node.getRow() - 1));
                neighbors.add(getRowsAndColls(node.getCollum() + 1, node.getRow() + 1));
                neighbors.add(getRowsAndColls(node.getCollum() - 1, node.getRow() - 1));
                node.setNodeList(neighbors);
        }
    }
    public void setClearButton(){
        clearButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                clearCanvas();
                allNodes.clear();
                rightNodes.clear();
                drawGrid((int) fxSlider.getValue());
            }
        });
    }



    public void stillDraw(){
        for(Node node : rightNodes){
            if(node.getNodeStates() != NodeStates.EMPTY){
                drawInGrid(node);
            }
        }
    }

    public void drawInGrid(Node node){
        Color color = Color.WHITE;
        int x = (int) (node.getCollum() * fxSlider.getValue());
        int y = (int) (node.getRow() * fxSlider.getValue());
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

    public Node findNode(int x, int y){
        for(Node node : rightNodes){
            if(node.getX() == x && node.getY() == y){
                return node;
            }
        }
        return null;
    }
    public void drawInGrid(Color color,  javafx.scene.input.MouseEvent mouseEvent, NodeStates nodeStates) {
        int x = (int) ((int) (mouseEvent.getX() / fxSlider.getValue()) * fxSlider.getValue());
        int y = (int) ((int) (mouseEvent.getY() / fxSlider.getValue()) * fxSlider.getValue());
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(color);
        gc.fillRect(x+1,y+1,fxSlider.getValue() - 2, fxSlider.getValue() - 2);
        Node node = findNode(x, y);
        node.setNodeStates(nodeStates);
        if(rightNodes.contains(node)){
            int idx = rightNodes.indexOf(node);
            if(rightNodes.get(idx).getNodeStates() == NodeStates.PLAYER){
                setStart = false;
            }
            if(rightNodes.get(idx).getNodeStates() == NodeStates.TARGET){
                setZiel = false;
            }
            rightNodes.set(idx, node);
        }
        rightNodes.sort(new SortByRow());

    }

    public void delteWrongRowsCollumns(int maxRow, int maxColumn){
        ArrayList<Node> toDel = new ArrayList<>();
        for(Node node : rightNodes){
            if(node.getRow() > maxRow ){
                toDel.add(node);
            }
            else if(node.getCollum() > maxColumn){
                toDel.add(node);
            }
        }
        rightNodes.removeAll(toDel);

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
            if(!setStart ) {
                if (startRadioButton.isSelected()) {
                    drawInGrid(Color.GREEN, mouseEvent, NodeStates.PLAYER);
                    setStart = true;
                }
            }
            if(!setZiel) {
                if (zielRadioButton.isSelected()) {
                    drawInGrid(Color.RED, mouseEvent, NodeStates.TARGET);
                    setZiel = true;
                }
            }

        });
        stillDraw();
    }

    public void setCheckBoxSettings(){
        checkBox.setSelected(true);
        if(checkBox.isSelected()){
            drawGrid((int) fxSlider.getValue());
            fxSlider.valueProperty().addListener((observableValue, number, t1) -> {
                if (!fxSlider.isValueChanging()) {
                    clearCanvas();
                    drawGrid((int) fxSlider.getValue());
                    stillDraw();

                }
            });
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        drawInNode();
        setCheckBoxSettings();
        setClearButton();
        canvas.heightProperty().bind(vBoxFXML.heightProperty().subtract(180));
        canvas.widthProperty().bind(vBoxFXML.widthProperty().subtract(120));

        canvas.heightProperty().addListener(observable -> {
            drawGrid((int) fxSlider.getValue());
        });

        canvas.widthProperty().addListener(observable -> {
            drawGrid((int) fxSlider.getValue());
        });

        checkBox.selectedProperty().addListener((o, oldV, newV) -> {
            if (checkBox.isSelected()) {
                drawGrid((int) fxSlider.getValue());
                stillDraw();
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