package states;

public interface State {

    void tick();

    void render();

    States getState();

    void reset();

}
