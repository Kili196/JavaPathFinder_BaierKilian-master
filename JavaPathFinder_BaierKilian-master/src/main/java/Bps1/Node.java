package Bps1;

import java.util.ArrayList;
import java.util.Objects;

public class Node {



    private int id;
    private int x;
    private int y;
    private int costs;

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    private Node parent;

    private int fCosts; //sum of h and g
    private double hCosts; //distance from endnode
    private double gCosts; //distance from starting node
    ArrayList nodeList = new ArrayList();
    private int row;
    private int collum;

    private boolean horizontalOrvertical = false;
    private boolean diagonal = false;
    NodeStates nodeStates;

    public boolean isHorizontalOrvertical() {
        return horizontalOrvertical;
    }

    public void setHorizontalOrvertical(boolean horizontalOrvertical) {
        this.horizontalOrvertical = horizontalOrvertical;
    }

    public boolean isDiagonal() {
        return diagonal;
    }

    public void setDiagonal(boolean diagonal) {
        this.diagonal = diagonal;
    }

    public int getId() {
        return id;
    }

    public void setNodeStates(NodeStates nodeStates) {
        this.nodeStates = nodeStates;
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

    public int getColumn() {
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

    public ArrayList<Node> getNodeList() {
        return nodeList;
    }

    public void setNodeList(ArrayList nodeList) {
        this.nodeList = nodeList;
    }


    public int getfCosts() {
        return fCosts;
    }

    public void setfCosts(int fCosts) {
        this.fCosts = fCosts;
    }

    public double gethCosts() {
        return hCosts;
    }

    public void sethCosts(double hCosts) {
        this.hCosts = hCosts;
    }

    public double getgCosts() {
        return gCosts;
    }

    public void setgCosts(double gCosts) {
        this.gCosts = gCosts;
    }

    public Node(int x, int y, NodeStates nodeStates, int slidervalue, int id) {
        this.x = x;
        this.y = y;
        this.nodeStates = nodeStates;
        this.row = (y / slidervalue);
        this.collum = (x / slidervalue);
        this.id = id;
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
        return x == node.x && y == node.y && costs == node.costs;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, costs);
    }

    @Override
    public String toString() {

        return getRow() + " Row " + getColumn() + " Collumn " + " h " + gethCosts() + " g " + getgCosts() + "}";
    }
}
