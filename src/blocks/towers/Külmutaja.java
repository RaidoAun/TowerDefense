package blocks.towers;

import entities.Monster;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import map.Map;

import java.util.HashSet;

public class Külmutaja extends Tower {

    HashSet<Monster> frozenMonsters;
    HashSet<Double> activeCircles;

    public Külmutaja(int x, int y) {
        super(Towers.KÜLMUTAJA, x, y);
        frozenMonsters = new HashSet<>();
        activeCircles = new HashSet<>();
    }

    @Override
    public void tick(Map map) {

        for (Monster monster : map.getAllMonsters()) {
            double distance = monster.distanceFrom(this.pixelX, this.pixelY);

            if (monster.isFrozen()) {
                if (distance <= this.range && !frozenMonsters.contains(monster) && frozenMonsters.size() < (damage / 4)) {
                    monster.setSpeed(((100 - (double) this.damage) / 100) * monster.getSpeed());
                    frozenMonsters.add(monster);
                } else if (distance > this.range && frozenMonsters.contains(monster)) {
                    monster.setSpeed((100 * monster.getSpeed()) / (100 - this.damage));
                    frozenMonsters.remove(monster);
                    monster.setFrozen(false);
                }
            } else {
                if (distance <= this.range && !frozenMonsters.contains(monster) && frozenMonsters.size() < (damage / 4)) {
                    monster.setSpeed(((100 - (double) this.damage) / 100) * monster.getSpeed());
                    frozenMonsters.add(monster);
                    monster.setFrozen(true);
                } else if (distance > this.range && frozenMonsters.contains(monster)) {
                    monster.setSpeed((100 * monster.getSpeed()) / (100 - this.damage));
                    frozenMonsters.remove(monster);
                }
            }
        }

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
        this.level += 1;
        this.range += 10;
        this.damage += 1;
    }
}
