package sample;

import javafx.scene.paint.Color;

public class Monster {
    int hp;
    int dmg;
    int id;
    double x;
    double y;
    int step;
    double speed;
    Color color;
    Monster(int type, double x_coord, double y_coord){
        this.x = x_coord;
        this.y = y_coord;
        this.id = type;
        this.step = 0;
        if (id==0){
            this.hp = 1000;
            this.dmg = 5;
            this.speed = 10;
            this.color = new Color(1,1,0,1);
        }
    }
     public void drawMonster(){
        Main.getGc().setFill(this.color);
        int diameeter = Main.map.getSize()/2;
        Main.getGc().fillOval(this.x-diameeter/2, this.y-diameeter/2, diameeter, diameeter);
    }

    public int getHp() {
        return hp;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    void move(int[][] path){
        move(path,this.speed);
    }
    void move(int[][] path,double speed){
        if (this.step<path.length){
            int distx= (int) (((path[this.step][0]+0.5)*Main.map.getSize())-this.x);
            int disty= (int) (((path[this.step][1]+0.5)*Main.map.getSize())-this.y);
            if (distx<0){
                if (speed>=-distx){
                    this.x=distx+this.x;
                    this.step+=1;
                    move(path,speed+distx);
                }
                else{
                    this.x = this.x-speed;
                }
            }
            else if (distx > 0){
                if(speed>=distx){
                    this.x = distx+this.x;
                    this.step+=1;
                    move(path,speed-distx);
                }
                else{
                    this.x = this.x+speed;
                }
            }
            else if (disty < 0){
                if (speed>=-disty){
                    this.y = disty+this.y;
                    this.step+=1;
                    move(path,speed+disty);
                }
                else{
                    this.y = this.y-speed;
                }
            }
            else if (disty>0){
                if (speed>=disty){
                    this.y = disty+this.y;
                    this.step+=1;
                    move(path,speed-disty);
                }
                else{
                    this.y = this.y+speed;
                }
            }
            if (distx==0&&disty==0){
                this.step+=1;
            }
        }
        else{
            this.hp = 0;
        }
    }
}