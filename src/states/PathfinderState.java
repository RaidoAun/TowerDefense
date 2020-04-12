package states;

import blocks.Nexus;
import blocks.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import map.Map;
import map.NewPathfinder;
import tools.Click;
import towerdefense.Main;

import java.util.HashSet;

public class PathfinderState implements State {

    private StateManager sm;
    private States state;
    private Scene scene;
    private GraphicsContext g;
    private Map map;
    private Click click;
    private NewPathfinder pathfinder;
    private HashSet<Node> path;

    public PathfinderState(StateManager sm) {
        this.sm = sm;
        this.state = States.PATHFINDING;
        this.g = sm.getCanvas().getGraphicsContext2D();
        this.map = getGeneratedMap();
        this.scene = getBuiltScene();
    }

    @Override
    public void tick() {
        if (sm.getWindow().getScene() != this.scene) {
            sm.changeScene(this.scene);
            sm.toggleFullscreen();
        }

        if (click != null) {
            System.out.println(map.getMap_matrix()[click.indexX][click.indexY].id);
            System.out.println(map.getMap_matrix()[click.indexX][click.indexY].getColor());
        }

        if (click != null && pathfinder == null) {
            pathfinder = new NewPathfinder(click.indexX, click.indexY, map);
            pathfinder.scanMap();
            map.editMap_matrix(click.indexX, click.indexY, new Nexus(click.indexX, click.indexY));
        } else if (click != null) {
            if (click.secondary) {
                map.setWall(click.indexX, click.indexY);
                pathfinder.addWall(click.indexX, click.indexY);
                pathfinder.resetMap();
                pathfinder.scanMap();
            } else {
                pathfinder.addEnd(click.indexX, click.indexY);
            }
            path = pathfinder.getPathNodes();
        }
        this.click = null;
    }

    @Override
    public void render() {
        map.drawMap(Main.blockSize);
        map.colorAllBlocks(Color.DARKOLIVEGREEN, path);
        map.drawGrid(0.2, Color.BLACK);
        if (pathfinder != null) {
            pathfinder.drawCost(g);
        }
    }

    @Override
    public States getState() {
        return state;
    }

    @Override
    public void reset() {
        this.map = getGeneratedMap();
        this.pathfinder = null;
        this.click = null;
        this.path = null;
    }

    private Scene getBuiltScene() {
        GridPane gameLayout = new GridPane();
        gameLayout.getChildren().add(sm.getCanvas());
        Scene scene = new Scene(gameLayout);

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.F11) {
                sm.toggleFullscreen();
            } else if (e.getCode() == KeyCode.ESCAPE) {
                sm.setState(States.PAUSE);
            }
        });

        return scene;
    }

    private Map getGeneratedMap() {
        System.out.println("Building the map...");
        long start = System.currentTimeMillis();
        int x = (int) Math.ceil((double) Main.screenW / Main.blockSize);
        int y = (int) Math.ceil((double) Main.screenH / Main.blockSize);
        Map notMap = new Map(x, y, g);
        notMap.initMap();
        notMap.genMap(2);
        long end = System.currentTimeMillis();
        System.out.println("Done! Building took " + (end - start) + " milliseconds.");

        sm.getCanvas().setOnMouseClicked(e -> click = new Click(e, this.map));

        return notMap;
    }


}
