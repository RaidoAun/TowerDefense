import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {

    private static Scene menu_scene;
    private static Stage window;
    private static int screenH = (int) Screen.getPrimary().getBounds().getHeight();
    private static int screenW = (int) Screen.getPrimary().getBounds().getWidth();
    private static int blockSize = 15;
    private static int spawnCount = 3;
    private static int spawnSpacing = 20;

    public static int getSpawnCount() {
        return spawnCount;
    }

    public static void setSpawnCount(int spawnCount) {
        Main.spawnCount = spawnCount;
    }

    public static int getSpawnSpacing() {
        return spawnSpacing;
    }

    public static void setSpawnSpacing(int spawnSpacing) {
        Main.spawnSpacing = spawnSpacing;
    }

    public static int getBlockSize() {
        return blockSize;
    }

    public static void setBlockSize(int blockSize) {
        Main.blockSize = blockSize;
    }

    public static Stage getWindow() {
        return window;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void switchToMenu() {
        window.setTitle("TowerDefense");
        window.setScene(menu_scene);
    }

    public static int getScreenH() {
        return screenH;
    }

    public static int getScreenW() {
        return screenW;
    }

    public static void toggleFullscreen() {
        if (window.isFullScreen()) {
            window.setFullScreen(false);
            window.setMaximized(true);
        } else {
            window.setFullScreen(true);
        }
    }

    public static void startGame() {

        Game.setValues();
        window.setScene(Game.getGameScene());
        Game.generateMap();
        window.setFullScreen(true);
        PopUp.createPopup("Vali nexuse asukoht kaardil!\nMäng algab pärast nexuse maha panekut!", true);

        Game.getCanvas().setOnMouseClicked(e -> {
            if (!Game.getMap().isNexus()) {
                Game.chooseNexus(e);
                if (Game.getMap().isNexus()) {
                    Game.runDijkstra();
                    //Game.resumeAnimation();
                }
            } else {
                //Game.clickDurigGame(e);
            }
        });

    }

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        VBox menu_button_box = new VBox();
        menu_button_box.setSpacing(20);
        menu_scene = new Scene(menu_button_box, 500, 250);
        Button play_btn = new Button("Play");
        Button settings_btn = new Button("Settings");
        Button exit = new Button("Exit");

        menu_button_box.getChildren().addAll(play_btn, settings_btn, exit);
        menu_button_box.setAlignment(Pos.CENTER);

        window.setTitle("TowerDefense");
        window.setScene(menu_scene);
        window.setFullScreenExitHint("Press F11 to turn fullscreen on/off");
        window.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        window.show();

        play_btn.setOnAction(event -> startGame());

        settings_btn.setOnAction(e -> {

            window.setTitle("Settings");
            window.setScene(Settings.settingsScene());

        });

        exit.setOnAction(e -> window.close());

    }

}

