package blocks.towers;

import entities.Monster;
import javafx.scene.canvas.GraphicsContext;
import map.Map;

public class Külmutaja extends Tower {

    public Külmutaja(int x, int y) {
        super(Towers.KÜLMUTAJA, x, y);
    }

    @Override
    public void tick(Map map) {
        for (Monster monster : map.getAllMonsters()) {
            if (monster.distanceFrom(this.pixelX, this.pixelY) <= this.range) {
                monster.setSpeed((100-this.damage)/100*monster.getSpeed());
            }
        }
    }

    @Override
    public void render(GraphicsContext g) {

    }
}
