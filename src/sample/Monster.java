package sample;

import javafx.scene.paint.Color;

public class Monster {
    int hp;
    int dmg;
    int id;
    int x;
    int y;
    int step;
    int speed;
    Color color;
    Monster(int type, int x_coord, int y_coord){
        this.x = x_coord;
        this.y = y_coord;
        this.id = type;
        this.step = 0;
        if (id==0){
            this.hp = 1000;
            this.dmg = 5;
            this.speed = 1;
            this.color = new Color(1,1,0,1);
        }
    }
     public void drawMonster(){
        Main.getGc().setFill(color);
        int radius = 10;
        Main.getGc().fillRect(this.x, this.y, Main.map.getSize(),Main.map.getSize());
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

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    void move(int x, int y){
        int pixelx=(x*Main.map.getSize())-this.x;
        int pixely=(y*Main.map.getSize())-this.y;
        if (pixelx==0&&pixely==0){
            this.step+=1;
        }
        if (this.speed>=x&&x!=0){
            setX(pixelx+this.x);
            this.step+=1;
        }else if (this.speed>=y&&y!=0){
            setY(pixely+this.y);
            this.step+=1;
        }
        if (pixelx<0){
            setX(this.x-this.speed);
        }
        else if (pixelx > 0){
            setX(this.x+this.speed);
        }
        else if (pixely < 0){
            setY(this.y-this.speed);
        }
        else if (pixely>0){
            setY(this.y+this.speed);
        }
    }
}