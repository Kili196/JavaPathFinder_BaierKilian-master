package Bps1;

import java.util.Comparator;

public class SortByRow implements Comparator<Node> {
    @Override
    public int compare(Node o1, Node o2) {
        return o1.getCollum() - o2.getCollum();
    }
}
