import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;

public class Pause {

    public static Scene getPauseScreen() {
        VBox pauseLayout = new VBox();
        pauseLayout.setSpacing(20);
        pauseLayout.setAlignment(Pos.CENTER);
        Button resume = new Button("Resume");
        Button restart = new Button("Restart");
        restart.setOnAction(e -> restart());
        Button exit = new Button("Exit to main menu");
        exit.setOnAction(e -> exit());
        pauseLayout.getChildren().addAll(resume, restart, exit);
        Scene pause = new Scene(pauseLayout);
        pause.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.F11) {
                Main.toggleFullscreen();
            }
        });
        return pause;
    }

    private static void restart() {
        Main.startGame();
    }

    private static void exit() {
        Main.switchToMenu();
    }

}
