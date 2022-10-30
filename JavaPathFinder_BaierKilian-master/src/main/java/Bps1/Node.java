package Bps1;

import java.util.ArrayList;
import java.util.Calendar;

public class Node {


    private int row;
    private int collum;
    NodeStates nodeState;
    private  int F;
    private int H;
    private int G;

    Node parent;


    public int getF() {

        return F;
    }

    public void setF() {
        F = getH() + getG();
    }

    public int getH() {
        return H;
    }

    public void setH(int h) {
        H = h;
    }

    public int getG() {
        return G;
    }




    public void setG(int g) {
        G = g;
    }

    public Node getParent() {
        return parent;
    }

    public Node(int row, int collum) {
        this.row = row;
        this.collum = collum;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Node(int x, int y, NodeStates nodeState) {
        this.collum = x;
        this.row = y;
        this.nodeState = nodeState;
    }

    public NodeStates getNodeState() {
        return nodeState;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCollum(int collum) {
        this.collum = collum;
    }

    public void setNodeState(NodeStates nodeState) {
        this.nodeState = nodeState;
    }

    public int getRow() {
        return row;
    }

    public int getCollum() {
        return collum;
    }


    @Override
    public String toString() {
        return "Node{" +
                "row=" + row +
                ", collum=" + collum +
                ", nodeState=" + nodeState +
                ", F=" + F +
                ", H=" + H +
                ", G=" + G +
                '}';
    }
}
