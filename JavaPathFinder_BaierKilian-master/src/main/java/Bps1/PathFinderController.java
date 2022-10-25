package Bps1;


import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;


import java.net.URL;
import java.util.*;

public class PathFinderController implements Initializable {
    @FXML
    public Button AStarButton;
    @FXML
    public Button DjakstraButton;
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
    private Node[][] nodes;


    /**
     * Leider hab ich die Suchalgorithmen nicht hinbekommen.
     * Hab trotzdem meine Ansätze drinnen gelassen...
     */


    public void clearCanvas() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getWidth());

    }

    public void drawGrid(int size) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setLineWidth(2);
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (int i = 0; i <= canvas.getWidth(); i += size) {
            for (int j = 0; j <= canvas.getHeight(); j += size) {
                gc.strokeRect(0, 0, i, j);
            }
        }
    }


    public void createNodes(int size) {
        nodes = new Node[(int) canvas.getWidth() / size][(int) canvas.getHeight() / size];
        for (int i = 0; i < nodes.length; i++) {
            for (int j = 0; j < nodes[i].length; j++) {
                nodes[i][j] = new Node(i, j, NodeStates.EMPTY);
            }
        }
    }

    public void resetState(int colum, int row) {
        if (nodes != null) {
            if (nodes[row][colum].getNodeState() == NodeStates.PLAYER) {
                setStart = false;
            } else if (nodes[row][colum].getNodeState() == NodeStates.TARGET) {
                setZiel = false;
            }
            nodes[row][colum].setNodeState(NodeStates.EMPTY);

        }
    }
    public void drawNodes(int size) {
        if (nodes != null) {
            GraphicsContext gc = canvas.getGraphicsContext2D();
            for (int i = 0; i < nodes.length; i++) {
                for (int j = 0; j < nodes[i].length; j++) {
                    if (nodes[i][j].getNodeState() == NodeStates.PLAYER) {
                        gc.setFill(Color.RED);
                        gc.fillRect(j * size, i * size, fxSlider.getValue() - 2, fxSlider.getValue() - 2);
                    } else if (nodes[i][j].getNodeState() == NodeStates.TARGET) {
                        gc.setFill(Color.GREEN);
                        gc.fillRect(j * size, i * size, fxSlider.getValue() - 2, fxSlider.getValue() - 2);
                    } else if (nodes[i][j].getNodeState() == NodeStates.BARRIER) {
                        gc.setFill(Color.DARKBLUE);
                        gc.fillRect(j * size, i * size, fxSlider.getValue() - 2, fxSlider.getValue() - 2);
                    } else if (nodes[i][j].getNodeState() == NodeStates.LIGHTBARRIER) {
                        gc.setFill(Color.LIGHTBLUE);
                        gc.fillRect(j * size, i * size, fxSlider.getValue() - 2, fxSlider.getValue() - 2);
                    }
                }
            }
        }
    }

    public void drawInNodes(MouseEvent mouseEvent) {
        int x = (int) ((int) (mouseEvent.getX() / fxSlider.getValue()) * fxSlider.getValue());
        int y = (int) ((int) (mouseEvent.getY() / fxSlider.getValue()) * fxSlider.getValue());
        GraphicsContext gc = canvas.getGraphicsContext2D();
        if (nodes != null) {
            if (radioButonBarrier.isSelected()) {
                gc.setFill(Color.DARKBLUE);
                gc.fillRect(x, y, fxSlider.getValue() - 2, fxSlider.getValue() - 2);
                nodes[x / (int) fxSlider.getValue()][y / (int) fxSlider.getValue()].setNodeState(NodeStates.BARRIER);
                System.out.println(nodes[x / (int) fxSlider.getValue()][y / (int) fxSlider.getValue()].getNodeState());
            } else if (lightBarrierRadioButton.isSelected()) {
                gc.setFill(Color.LIGHTBLUE);
                gc.fillRect(x, y, fxSlider.getValue() - 2, fxSlider.getValue() - 2);
                nodes[x / (int) fxSlider.getValue()][y / (int) fxSlider.getValue()].setNodeState(NodeStates.LIGHTBARRIER);
                System.out.println(nodes[x / (int) fxSlider.getValue()][y / (int) fxSlider.getValue()].getNodeState());
            } else if (startRadioButton.isSelected()) {
                if (!setStart) {
                    gc.setFill(Color.GREEN);
                    gc.fillRect(x, y, fxSlider.getValue() - 2, fxSlider.getValue() - 2);
                    nodes[x / (int) fxSlider.getValue()][y / (int) fxSlider.getValue()].setNodeState(NodeStates.PLAYER);
                    setStart = true;
                    radioButonBarrier.setSelected(true);
                    System.out.println(nodes[x / (int) fxSlider.getValue()][y / (int) fxSlider.getValue()].getNodeState());
                }
            } else if (zielRadioButton.isSelected()) {
                if (!setZiel) {
                    gc.setFill(Color.RED);
                    gc.fillRect(x, y, fxSlider.getValue() - 2, fxSlider.getValue() - 2);
                    nodes[x / (int) fxSlider.getValue()][y / (int) fxSlider.getValue()].setNodeState(NodeStates.TARGET);
                    setZiel = true;
                    radioButonBarrier.setSelected(true);
                    System.out.println(nodes[x / (int) fxSlider.getValue()][y / (int) fxSlider.getValue()].getNodeState());
                }
            } else if (loeschenRadioButton.isSelected()) {
                resetState(x / (int) fxSlider.getValue(), y / (int) fxSlider.getValue());
                gc.setFill(Color.WHITE);
                gc.fillRect(x, y, fxSlider.getValue() - 2, fxSlider.getValue() - 2);
                System.out.println(nodes[x / (int) fxSlider.getValue()][y / (int) fxSlider.getValue()].getNodeState());
            }
        }
    }

    //get Player Node
    public Node getPlayerNode() {
        for (int i = 0; i < nodes.length; i++) {
            for (int j = 0; j < nodes[i].length; j++) {
                if (nodes[i][j].getNodeState() == NodeStates.PLAYER) {
                    return nodes[i][j];
                }
            }
        }
        return null;
    }

    //get Target Node
    public Node getTargetNode() {
        for (int i = 0; i < nodes.length; i++) {
            for (int j = 0; j < nodes[i].length; j++) {
                if (nodes[i][j].getNodeState() == NodeStates.TARGET) {
                    return nodes[i][j];
                }
            }
        }
        return null;
    }









    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        checkBox.setSelected(true);
        drawGrid((int) fxSlider.getValue());
        createNodes((int) fxSlider.getValue());
        radioButonBarrier.setSelected(true);


        fxSlider.valueProperty().addListener((observableValue, number, t1) -> {
            drawGrid((int) fxSlider.getValue());


        });
        canvas.setOnMouseDragged(this::drawInNodes);
        canvas.setOnMouseClicked(this::drawInNodes);
        checkBox.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
            if (checkBox.isSelected()) {
                drawGrid((int) fxSlider.getValue());
                drawNodes((int) fxSlider.getValue());
            } else {
                clearCanvas();
                drawNodes((int) fxSlider.getValue());
            }
        });
        clearButton.setOnAction(actionEvent -> {
            clearCanvas();
            drawGrid((int) fxSlider.getValue());
        });

        AStarButton.setOnAction(actionEvent -> {
            if (setStart && setZiel) {
                AStar aStar = new AStar();
                aStar.findPath(getPlayerNode(), getTargetNode(), nodes);
                for(Node node : aStar.findPath(getPlayerNode(), getTargetNode(), nodes)){
                    GraphicsContext gc = canvas.getGraphicsContext2D();
                    gc.setFill(Color.BLACK);
                    gc.fillRect(node.getCollum() * (int) fxSlider.getValue(), node.getRow() * (int) fxSlider.getValue(), fxSlider.getValue() - 2, fxSlider.getValue() - 2);
                }
            }
        });

        DjakstraButton.setOnAction(actionEvent -> {
            if (setStart && setZiel) {
                Djakstra djakstra = new Djakstra();
                djakstra.findPath(getPlayerNode(), getTargetNode(), nodes);
                for(Node node : djakstra.findPath(getPlayerNode(), getTargetNode(), nodes)){
                    GraphicsContext gc = canvas.getGraphicsContext2D();
                    gc.setFill(Color.BLACK);
                    gc.fillRect(node.getCollum() * (int) fxSlider.getValue(), node.getRow() * (int) fxSlider.getValue(), fxSlider.getValue() - 2, fxSlider.getValue() - 2);
                }
            }
        });


    }
}