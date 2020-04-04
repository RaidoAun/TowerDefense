package states;

import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

import java.util.HashMap;

public class StateManager {

    private State currentState;
    private HashMap<States, State> states;

    public StateManager() {
        states = new HashMap<>();
    }

    public void addState(State state) {
        if (currentState == null) {
            currentState = state;
        }
        if (states.containsKey(state.getState())) {
            System.err.println("States list already contains this state!");
        } else {
            states.put(state.getState(), state);
        }
    }

    public void setCurrentState(States state) {
        if (states.containsKey(state)) {
            currentState = states.get(state);
        } else {
            System.err.println("This state doesn't exist!");
        }
    }

    public void tick() {
        currentState.tick();
    }

    public void render(GraphicsContext g) {
        currentState.render(g);
    }

}
