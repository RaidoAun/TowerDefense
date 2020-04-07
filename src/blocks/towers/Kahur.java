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
        Monster monster = getClosestMonster(map.getAllMonsters());
        if (idleTime >= cooldown && monster != null) {
            if (monster.distanceFrom(this.pixelX, this.pixelY) <= this.range) {
                Projectile missile = new Projectile(this, monster, 3, Main.blockSize * 0.75);
                shotProjectiles.add(missile);
            }
            idleTime = 0;
        } else {
            idleTime += 1;
        }
        HashSet<Projectile> projectiles = new HashSet<>(shotProjectiles);
        for (Projectile projectile : projectiles) {
            projectile.tick(null);
        }
    }

    @Override
    public void render(GraphicsContext g) {
        for (Projectile projectile : shotProjectiles) {
            projectile.render(g);
        }
    }

    @Override
    public void lvlUp() {
        this.level += 1;
        this.range += 10;
        this.damage += 1;
    }
}
