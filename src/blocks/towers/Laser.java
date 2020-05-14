package blocks.towers;

import entities.Monster;
import javafx.scene.canvas.GraphicsContext;
import map.Map;
import states.GameState;
import towerdefense.Main;

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
            monstersToLaser.clear();
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
            if (monster.getHp()>0&& !monster.isReachedNexus()){
                g.setStroke(color);
                g.setLineWidth((double) Main.blockSize / 10);
                double a = monster.getPixelX()-(monster.getPixelX()-this.pixelX)/(monster.distanceFrom(this.pixelX,this.pixelY)/10);
                double b = monster.getPixelY()-(monster.getPixelY()-this.pixelY)/(monster.distanceFrom(this.pixelX,this.pixelY)/10);
                g.strokeLine(monster.getPixelX(), monster.getPixelY(), a, b);
            }
        }

    }

    @Override
    public void lvlUp() {
        int upgradePrice = (int) (this.getHind() * 0.1);
        if (this.getMaxLevel() > this.getLevel() && GameState.raha >= upgradePrice) {
            GameState.updateMoney(-upgradePrice);
            this.level += 1;
            this.range += (Towers.LASER.getRange()/10)*Main.blockSize;
            this.damage += (Towers.LASER.getDmg()/10);
        }
    }

    @Override
    public void sell() {
        GameState.updateMoney((int) (Towers.LASER.getHind()*(10+this.level)*0.05));
    }
}
