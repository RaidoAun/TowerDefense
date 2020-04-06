package entities;

import javafx.scene.paint.Color;

public enum Monsters {

    TAVALINE(0, 3, 100, 10, 20, Color.YELLOW),
    KIIRE(1, 4, 50, 10, 30, Color.BLUE),
    TUGEV(2, 1, 200, 20, 40, Color.CRIMSON);

    private final int id;
    private final int kiirus;
    private final int hp;
    private final int damage;
    private final int money;
    private final Color color;

    Monsters(int id, int kiirus, int hp, int damage, int money, Color color) {
        this.id = id;
        this.kiirus = kiirus;
        this.hp = hp;
        this.damage = damage;
        this.money = money;
        this.color = color;
    }

    public int getKiirus() {
        return kiirus;
    }

    public int getDamage() {
        return damage;
    }

    public int getId() {
        return id;
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
