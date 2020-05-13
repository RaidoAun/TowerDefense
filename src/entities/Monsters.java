package entities;

import javafx.scene.paint.Color;

public enum Monsters {

    TAVALINE(0.05, 1000, 5, 2, Color.YELLOW),
    KIIRE(0.08, 500, 5, 2, Color.BLUE),
    TUGEV(0.03, 3000, 5, 2, Color.CRIMSON),
    BOSS(0.05, 10000, 50, 2, Color.CADETBLUE);

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
