package states;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import towerdefense.Main;

public class MenuState implements State {

    private States state;
    private StateManager sm;
    private Scene menu;
    private boolean rendered = false;

    public MenuState(StateManager sm) {
        this.sm = sm;
        state = States.MENU;
        menu = createMenuScene();
    }

    private Scene createMenuScene() {
        VBox menuLayout = new VBox();
        menuLayout.setAlignment(Pos.CENTER);
        menuLayout.setSpacing(20);
        Button play = new Button("Play");
        Button settings = new Button("Setting");
        Button exit = new Button("Exit");
        menuLayout.getChildren().addAll(play, settings, exit);
        return new Scene(menuLayout, 400, 200);
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(GraphicsContext g) {
        if (!rendered) {
            System.out.println("YES");
            Platform.runLater(() -> {
                Main.setScene(menu);
                rendered = true;
            });
        }
    }

    @Override
    public States getState() {
        return state;
    }

    @Override
    public void kill() {
        rendered = false;
    }
}
