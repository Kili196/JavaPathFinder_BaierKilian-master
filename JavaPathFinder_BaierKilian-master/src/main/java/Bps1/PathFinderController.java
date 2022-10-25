package Bps1;


import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

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
    private Button astar;

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
    int id = 0;

    int lastMaxRow = 0;
    int lastMaxColumn = 0;

    public void clearCanvas() {
        setZiel = false;
        setStart = false;
        allNodes.clear();
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getWidth());
    }

    public void drawGrid(int size) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setLineWidth(2);
        gc.setFill(Color.WHITE);
        double width = (Math.floor(canvas.getWidth() / size)) * size;
        double height = (Math.floor(canvas.getHeight() / size)) * size;

        gc.fillRect(0, 0, width, height);
        for (int i = 0; i <= width; i += size) {
            for (int j = 0; j <= height; j += size) {
                gc.strokeRect(0, 0, i, j);
                Node n;
                if ((n = findNode(i, j)) != null) {
                    drawInGrid(n);
                } else {
                    allNodes.add(new Node(i, j, NodeStates.EMPTY, (int) fxSlider.getValue(), id));
                }

                //
            }
        }
        int maxRow = (int) (canvas.getWidth() / 24 - 1);
        int maxColumn = (int) (canvas.getHeight() / 24 - 1);
        System.out.println(maxColumn + ":" + maxRow);
        if (!(lastMaxColumn == maxColumn && lastMaxRow == maxRow)) {
            lastMaxColumn = maxColumn;
            lastMaxRow = maxRow;
            //delteWrongRowsCollumns(maxRow, maxColumn);
        }
        getNeighbors();
        stillDraw();
    }


    public Node getRowsAndColls(int x, int y) {
        for (Node node : allNodes) {
            if (x == node.getColumn() && y == node.getRow()) {
                return node;
            }
        }
        return null;
    }

    public void getNeighbors() {
        for (Node node : allNodes) {
            int count = 1;
            Node tmpnode;
            ArrayList<Node> neighbors = new ArrayList<>();
            tmpnode = (getRowsAndColls(node.getColumn(), node.getRow() + 1));
            neighbors.add(tmpnode);
            tmpnode = (getRowsAndColls(node.getColumn(), node.getRow() - 1));
            neighbors.add(tmpnode);
            tmpnode = (getRowsAndColls(node.getColumn() + 1, node.getRow()));
            neighbors.add(tmpnode);
            tmpnode = (getRowsAndColls(node.getColumn() - 1, node.getRow()));
            neighbors.add(tmpnode);
            neighbors.add(getRowsAndColls(node.getColumn() - 1, node.getRow() - 1));
            neighbors.add(getRowsAndColls(node.getColumn() + 1, node.getRow() - 1));
            neighbors.add(getRowsAndColls(node.getColumn() + 1, node.getRow() + 1));
            neighbors.add(getRowsAndColls(node.getColumn() - 1, node.getRow() - 1));
            for (Node node1 : neighbors) {
                if (node1 != null) {
                    if (count <= 4) {
                        node1.setHorizontalOrvertical(true);
                    } else {
                        node1.setDiagonal(true);
                    }
                }
                count++;
            }
            node.setNodeList(neighbors);
        }
    }

    public void setClearButton() {
        clearButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                clearCanvas();
                //allNodes.clear();
                //allNodes.clear();
                drawGrid((int) fxSlider.getValue());
            }
        });
    }


    public void stillDraw() {
        for (Node node : allNodes) {
            if (node.getNodeStates() != NodeStates.EMPTY) {
                drawInGrid(node);
            }
        }
    }

    public void drawInGrid(Node node) {
        Color color = Color.WHITE;
        int x = (int) (node.getColumn() * fxSlider.getValue());
        int y = (int) (node.getRow() * fxSlider.getValue());
        GraphicsContext gc = canvas.getGraphicsContext2D();
        if (node.getNodeStates() == NodeStates.BARRIER) {
            color = Color.GREY;
        } else if (node.getNodeStates() == NodeStates.LIGHTBARRIER) {
            color = Color.DARKGRAY;
        } else if (node.getNodeStates() == NodeStates.PATH) {
            color = Color.BLUE;
        } else if (node.getNodeStates() == NodeStates.TARGET) {
            color = Color.RED;
        } else if (node.getNodeStates() == NodeStates.PLAYER) {
            color = Color.GREEN;
        }
        gc.setFill(color);
        gc.fillRect(x + 1, y + 1, fxSlider.getValue() - 3, fxSlider.getValue() - 3);
    }

    public Node findNode(int x, int y) {
        for (Node node : allNodes) {
            if (node.getX() == x && node.getY() == y) {
                return node;
            }
        }
        return null;
    }

    public void drawInGrid(Color color, javafx.scene.input.MouseEvent mouseEvent, NodeStates nodeStates) {
        int x = (int) ((int) (mouseEvent.getX() / fxSlider.getValue()) * fxSlider.getValue());
        int y = (int) ((int) (mouseEvent.getY() / fxSlider.getValue()) * fxSlider.getValue());
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(color);
        gc.fillRect(x + 1, y + 1, fxSlider.getValue() - 2, fxSlider.getValue() - 2);
        Node node = findNode(x, y);
        if (allNodes.contains(node)) {
            int idx = allNodes.indexOf(node);
            if (allNodes.get(idx).getNodeStates() == NodeStates.PLAYER) {
                setStart = false;
            }
            if (allNodes.get(idx).getNodeStates() == NodeStates.TARGET) {
                setZiel = false;
            }
            allNodes.set(idx, node);
        }
        if (node != null) node.setNodeStates(nodeStates);
        allNodes.sort(new SortByRow());
    }

    public void delteWrongRowsCollumns(int maxRow, int maxColumn) {
        ArrayList<Node> toDel = new ArrayList<>();
        for (Node node : allNodes) {
            if (node.getRow() > maxRow) {
                toDel.add(node);
            } else if (node.getColumn() > maxColumn) {
                toDel.add(node);
            }
        }
        allNodes.removeAll(toDel);

    }

    public void printNeighborList(Node node) {
        if (allNodes.contains(node)) {
            System.out.println(node.getNodeList());
        }
    }

    public Node getStart() {
        for (Node node : allNodes) {
            if (node.getNodeStates() == NodeStates.PLAYER) {
                return node;
            }
        }
        return null;
    }


    public Node getZiel() {
        for (Node node : allNodes) {
            if (node.getNodeStates() == NodeStates.TARGET) {
                return node;
            }
        }
        return null;
    }


    // find and draw path from player to target using astar algorithm
    public void aStarAlgorithm() {
        Node start = getStart();
        Node ziel = getZiel();
        if (start != null && ziel != null) {
            ArrayList<Node> openList = new ArrayList<>();
            ArrayList<Node> closedList = new ArrayList<>();
            openList.add(start);
            while (openList.size() > 0) {
                Node currentNode = openList.get(0);
                for (int i = 0; i < openList.size(); i++) {
                    if (openList.get(i).getfCosts() < currentNode.getfCosts() || openList.get(i).getfCosts() == currentNode.getfCosts() && openList.get(i).gethCosts() < currentNode.gethCosts()) {
                        currentNode = openList.get(i);
                    }
                }
                openList.remove(currentNode);
                closedList.add(currentNode);
                if (currentNode == ziel) {
                    Node tmp = currentNode;
                    while (tmp != null) {
                        tmp.setNodeStates(NodeStates.PATH);
                        tmp = tmp.getParent();
                    }
                    break;
                }
                for (Node neighbor : currentNode.getNodeList()) {
                    if (neighbor == null || neighbor.getNodeStates() == NodeStates.BARRIER || neighbor.getNodeStates() == NodeStates.LIGHTBARRIER || closedList.contains(neighbor)) {
                        continue;
                    }
                    double newMovementCostToNeighbor = currentNode.getgCosts() + getDistance(currentNode, neighbor);
                    if (newMovementCostToNeighbor < neighbor.getgCosts() || !openList.contains(neighbor)) {
                        neighbor.setgCosts(newMovementCostToNeighbor);
                        neighbor.sethCosts(getDistance(neighbor, ziel));
                        neighbor.setParent(currentNode);
                        if (!openList.contains(neighbor)) {
                            openList.add(neighbor);
                        }
                    }
                }

            }
        }
        System.out.println("ziel" + ziel);
        stillDraw();
    }

    public double getDistance(Node self, Node other) {
        return Math.hypot(self.getX() - other.getX(), self.getY() - other.getY());
    }


    public void drawInNode() {
        canvas.setOnMouseDragged(mouseEvent -> {

            if (radioButonBarrier.isSelected()) {
                drawInGrid(Color.GREY, mouseEvent, NodeStates.BARRIER);
            } else if (lightBarrierRadioButton.isSelected()) {
                drawInGrid(Color.DARKGRAY, mouseEvent, NodeStates.LIGHTBARRIER);
            } else if (loeschenRadioButton.isSelected()) {
                drawInGrid(Color.WHITE, mouseEvent, NodeStates.EMPTY);
            }
            aStarAlgorithm();
        });

        canvas.setOnMouseClicked(mouseEvent -> {
            if (radioButonBarrier.isSelected()) {
                drawInGrid(Color.GREY, mouseEvent, NodeStates.BARRIER);
            } else if (lightBarrierRadioButton.isSelected()) {
                drawInGrid(Color.DARKGRAY, mouseEvent, NodeStates.LIGHTBARRIER);
            } else if (loeschenRadioButton.isSelected()) {
                drawInGrid(Color.WHITE, mouseEvent, NodeStates.EMPTY);
            }
            if (!setStart) {
                if (startRadioButton.isSelected()) {
                    drawInGrid(Color.GREEN, mouseEvent, NodeStates.PLAYER);
                    setStart = true;
                }
            }
            if (!setZiel) {
                if (zielRadioButton.isSelected()) {
                    drawInGrid(Color.RED, mouseEvent, NodeStates.TARGET);
                    setZiel = true;
                }
            }
            aStarAlgorithm();

        });

        stillDraw();
    }

    public void setCheckBoxSettings() {
        checkBox.setSelected(true);
        if (checkBox.isSelected()) {
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

        astar.setOnAction(actionEvent -> {
            aStarAlgorithm();
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
            } else {
                clearCanvas();
                stillDraw();
            }
        });
    }
}