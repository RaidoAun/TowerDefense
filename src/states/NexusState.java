package states;

import gui.PopUp;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import blocks.Block;
import map.Map;
import blocks.Spawnpoint;
import tools.Converter;
import towerdefense.Main;

public class NexusState implements State {

    private StateManager sm;
    private Scene game;
    private States state;
    private GraphicsContext g;
    private Map map;
    private Block clickedBlock;

    public NexusState(StateManager sm) {
        this.sm = sm;
        this.state = States.NEXUS;
        this.g = sm.getCanvas().getGraphicsContext2D();
        this.game = getGameScene();
    }

    public Scene getGame() {
        return game;
    }

    private Map getGeneratedMap() {
        int x = (int) Math.ceil((double) Main.screenW / Main.blockSize);
        int y = (int) Math.ceil((double) Main.screenH / Main.blockSize);
        Map notMap = new Map(x, y, g);
        notMap.initMap();
        notMap.genMap(2);
        notMap.genFlippedMap();
        notMap.generateSpawnpoints();
        notMap.spawnSpawnpoints();
        return notMap;
    }

    private Scene getGameScene() {
        GridPane gameLayout = new GridPane();
        gameLayout.getChildren().add(sm.getCanvas());
        Scene gameScene = new Scene(gameLayout);

        gameScene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.F11) {
                sm.toggleFullscreen();
            } else if (e.getCode() == KeyCode.ESCAPE) {
                sm.setState(States.PAUSE);
            }
        });

        return gameScene;
    }

    @Override
    public void tick() {
        if (clickedBlock != null && !map.getSpawnpoints().get(0).isNexusWithPath()) {
            map.setNexusxy(new int[]{clickedBlock.indexX, clickedBlock.indexY});
            if (clickedBlock.getId() != 0) {
                Platform.runLater(() -> PopUp.createPopup("Valitud nexuse asukoht ei sobi! (sein)\nProovi uuesti!"));
            } else if (map.getSpawnpoints().get(0).genPathReturn(0).length == 0) {
                Platform.runLater(() -> PopUp.createPopup("Valitud nexuse asukoht ei sobi! (no path)\nProovi uuesti!"));
            } else {
                map.genNexus();
                map.genPathstoNexus(500);
                for (Spawnpoint spawn : map.getSpawnpoints()) {
                    map.drawPath(spawn.getPath());
                }
                GameState gameState = new GameState(sm, game, map);
                sm.replaceState(States.GAME, gameState);
                sm.setState(States.GAME);
            }
            clickedBlock = null;
        }
    }

    @Override
    public void render() {

        if (map == null) {
            System.out.println("Building the map...");
            long start = System.currentTimeMillis();
            map = getGeneratedMap();
            long end = System.currentTimeMillis();
            System.out.println("Done! Building took " + (end - start) + " milliseconds.");
        } else {
            map.drawMap(Main.blockSize);
        }

        if (sm.getWindow().getScene() != this.game) {
            sm.changeScene(game);
            sm.toggleFullscreen();
            sm.getCanvas().setOnMouseClicked(e -> {
                if (map != null) {
                    clickedBlock = map.getBlock(Converter.pixToIndex((int) e.getX()), Converter.pixToIndex((int) e.getY()));
                }
            });
            if (!map.getSpawnpoints().get(0).isNexusWithPath()) {
                Platform.runLater(() -> PopUp.createPopup("Vali nexuse asukoht kaardil!\nMäng algab pärast nexuse maha panekut!"));
            }
        }
    }

    @Override
    public States getState() {
        return state;
    }

    @Override
    public void reset() {
        map = null;
        g.clearRect(0, 0, Main.screenW, Main.screenH);
        clickedBlock = null;
    }
}
