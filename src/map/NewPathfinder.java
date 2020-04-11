package map;

import blocks.Node;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import tools.Converter;
import towerdefense.Main;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class NewPathfinder {

    HashSet<Node> visited;
    HashSet<Node> ends;
    private Node[][] map;
    private int startX;
    private int startY;

    public NewPathfinder(int startX, int startY) {
        this.startX = startX;
        this.startY = startY;
        this.visited = new HashSet<>();
        this.ends = new HashSet<>();
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
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length; x++) {
                map[y][x].setNaabrid(getNeighbours(x, y));
            }
        }
    }

    public void resetMap() {
        visited = new HashSet<>();
        for (Node[] rida : map) {
            for (Node node : rida) {
                node.setInUnvisitedList(false);
                node.setVisited(false);
                if (!node.isStart()) {
                    node.setParent(null);
                    node.setCost(Double.POSITIVE_INFINITY);
                }
            }
        }
    }

    public void addWall(int indexX, int indexY) {
        map[indexY][indexX].setWall();
    }

    public int[][] getPath(int endX, int endY) {
        Node current = map[endY][endX];
        if (current.getParent() == null) {
            return new int[0][0];
        } else {
            Node startNode = map[startY][startX];
            List<int[]> path = new ArrayList<>();
            while (current != startNode) {
                path.add(new int[]{current.getX(), current.getY()});
                current = current.getParent();
            }
            path.add(new int[]{startX, startY});
            return path.toArray(new int[path.size()][2]);
        }
    }

    public void addEnd(int indexX, int indeXY) {
        this.ends.add(map[indeXY][indexX]);
    }

    public HashSet<Node> getPathNodes() {
        HashSet<Node> path = new HashSet<>();
        HashSet<Node> endsToRemove = new HashSet<>();
        for (Node end : ends) {
            if (end.getParent() == null) {
                endsToRemove.add(end);
            } else {
                Node startNode = map[startY][startX];
                Node current = end;
                while (current != startNode) {
                    path.add(current);
                    current = current.getParent();
                }
            }
        }
        ends.removeAll(endsToRemove);
        return path;
    }

    public void scanMap() {
        List<Node> unvisited = new ArrayList<>();
        unvisited.add(map[startY][startX]);
        map[startY][startX].setInUnvisitedList(true);
        while (unvisited.size() > 0) {
            if (ends.size() > 0 && visited.containsAll(ends)) {
                break;
            }
            Node current = cheapestNode(unvisited);
            for (Node neighbour : current.get4Naabrit()) {
                if (!neighbour.isWall() && !neighbour.isVisited()) {
                    double costAfterMoving = current.getCost() + 1;
                    if (neighbour.getCost() > costAfterMoving) {
                        neighbour.setCost(costAfterMoving);
                        neighbour.setParent(current);
                        if (!neighbour.isInUnvisitedList()) {
                            unvisited.add(neighbour);
                            neighbour.setInUnvisitedList(true);
                        }
                    } /*else if (neighbour.getCost() == costAfterMoving) {
                        Random r = new Random();
                        int flip = r.nextInt(2);
                        if (flip == 0) {
                            neighbour.setCost(costAfterMoving);
                            neighbour.setParent(current);
                        }
                    }*/
                }
            }
            unvisited.remove(current);
            current.setInUnvisitedList(false);
            visited.add(current);
            current.setVisited(true);
        }
    }

    private HashSet<Node> getNeighbours(int currentX, int currentY) {
        HashSet<Node> neighbours = new HashSet<>();

        for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
                int newX = currentX + x;
                int newY = currentY + y;
                boolean isInBorders = newX >= 0 && newX < map[0].length && newY >= 0 && newY < map.length;
                if (isInBorders) {
                    Node neighbour = map[newY][newX];
                    neighbours.add(neighbour);
                }
            }
        }

        return neighbours;
    }

    public void drawCost(GraphicsContext g) {
        for (Node node : visited) {
            String cost = Integer.toString((int) node.getCost());
            double centreX = Converter.indexToPix(node.getX());
            double centreY = Converter.indexToPix(node.getY());
            g.setTextAlign(TextAlignment.CENTER);
            g.setTextBaseline(VPos.CENTER);
            g.setFont(Font.font("Calibri", FontWeight.BOLD, (double) Main.blockSize / 1.5));
            g.setFill(Paint.valueOf("#20fc03"));
            g.fillText(cost, centreX, centreY);
        }
    }

    private Node cheapestNode(List<Node> unvisited) {
        Node cheapest = unvisited.get(0);
        for (Node node : unvisited) {
            if (cheapest.getCost() > node.getCost()) cheapest = node;
        }
        return cheapest;
    }

    public HashSet<Node> getVisited() {
        return visited;
    }
}
