package states;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;

public class PauseState implements State {

    private StateManager sm;
    private Scene pause;
    private States state;

    public PauseState(StateManager sm) {
        this.sm = sm;
        this.pause = getPauseScene();
        this.state = States.PAUSE;
    }

    public Scene getPauseScene() {
        VBox pauseLayout = new VBox();
        pauseLayout.setSpacing(20);
        pauseLayout.setAlignment(Pos.CENTER);
        Button resume = new Button("Resume");
        resume.setOnAction(e -> resume());
        Button restart = new Button("Restart");
        restart.setOnAction(e -> restart());
        Button exit = new Button("Exit to main menu");
        exit.setOnAction(e -> exit());
        pauseLayout.getChildren().addAll(resume, restart, exit);
        Scene pauseScene = new Scene(pauseLayout);
        pauseScene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                sm.setState(sm.getPreviousState().getState());
            }
        });
        return pauseScene;
    }

    private void resume() {
        sm.setState(sm.getPreviousState().getState());
    }

    private void restart() {
        if (sm.getPreviousState().getState() == States.PATHFINDING) {
            sm.resetStates(States.PATHFINDING);
            sm.setState(States.PATHFINDING);
        } else {
            sm.resetStates(States.NEXUS, States.GAME);
            sm.setState(States.NEXUS);
        }
    }

    private void exit() {
        sm.setState(States.MENU);
    }

    @Override
    public void tick() {

    }

    @Override
    public void render() {
        if (sm.getWindow().getScene() != pause) {
            sm.changeScene(pause);
            sm.toggleFullscreen();
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
