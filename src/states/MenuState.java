package states;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class MenuState implements State {

    private StateManager sm;
    private Scene menu;
    private States state;

    public MenuState(StateManager sm) {
        this.sm = sm;
        this.menu = getMenuScene();
        this.state = States.MENU;
    }

    private Scene getMenuScene() {
        VBox menuLayout = new VBox();
        menuLayout.setSpacing(20);
        menuLayout.setAlignment(Pos.CENTER);
        Button play = new Button("Play");
        Button settings = new Button("Settings");
        Button exit = new Button("Exit");

        play.setOnAction(e -> sm.setState(States.NEXUS));
        settings.setOnAction(e -> sm.setState(States.SETTINGS));
        exit.setOnAction(e -> sm.getWindow().close());

        menuLayout.getChildren().addAll(play, settings, exit);
        return new Scene(menuLayout, 500, 250);
    }

    @Override
    public void tick() {

    }

    @Override
    public void render() {
        if (sm.getWindow().getScene() != menu) {
            sm.changeScene(menu);
        }
    }

    @Override
    public States getState() {
        return state;
    }

    @Override
    public void reset() {

    }

}
