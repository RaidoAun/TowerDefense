import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Monster {

    private int hp;
    private int dmg;
    private int id;
    private double x;
    private double y;
    private int step;
    private double speed;
    private Color color;
    private int money;
    private boolean reachedNexus;
    private List<Projectile> lockedOnMissiles;
    private Spawnpoint spawnPoint; //Spawnpoint, kust koletis alguse sai.

    Monster(Monsters type, double x_coord, double y_coord, Spawnpoint spawnPoint) {
        this.x = x_coord;
        this.y = y_coord;
        this.step = 0;
        this.id = type.getId();
        this.hp = type.getHp();
        this.dmg = type.getDamage();
        this.speed = type.getKiirus();
        this.color = type.getColor();
        this.money = type.getMoney();
        this.reachedNexus = false;
        this.lockedOnMissiles = new ArrayList<>();
        this.spawnPoint = spawnPoint;
    }

    public void drawMonster() {
        GraphicsContext g = Game.getG();
        g.setFill(this.color);
        double diameeter = (double) Game.getBlockSize() / 2;
        g.fillOval(this.x - diameeter / 2, this.y - diameeter / 2, diameeter, diameeter);
    }

    public int getMoney() {
        return money;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getDmg() {
        return dmg;
    }

    public boolean hasReachedNexus() {
        return reachedNexus;
    }

    void move() {
        move(this.spawnPoint.getPath(), Game.getLastFrameTime() * this.speed);
    }

    void move(int[][] path, double speed) {
        if (this.step < path.length) {
            int distx = (int) (((path[this.step][0] + 0.5) * Game.getBlockSize()) - this.x);
            int disty = (int) (((path[this.step][1] + 0.5) * Game.getBlockSize()) - this.y);
            if (distx < 0) {
                if (speed >= -distx) {
                    this.x = distx + this.x;
                    this.step += 1;
                    move(path, speed + distx);
                } else {
                    this.x = this.x - speed;
                }
            } else if (distx > 0) {
                if (speed >= distx) {
                    this.x = distx + this.x;
                    this.step += 1;
                    move(path, speed - distx);
                } else {
                    this.x = this.x + speed;
                }
            } else if (disty < 0) {
                if (speed >= -disty) {
                    this.y = disty + this.y;
                    this.step += 1;
                    move(path, speed + disty);
                } else {
                    this.y = this.y - speed;
                }
            } else if (disty > 0) {
                if (speed >= disty) {
                    this.y = disty + this.y;
                    this.step += 1;
                    move(path, speed - disty);
                } else {
                    this.y = this.y + speed;
                }
            }
            if (distx == 0 && disty == 0) {
                this.step += 1;
            }
        } else {
            this.reachedNexus = true;
        }
    }

    public void addMissile(Projectile missile) {
        lockedOnMissiles.add(missile);
    }

    public void updateMissilesEndpoint() {
        for (Projectile missile : this.lockedOnMissiles) {
            missile.setEndPoint(this.x, this.y);
        }
    }

    public void pullMissiles() {
        List<Projectile> toRemove = new ArrayList<>();
        for (Projectile missile : lockedOnMissiles) {
            missile.moveMissile();
            if (missile.hasReachedEnd()) {
                this.hp -= missile.getDamage();
                toRemove.add(missile);
            }
        }
        this.lockedOnMissiles.removeAll(toRemove);
    }

    public Spawnpoint getSpawnPoint() {
        return spawnPoint;
    }
}