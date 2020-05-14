package entities;

import blocks.Spawnpoint;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import map.Map;
import states.GameState;
import towerdefense.Main;

public class Monster extends Entity {

    private double slowDebuff;
    private int hp;
    private final int dmg;
    private int step;
    private final int money;
    private final Monsters type;
    private boolean reachedNexus;
    private final Spawnpoint spawnPoint; //blocks.Spawnpoint, kust koletis alguse sai.

    public Monster(Monsters type, double pixelX, double pixelY, Spawnpoint spawnPoint) {
        super(pixelX, pixelY, type.getColor(), type.getKiirus());
        this.step = 0;
        this.hp = type.getHp();
        this.dmg = type.getDamage();
        this.money = type.getMoney();
        this.reachedNexus = false;
        this.spawnPoint = spawnPoint;
        this.slowDebuff = 0;
        this.speed*= Main.blockSize;
        this.type = type;
    }

    @Override
    public void tick(Map map) {

        if (hp <= 0) {
            GameState.updateMoney(money);
            map.getAllMonsters().remove(this);
        } else if (reachedNexus) {
            GameState.updateHealth(-dmg);
            GameState.updateMoney(money);
            map.getAllMonsters().remove(this);
        }

        if (slowDebuff > 1) {
            slowDebuff = 1;
        } else if (slowDebuff < 0) {
            slowDebuff = 0;
        }

        move();
    }

    @Override
    public void render(GraphicsContext g) {
        drawMonster(g);
    }

    public void drawMonster(GraphicsContext g) {
        g.setFill(this.color);
        double diameeter;
        if (type == Monsters.BOSS) {
            diameeter = (double) Main.blockSize * 0.75;
        } else {
            diameeter = (double) Main.blockSize / 2;
        }
        g.fillOval(this.pixelX - diameeter / 2, this.pixelY - diameeter / 2, diameeter, diameeter);
        if (slowDebuff > 0) {
            g.setStroke(Color.AQUA);
            g.setLineWidth((double) Main.blockSize / 20);
            g.strokeOval(this.pixelX - diameeter / 2, this.pixelY - diameeter / 2, diameeter, diameeter);
        }
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void move() {
        move(this.spawnPoint.getPath(), this.speed * (1 - slowDebuff));
    }

    public void move(int[][] path, double speed) {

        if (this.step < path.length) {
            int distx = (int) (((path[this.step][0] + 0.5) * Main.blockSize) - this.pixelX);
            int disty = (int) (((path[this.step][1] + 0.5) * Main.blockSize) - this.pixelY);
            if (distx < 0) {
                if (speed >= -distx) {
                    this.pixelX += distx;
                    this.step += 1;
                    move(path, speed + distx);
                } else {
                    this.pixelX -= speed;
                }
            } else if (distx > 0) {
                if (speed >= distx) {
                    this.pixelX += distx;
                    this.step += 1;
                    move(path, speed - distx);
                } else {
                    this.pixelX += speed;
                }
            } else if (disty < 0) {
                if (speed >= -disty) {
                    this.pixelY += disty;
                    this.step += 1;
                    move(path, speed + disty);
                } else {
                    this.pixelY -= speed;
                }
            } else if (disty > 0) {
                if (speed >= disty) {
                    this.pixelY += disty;
                    this.step += 1;
                    move(path, speed - disty);
                } else {
                    this.pixelY += speed;
                }
            }
            if (distx == 0 && disty == 0) {
                this.step += 1;
            }
        } else {
            int distx = (int) (((path[path.length-1][0] + 0.5) * Main.blockSize) - this.pixelX);
            int disty = (int) (((path[path.length-1][1] + 0.5) * Main.blockSize) - this.pixelY);
            if (distx==0 && disty == 0){
                this.reachedNexus = true;
            }
        }
    }

    public boolean isReachedNexus() {
        return reachedNexus;
    }

    public void setSlowDebuff(double slowDebuff) {
        this.slowDebuff = slowDebuff;
    }

    public double getSlowDebuff() {
        return slowDebuff;
    }
}