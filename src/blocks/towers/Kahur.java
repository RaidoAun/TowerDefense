package blocks.towers;

import entities.Explosion;
import entities.Monster;
import entities.Projectile;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import map.Map;
import states.GameState;
import towerdefense.Main;

import java.util.HashSet;

public class Kahur extends Tower {
    public Kahur(int x, int y) {
        super(Towers.KAHUR, x, y);
        this.explosionRadius = 2*Main.blockSize;
        this.activeExplosions=new HashSet<>();
    }

    @Override
    public void tick(Map map) {
        Monster monster = getClosestMonster(map.getAllMonsters());
        if (idleTime >= cooldown && monster != null) {
            if (monster.distanceFrom(this.pixelX, this.pixelY) <= this.range) {
                Projectile missile = new Projectile(this, monster, 2*Main.blockSize/15, Main.blockSize * 0.3);
                shotProjectiles.add(missile);
            }
            idleTime = 0;
        } else {
            idleTime += 1;
        }
        HashSet<Projectile> projectiles = new HashSet<>(shotProjectiles);
        for (Projectile projectile : projectiles) {
            projectile.tick(map);
        }
        if (getactiveExplosions().size()>0){
            HashSet<Explosion> toRemoveExplosions = new HashSet<>();
            for (Explosion explosion:getactiveExplosions()) {
                explosion.tick(map);
                if (explosion.getTime()<=0){
                    toRemoveExplosions.add(explosion);
                }
            }
            getactiveExplosions().removeAll(toRemoveExplosions);
        }
    }

    @Override
    public void render(GraphicsContext g) {
        for (Projectile projectile : shotProjectiles) {
            projectile.render(g);
        }
        for (Explosion explosion:getactiveExplosions()) {
            explosion.render(g);
        }
    }

    @Override
    public void lvlUp() {
        this.level += 1;
        this.range += (Towers.KAHUR.getRange()/10)*Main.blockSize;
        this.damage += (Towers.KAHUR.getDmg()/10);
    }

    @Override
    public void sell() {
        GameState.updateMoney((int) (Towers.KAHUR.getHind()*(10+this.level)*0.05));
    }
}
