package blocks;

import javafx.scene.paint.Color;

public class Node extends Block {

    private double cost;
    private Node parent;
    private boolean visited;
    private boolean inUnvisitedList;

    public Node(int indexX, int indexY, int id, int value, Color color, int pathCount) {
        super(indexX, indexY, id, value, color, pathCount);
    }
}
