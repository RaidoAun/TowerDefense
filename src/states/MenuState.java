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
        Button dijkstra = new Button("Dijkstra Testing");
        Button exit = new Button("Exit");

        play.setOnAction(e -> {
            NexusState nexusState = new NexusState(sm);
            sm.replaceState(States.NEXUS, nexusState);
            sm.setState(States.NEXUS);
        });
        settings.setOnAction(e -> sm.setState(States.SETTINGS));
        dijkstra.setOnAction(e -> {
            PathfinderState testingSite = new PathfinderState(sm);
            sm.replaceState(States.PATHFINDING, testingSite);
            sm.setState(States.PATHFINDING);
        });
        exit.setOnAction(e -> sm.getWindow().close());

        menuLayout.getChildren().addAll(play, settings, dijkstra, exit);
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
