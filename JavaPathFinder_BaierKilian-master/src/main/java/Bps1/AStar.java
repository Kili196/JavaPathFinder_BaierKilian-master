package Bps1;

import java.util.ArrayList;


public class AStar {
    public static ArrayList<Node> findPath(Node start, Node target, Node[][] nodes) {
        ArrayList<Node> openList = new ArrayList<>();
        ArrayList<Node> path = new ArrayList<>();
        ArrayList<Node> visited = new ArrayList<>();
        Node tmp_Node = null;
        start.setG(0);
        start.setH(getDistance(start, target));
        start.setF();
        openList.add(start);
        while (!openList.isEmpty()) {
            Node current = openList.get(0);
            if (current == target) {
                break;
            }
            ArrayList<Node> neighbours = new ArrayList<>();
            if (current != null) {
                if (current.getCollum() < nodes.length) {
                    neighbours.add(nodes[current.getCollum() + 1][current.getRow()]);
                }
                if (current.getCollum() > 0) {
                    neighbours.add(nodes[current.getCollum() - 1][current.getRow()]);
                }
                if (current.getRow() < nodes[0].length - 1) {
                    neighbours.add(nodes[current.getCollum()][current.getRow() + 1]);
                }
                if (current.getRow() > 0) {
                    neighbours.add(nodes[current.getCollum()][current.getRow() - 1]);
                }
            }
            if (current.getRow() >= 0 && current.getCollum() < nodes.length) {
                neighbours.add(nodes[current.getCollum() + 1][current.getRow() + 1]);
            }
            if (current.getRow() < nodes[0].length - 1 && current.getRow() < 0 && current.getCollum() > 0) {
                neighbours.add(nodes[current.getCollum() - 1][current.getRow() - 1]);
            }
            if (current.getRow() < nodes[0].length - 1 && current.getCollum() < nodes.length && current.getRow() > 0) {
                neighbours.add(nodes[current.getCollum() + 1][current.getRow() - 1]);
            }
            if (current.getRow() > 0 && current.getCollum() > 0) {
                neighbours.add(nodes[current.getCollum() - 1][current.getRow() + 1]);
            }


            tmp_Node = null;
            for (Node node_neighbor : neighbours) {
                if (node_neighbor != null && node_neighbor.getNodeState() != NodeStates.BARRIER) {
                    node_neighbor.setG(getDistance(start, node_neighbor));
                    node_neighbor.setH(getDistance(node_neighbor, target));
                    node_neighbor.setF();
                    if (tmp_Node == null) {
                        tmp_Node = node_neighbor;
                    }
                    System.out.println(node_neighbor.getF());
                    System.out.println(tmp_Node.getF());
                    ;
                    if (node_neighbor.getF() <= tmp_Node.getF() && node_neighbor.getNodeState() != NodeStates.PLAYER && !visited.contains(node_neighbor)) {
                        tmp_Node = node_neighbor;
                        visited.add(tmp_Node);

                    }
                }
            }
            openList.remove(current);
            openList.add(tmp_Node);
            path.add(tmp_Node);
        }
        return path;
    }

    //getNeighbors diagonal





    public static int getDistance(Node nodeA, Node nodeB) {
        if (nodeA != null && nodeB != null) {
            int dstX = Math.abs(nodeA.getCollum() - nodeB.getCollum());
            int dstY = Math.abs(nodeA.getRow() - nodeB.getRow());
            if (dstX > dstY) {
                return 14 * dstY + 10 * (dstX - dstY);
            }
            return 14 * dstX + 10 * (dstY - dstX);
        }
        return -1;
    }
}