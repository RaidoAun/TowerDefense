package sample;

import javafx.scene.paint.Color;

public class Monster {
    int hp;
    int dmg;
    int id;
    int x;
    int y;
    Color color;
    Monster(int type, int x_coord, int y_coord){
        x = x_coord;
        y = y_coord;
        id = type;
        if (id==0){
            hp = 1000;
            dmg = 5;
            color = new Color(1,1,0,1);
        }
    }
     public void drawMonster(){
        Main.getGc().setFill(color);
        int radius = 10;
        Main.getGc().fillOval(x-radius, x-radius, radius*2, radius*2);
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}