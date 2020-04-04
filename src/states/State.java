package states;

import javafx.scene.canvas.GraphicsContext;

public interface State {

    void tick();

    void render(GraphicsContext g);

    States getState();

    void kill();

}
