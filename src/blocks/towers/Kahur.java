package blocks.towers;

import entities.Monster;
import entities.Projectile;
import javafx.scene.canvas.GraphicsContext;
import map.Map;
import towerdefense.Main;

import java.util.HashSet;

public class Kahur extends Tower {

    public Kahur(int x, int y) {
        super(Towers.KAHUR, x, y);
    }

    @Override
    public void tick(Map map) {
        HashSet<Projectile> projectiles = new HashSet<>(shotProjectiles);
        for (Projectile projectile : projectiles) {
            projectile.tick(null);
        }
        Monster monster = getClosestMonster(map.getAllMonsters());
        if (idleTime >= cooldown && monster != null) {
            if (monster.distanceFrom(this.pixelX, this.pixelY) <= this.range) {
                Projectile missile = new Projectile(this.pixelX, this.pixelY, this.damage, 2, Main.blockSize, this.color, monster, this);
                shotProjectiles.add(missile);
            }
            idleTime = 0;
        } else {
            idleTime += 1;
        }
    }

    @Override
    public void render(GraphicsContext g) {
        for (Projectile projectile : shotProjectiles) {
            projectile.render(g);
        }
    }
}
