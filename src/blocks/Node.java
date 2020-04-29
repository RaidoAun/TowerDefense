package blocks;

import javafx.scene.paint.Color;

import java.util.HashSet;

public class Node extends Block {

    private double cost;
    private Node parent;
    private boolean visited;
    private boolean inUnvisitedList;
    private HashSet<Node> naabrid;

    public Node(int x, int y, int id) {
        super(x, y, id, id, null, 0);
        this.visited = false;
        this.inUnvisitedList = false;
        this.cost = Double.POSITIVE_INFINITY;
        this.parent = null;
    }

    public Node(Node node) {
        super(node.indexX, node.indexY, node.id, node.value, node.color, 0);
        this.visited = node.isVisited();
        this.inUnvisitedList = node.isInUnvisitedList();
        this.cost = node.getCost();
        this.parent = node.getParent();
    }
    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public boolean isWall() {
        return id != 0 && id != 9;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public int getX() {
        return indexX;
    }

    public int getY() {
        return indexY;
    }

    public boolean isInUnvisitedList() {
        return inUnvisitedList;
    }

    public void setInUnvisitedList(boolean inUnvisitedList) {
        this.inUnvisitedList = inUnvisitedList;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public HashSet<Node> get4Naabrit() {
        HashSet<Node> naabrid4 = new HashSet<>();
        for (Node naaber : naabrid) {
            if (Math.abs(naaber.indexX - indexX) + Math.abs(naaber.indexY - indexY) != 2) {
                naabrid4.add(naaber);
            }
        }
        return naabrid4;
    }

    public HashSet<Node> getKõikNaabrid() {
        return naabrid;
    }

    public void setNaabrid(HashSet<Node> naabrid) {
        this.naabrid = naabrid;
    }

    public void setNexus() {
        this.parent = this;
        this.id = 3;
        this.color = Color.PURPLE;
        this.cost = 0;
    }

    public void copyBlock(Block block) {
        this.id = block.id;
        this.value = block.value;
        this.color = block.color;
        setPathCount(block.getPathCount());
    }

}
