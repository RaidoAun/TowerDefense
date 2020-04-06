package map;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import towerdefense.Main;

import java.util.ArrayList;
import java.util.List;

enum Küljed {

    PAREM(1, 0),
    VASAK(-1, 0),
    ÜLEMINE(0, 1),
    ALUMINE(0, -1);

    private final int xMuut;
    private final int yMuut;

    Küljed(int xMuut, int yMuut) {
        this.xMuut = xMuut;
        this.yMuut = yMuut;
    }

    public int getNewX(int currentX) {
        return currentX + xMuut;
    }

    public int getNewY(int currentY) {
        return currentY + yMuut;
    }

}

public class NewPathfinder {

    List<Node> visited;
    private Node[][] map;
    private int startX;
    private int startY;

    public NewPathfinder(int startX, int startY) {
        this.startX = startX;
        this.startY = startY;
        this.visited = new ArrayList<>();
    }

    public void generateMatrix(Map mapClass) {
        map = new Node[mapClass.getY()][mapClass.getX()];
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length; x++) {
                if (x == startX && y == startY) {
                    map[y][x] = new Node(x, y, mapClass.getBlock(x, y).getId(), true);
                } else {
                    map[y][x] = new Node(x, y, mapClass.getBlock(x, y).getId(), false);
                }
            }
        }
    }

    public int[][] getPath(int endX, int endY) {
        Node current = map[endY][endX];
        Node startNode = map[startY][startX];
        List<int[]> path = new ArrayList<>();
        while (current != startNode) {
            path.add(new int[]{current.getX(), current.getY()});
            current = current.getParent();
        }
        path.add(new int[]{startX, startY});
        return path.toArray(new int[path.size()][2]);
    }

    public void scanMap() {
        List<Node> unvisited = new ArrayList<>();
        unvisited.add(map[startY][startX]);
        map[startY][startX].setInUnvisitedList(true);
        while (unvisited.size() > 0) {
            Node current = cheapestNode(unvisited);
            for (Node neighbour : getSuitableNeighbours(current.getX(), current.getY())) {
                double costAfterMoving = current.getCost() + 1;
                if (neighbour.getCost() > costAfterMoving) {
                    neighbour.setCost(costAfterMoving);
                    neighbour.setParent(current);
                    if (!neighbour.isInUnvisitedList()) {
                        unvisited.add(neighbour);
                        neighbour.setInUnvisitedList(true);
                    }
                }
            }
            unvisited.remove(current);
            current.setInUnvisitedList(false);
            visited.add(current);
            current.setVisited(true);
        }
    }

    private List<Node> getSuitableNeighbours(int currentX, int currentY) {
        List<Node> neighbours = new ArrayList<>();
        for (Küljed suund : Küljed.values()) {
            int newX = suund.getNewX(currentX);
            int newY = suund.getNewY(currentY);
            boolean isInBorders = newX >= 0 && newX <= map[0].length && newY >= 0 && newY <= map.length;
            if (isInBorders) {
                Node neighbour = map[newY][newX];
                if (!neighbour.isVisited() && !neighbour.isWall()) {
                    neighbours.add(neighbour);
                }
            }
        }
        return neighbours;
    }

    public void drawCost() {
        //Ühe bloki külje suurus pikslites
        int blockSize = Main.blockSize;
        Canvas canvas = new Canvas();
        GraphicsContext g = canvas.getGraphicsContext2D();
        for (Node node : visited) {
            String cost = Double.toString(node.getCost());
            int centreX = indexCentrePixel(node.getX(), blockSize);
            int centreY = indexCentrePixel(node.getY(), blockSize);
            g.setFont(Font.font("Calibri", FontWeight.BOLD, 10));
            g.setFill(Paint.valueOf("#20fc03"));
            g.fillText(cost, centreX, centreY);
        }
    }

    private int indexCentrePixel(int index, int blockSize) {
        return index * blockSize + blockSize / 2;
    }

    private Node cheapestNode(List<Node> unvisited) {
        Node cheapest = unvisited.get(0);
        for (Node node : unvisited) {
            if (cheapest.getCost() > node.getCost()) cheapest = node;
        }
        return cheapest;
    }

    public List<Node> getVisited() {
        return visited;
    }
}

class Node {

    private int x;
    private int y;
    private int id;
    private double cost;
    private Node parent;
    private boolean visited;
    private boolean inUnvisitedList;

    Node(int x, int y, int id, boolean start) {
        this.x = x;
        this.y = y;
        this.id = id;
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
        return x;
    }

    public int getY() {
        return y;
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
}
