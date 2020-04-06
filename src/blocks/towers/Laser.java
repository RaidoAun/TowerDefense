package blocks.towers;

import entities.Monster;
import javafx.scene.canvas.GraphicsContext;
import map.Map;

import java.util.HashSet;

public class Laser extends Tower {

    HashSet<Monster> monstersToLaser;

    public Laser(int x, int y) {
        super(Towers.LASER, x, y);
        monstersToLaser = new HashSet<>();
    }

    @Override
    public void tick(Map map) {
        if (idleTime >= cooldown) {
            for (Monster monster : map.getAllMonsters()) {
                if (monster.distanceFrom(this.pixelX, this.pixelY) <= this.range) {
                    monster.setHp(monster.getHp() - damage);
                    monstersToLaser.add(monster);
                }
            }
            idleTime = 0;
        } else {
            idleTime += 1;
        }
    }

    @Override
    public void render(GraphicsContext g) {
        for (Monster monster : monstersToLaser) {
            g.setStroke(color);
            g.setLineWidth(5);
            g.strokeLine(monster.getPixelX(), monster.getPixelY(), this.pixelX, this.pixelY);
        }
        monstersToLaser.clear();
    }
}