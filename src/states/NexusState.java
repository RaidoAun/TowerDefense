package states;

import blocks.Nexus;
import blocks.Spawnpoint;
import gui.PopUp;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import map.Map;
import tools.Click;
import tools.Converter;
import towerdefense.Main;

public class NexusState implements State {

    private StateManager sm;
    private Scene game;
    private States state;
    private GraphicsContext g;
    private Map map;
    private Click click;

    public NexusState(StateManager sm) {
        this.sm = sm;
        this.state = States.NEXUS;
        this.g = sm.getCanvas().getGraphicsContext2D();
        this.map = getGeneratedMap();
        this.game = getGameScene();
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

        sm.getCanvas().setOnMouseClicked(e -> click = new Click(e, this.map));

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
        if (click != null) {
            System.out.println("Click");
        }
        if (click != null && map.getNexus() == null) {
            System.out.println("Clicky");
            map.setNexusxy(new int[]{click.indexX, click.indexY});
            if (click.eventblock.getId() != 0) {
                Platform.runLater(() -> PopUp.createPopup("Valitud nexuse asukoht ei sobi! (sein)\nProovi uuesti!"));
            } else if (map.getSpawnpoints().get(0).genPathReturn(0).length == 0) {
                Platform.runLater(() -> PopUp.createPopup("Valitud nexuse asukoht ei sobi! (no path)\nProovi uuesti!"));
            } else {
                map.genNexus();
                map.genPathstoNexus(500);
                for (Spawnpoint spawn : map.getSpawnpoints()) {
                    map.drawPath(spawn.getPath());
                }
                map.setNexus(new Nexus(0, 0));
                GameState gameState = new GameState(sm, game, map);
                sm.replaceState(States.GAME, gameState);
                sm.setState(States.GAME);
            }
        }
        click = null;
    }

    @Override
    public void render() {

        map.drawMap(Main.blockSize);

        if (sm.getWindow().getScene() != this.game) {
            sm.changeScene(game);
            sm.toggleFullscreen();
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
        map = getGeneratedMap();
        click = null;
    }
}
