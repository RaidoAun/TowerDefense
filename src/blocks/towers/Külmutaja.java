package blocks.towers;

import entities.Monster;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import map.Map;
import states.GameState;
import towerdefense.Main;

import java.util.HashSet;

public class Külmutaja extends Tower {

    HashSet<Monster> frozenMonsters;
    HashSet<Double> activeCircles;

    public Külmutaja(int x, int y) {
        super(Towers.KÜLMUTAJA, x, y);
        this.frozenMonsters = new HashSet<>();
        this.activeCircles = new HashSet<>();
    }

    @Override
    public void tick(Map map) {

        for (Monster monster : map.getAllMonsters()) {
            double distance = monster.distanceFrom(this.pixelX, this.pixelY);
            if (distance <= this.range && !frozenMonsters.contains(monster) && frozenMonsters.size() < (damage / 4)) {
                frozenMonsters.add(monster);
                monster.setSlowDebuff(monster.getSlowDebuff() + (double) this.damage / 100);
            }
        }

        HashSet<Monster> frozenToRemove = new HashSet<>();
        for (Monster frozen : frozenMonsters) {
            double distance = frozen.distanceFrom(this.pixelX, this.pixelY);
            if (distance > this.range || frozen.isReachedNexus() || frozen.getHp() <= 0) {
                frozenToRemove.add(frozen);
                frozen.setSlowDebuff(frozen.getSlowDebuff() - (double) this.damage / 100);
            }
        }
        frozenMonsters.removeAll(frozenToRemove);

        if (idleTime >= cooldown) {
            activeCircles.add(0d);
            idleTime = 0;
        } else {
            idleTime += 1;
        }

        HashSet<Double> circlesToRemove = new HashSet<>();
        HashSet<Double> circlesToAdd = new HashSet<>();
        for (Double circle : activeCircles) {
            if (circle <= this.range) {
                circlesToRemove.add(circle);
                circlesToAdd.add(circle + 1);
            } else {
                circlesToRemove.add(circle);
            }
        }
        activeCircles.addAll(circlesToAdd);
        activeCircles.removeAll(circlesToRemove);
    }

    @Override
    public void render(GraphicsContext g) {
        for (Double circle : activeCircles) {
            double r = circle;
            g.setStroke(new Color(color.getRed(), color.getGreen(), color.getGreen(), 0.5));
            g.setLineWidth(5);
            g.strokeOval(pixelX - r, pixelY - r, r * 2, r * 2);
        }
    }

    @Override
    public void lvlUp() {
        int upgradePrice = (int) (this.getHind() * 0.1);
        if (this.getMaxLevel() > this.getLevel() && GameState.raha >= upgradePrice) {
            GameState.updateMoney(-upgradePrice);
            this.level += 1;
            this.range += (Towers.KÜLMUTAJA.getRange()/10)*Main.blockSize;
            this.damage += 5;//(Towers.KÜLMUTAJA.getDmg()/10);
        }
    }

    @Override
    public void sell() {
        GameState.updateMoney((int) (Towers.KÜLMUTAJA.getHind()*(10+this.level)*0.05));
        for (Monster monster : frozenMonsters) {
            monster.setSlowDebuff(monster.getSlowDebuff() - (double) this.damage / 100);
        }
    }
}
