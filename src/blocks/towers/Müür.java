package blocks.towers;

import javafx.scene.canvas.GraphicsContext;
import map.Map;
import states.GameState;

public class Müür extends Tower {

    public Müür(int x, int y) {
        super(Towers.MÜÜR, x, y);
    }

    @Override
    public void tick(Map map) {

    }

    @Override
    public void render(GraphicsContext g) {

    }

    @Override
    public void lvlUp() {
        this.level += 1;
    }

    @Override
    public void sell() {

    }
}
