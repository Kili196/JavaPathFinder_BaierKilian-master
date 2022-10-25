package Bps1;

import java.util.ArrayList;



public class AStar {
    public static ArrayList<Node> findPath(Node start, Node target, Node[][] nodes) {
        ArrayList<Node> openList = new ArrayList<>();
        ArrayList<Node> closedList = new ArrayList<>();
        ArrayList<Node> path = new ArrayList<>();
        openList.add(start);
        while (!openList.isEmpty()) {
            Node leastF = openList.get(0);
            for(Node node : openList){
                if (node.getF() < leastF.getF() || node.getF() == leastF.getF()) {
                    leastF = node;
                }
            }
            if (leastF == target) {
                Node temp = leastF;
                closedList.add(temp);
                break;
            }
            ArrayList<Node> neighbours = new ArrayList<>();
            if (leastF.getCollum() < nodes.length) {
                neighbours.add(nodes[leastF.getCollum() + 1][leastF.getRow()]);
            }
            if (leastF.getCollum() > 0) {
                neighbours.add(nodes[leastF.getCollum() - 1][leastF.getRow()]);
            }
            if (leastF.getRow() < nodes[0].length - 1) {
                neighbours.add(nodes[leastF.getCollum()][leastF.getRow() + 1]);
            }
            if (leastF.getRow() > 0) {
                neighbours.add(nodes[leastF.getCollum()][leastF.getRow() - 1]);
            }
            for (Node node_successor : neighbours) {
                node_successor.setParent(leastF);
                if (node_successor.getNodeState() == NodeStates.TARGET) {
                    Node temp = node_successor;
                    path.add(temp);
                    break;
                }
                else{
                    int node_successor_currentcost = leastF.getG() + getDistance(leastF, node_successor);
                    if ( openList.contains(node_successor)) {
                        if (node_successor.getG() <= node_successor_currentcost) {
                            continue;
                        }
                            else if (closedList.contains(node_successor)) {
                                if (node_successor.getG() <= node_successor_currentcost) {
                                    continue;
                                }
                                closedList.remove(node_successor);
                            }
                            else{
                                openList.add(node_successor);
                                node_successor.setH(getDistance(node_successor, target));
                                node_successor.setG(node_successor_currentcost);
                                node_successor.setParent(leastF);
                            }
                            closedList.add(leastF);
                        }
                    }
                }
            }
        return closedList;
        }




    public static int getDistance(Node nodeA, Node nodeB) {
        int dstX = Math.abs(nodeA.getCollum() - nodeB.getCollum());
        int dstY = Math.abs(nodeA.getRow() - nodeB.getRow());
        if (dstX > dstY) {
            return 14 * dstY + 10 * (dstX - dstY);
        }
        return 14 * dstX + 10 * (dstY - dstX);
    }
}