package blocks.towers;

import entities.Monster;
import entities.Projectile;
import javafx.scene.canvas.GraphicsContext;
import map.Map;
import states.GameState;
import towerdefense.Main;

import java.util.HashSet;

public class Kuulipilduja extends Tower {

    private Monster target;

    public Kuulipilduja(int x, int y) {
        super(Towers.KUULIPILDUJA, x, y);
    }

    @Override
    public void tick(Map map) {

        if (map.getAllMonsters().size() > 0) {
            Monster potensialTarget = getClosestMonster(map.getAllMonsters());
            if (target == null && potensialTarget.distanceFrom(this.pixelX, this.pixelY) <= this.range) {
                target = potensialTarget;
            } else if (target != null && (target.distanceFrom(this.pixelX, this.pixelY) > this.range || target.isReachedNexus() || target.getHp() <= 0)) {
                target = null;
            }
        }

        if (idleTime >= cooldown && target != null) {
            Projectile missile = new Projectile(this, target, 4*Main.blockSize/15, Main.blockSize * 0.25);
            shotProjectiles.add(missile);
            idleTime = 0;
        } else {
            idleTime += 1;
        }
        HashSet<Projectile> projectiles = new HashSet<>(shotProjectiles);
        for (Projectile projectile : projectiles) {
            projectile.tick(map);
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
        int upgradePrice = (int) (this.getHind() * 0.1);
        if (this.getMaxLevel() > this.getLevel() && GameState.raha >= upgradePrice) {
            GameState.updateMoney(-upgradePrice);
            this.level += 1;
            this.range += (Towers.KUULIPILDUJA.getRange()/10)*Main.blockSize;
            this.damage += (Towers.KUULIPILDUJA.getDmg()/10);
        }
    }

    @Override
    public void sell() {
        GameState.updateMoney((int) (Towers.KUULIPILDUJA.getHind()*(10+this.level)*0.05));
    }
}
