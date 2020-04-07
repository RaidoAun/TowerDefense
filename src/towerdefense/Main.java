package towerdefense;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCombination;
import javafx.stage.Screen;
import javafx.stage.Stage;
import states.*;

public class Main extends Application {

    public static int screenH = (int) Screen.getPrimary().getBounds().getHeight();
    public static int screenW = (int) Screen.getPrimary().getBounds().getWidth();
    public static int blockSize = 15;
    public static Stage window;
    public static int spawnCount = 3;
    public static int spawnSpacing = 20;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        primaryStage.setFullScreenExitHint("Press F11 to toggle fullscreen");
        primaryStage.setTitle("TowerDefenseGaem");
        //Windowist tuleb lahti saada.
        window = primaryStage;
        Canvas gameCanvas = new Canvas(screenW, screenW);
        StateManager stateManager = new StateManager(primaryStage, gameCanvas);
        MenuState menu = new MenuState(stateManager);
        stateManager.addState(menu);
        SettingsState settings = new SettingsState(stateManager);
        NexusState nexus = new NexusState(stateManager);
        PauseState pause = new PauseState(stateManager);
        stateManager.addAllStates(settings, nexus, pause);
        primaryStage.show();

        AnimationTimer gameLoop = new AnimationTimer() {

            double timer;
            int ticks;
            long oldTime;

            @Override
            public void handle(long now) {

                timer += (double) (now - oldTime) / 1000000000;
                oldTime = now;

                stateManager.tick();
                stateManager.render();
                ticks += 1;

                //FPS counter, AnimationTimeri fps on reeglina 60.
                if (timer >= 1) {
                    //System.out.println("Fps: " + ticks);
                    timer = 0;
                    ticks = 0;
                }
            }
        };
        gameLoop.start();
    }

}

