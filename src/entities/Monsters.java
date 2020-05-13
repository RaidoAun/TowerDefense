package entities;

import javafx.scene.paint.Color;

public enum Monsters {

    TAVALINE(0.07, 10000, 10, 2, Color.YELLOW),
    KIIRE(0.15, 5000, 10, 3, Color.BLUE),
    TUGEV(0.03, 30000, 20, 4, Color.CRIMSON),
    BOSS(0.1, 100000, 50, 20, Color.CADETBLUE);

    private final double kiirus;
    private final int hp;
    private final int damage;
    private final int money;
    private final Color color;

    Monsters(double kiirus, int hp, int damage, int money, Color color) {
        this.kiirus = kiirus;
        this.hp = hp;
        this.damage = damage;
        this.money = money;
        this.color = color;
    }

    public double getKiirus() {
        return kiirus;
    }

    public int getDamage() {
        return damage;
    }

    public int getHp() {
        return hp;
    }

    public int getMoney() {
        return money;
    }

    public Color getColor() {
        return color;
    }

}
