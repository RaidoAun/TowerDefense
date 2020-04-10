package blocks;

import java.util.HashSet;

public class Node extends Block {

    private double cost;
    private Node parent;
    private boolean visited;
    private boolean inUnvisitedList;
    private HashSet<Node> naabrid;

    public Node(int x, int y, int id, boolean start) {
        super(x, y, id, id, null, 0);
        this.visited = false;
        this.inUnvisitedList = false;
        if (start) {
            this.cost = 0;
            parent = this;
        } else {
            this.cost = Double.POSITIVE_INFINITY;
            parent = null;
        }
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
}
