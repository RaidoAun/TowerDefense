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
    private Map map;
    private int startX;
    private int startY;

    public NewPathfinder(int startX, int startY, Map mapClass) {
        this.startX = startX;
        this.startY = startY;
        this.visited = new HashSet<>();
        this.ends = new HashSet<>();
        this.map = mapClass;
        map.getMap_matrix()[startX][startY].setNexus();
    }

    public void resetMap() {
        visited = new HashSet<>();
        for (Node[] veerg : map.getMap_matrix()) {
            for (Node node : veerg) {
                node.setInUnvisitedList(false);
                node.setVisited(false);
                if (node.getCost() != 0) {
                    node.setParent(null);
                    node.setCost(Double.POSITIVE_INFINITY);
                }
            }
        }
    }

    public void addWall(int indexX, int indexY) {
        map.getMap_matrix()[indexX][indexY].setWall();
    }

    public void addEnd(int indexX, int indexY) {
        this.ends.add(map.getMap_matrix()[indexX][indexY]);
    }

    public HashSet<Node> getPathNodes() {
        HashSet<Node> path = new HashSet<>();
        HashSet<Node> endsToRemove = new HashSet<>();
        for (Node end : ends) {
            if (end.getParent() == null) {
                endsToRemove.add(end);
            } else {
                Node startNode = map.getMap_matrix()[startX][startY];
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
        unvisited.add(map.getMap_matrix()[startX][startY]);
        map.getMap_matrix()[startX][startY].setInUnvisitedList(true);
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
