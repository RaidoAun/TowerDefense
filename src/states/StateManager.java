package states;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import towerdefense.Main;

import java.util.HashMap;

public class StateManager {

    private Stage window;
    private State currentState;
    private HashMap<States, State> allStates;
    private Canvas canvas;
    private State previousState;

    public StateManager(Stage window) {
        this.window = window;
        this.canvas = new Canvas(Main.screenW, Main.screenH);
        allStates = new HashMap<>();
    }

    public void tick() {
        currentState.tick();
    }

    public void render() {
        currentState.render();
    }

    public Stage getWindow() {
        return window;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void addState(State state) {
        if (currentState == null) {
            currentState = state;
        }
        if (!allStates.containsKey(state.getState())) {
            allStates.put(state.getState(), state);
        }
    }

    public void addAllStates(State... states) {
        for (State state : states) {
            if (!allStates.containsKey(state.getState())) {
                allStates.put(state.getState(), state);
            }
        }
    }

    public void setState(States state) {
        if (allStates.containsKey(state)) {
            previousState = currentState;
            currentState = allStates.get(state);
        } else {
            System.err.println("The state you want to set doesn't exist!");
        }
    }

    public void changeScene(Scene scene) {
        window.setScene(scene);
    }

    public void toggleFullscreen() {
        if (window.isFullScreen()) {
            window.setFullScreen(false);
            window.setMaximized(true);
        } else {
            window.setFullScreen(true);
        }
    }

    public State getPreviousState() {
        return previousState;
    }

    public void resetStates(States... states) {
        for (States state : states) {
            if (allStates.containsKey(state)) {
                allStates.get(state).reset();
            }
        }
    }

    public void replaceState(States whatState, State state) {
        if (allStates.containsKey(whatState)) {
            allStates.replace(whatState, state);
        } else {
            allStates.put(whatState, state);
        }
    }

}
