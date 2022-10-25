package Bps1;

import java.util.ArrayList;

public class Djakstra {
    //Djakstra algorithm for finding the shortest path from player to target
    public static ArrayList<Node> findPath(Node start, Node target, Node[][] nodes) {
        ArrayList<Node> openList = new ArrayList<>();
        ArrayList<Node> closedList = new ArrayList<>();
        openList.add(start);
        while (!openList.isEmpty()) {
            Node leastF = openList.get(0);
            for (Node node : openList) {
                if (node.getF() < leastF.getF() || node.getF() == leastF.getF()) {
                    leastF = node;
                }
            }
            if (leastF == target) {
                Node temp = leastF;
                closedList.add(temp);
                break;
            }
        }
        return closedList;
    }
}
