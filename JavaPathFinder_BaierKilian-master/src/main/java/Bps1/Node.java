package Bps1;

import java.util.ArrayList;
import java.util.Objects;

public class Node {



    private int id;
    private int x;
    private int y;
    private int costs;
    ArrayList nodeList = new ArrayList();

    private int row;
    private int collum;

    NodeStates nodeStates;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getRow() {
        return row;
    }

    public int getCollum() {
        return collum;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCollum(int collum) {
        this.collum = collum;
    }

    public int getCosts() {
        return costs;
    }

    public void setCosts(int costs) {
        this.costs = costs;
    }

    public ArrayList getNodeList() {
        return nodeList;
    }

    public void setNodeList(ArrayList nodeList) {
        this.nodeList = nodeList;
    }


    public Node(int x, int y, NodeStates nodeStates, int slidervalue) {
        this.x = x;
        this.y = y;
        this.nodeStates = nodeStates;
        this.row = (y / slidervalue);
        this.collum = (x / slidervalue);
    }

    public NodeStates getNodeStates() {
        return nodeStates;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node node)) return false;
        return id == node.id && x == node.x && y == node.y && costs == node.costs && Objects.equals(nodeList, node.nodeList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, x, y, costs, nodeList);
    }

    @Override
    public String toString() {

        return "Row " + row + " " + "Collumn " + collum + "  State " + nodeStates;
    }
}
