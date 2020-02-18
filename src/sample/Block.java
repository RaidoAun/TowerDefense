package sample;

import javafx.scene.paint.Color;

import java.util.List;

public class Block {
    private int id;
    private Color color;
    private int value;
    private int level;
    private int range;
    private int x;
    private int y;
    private boolean active;
    private boolean wall;

    Block(int block_id, int block_value, Color block_color){
        //0 - vaba; 1 - sein; 3 - nexus; 2 - start; 10 - tower; 9 - path;
        setId(block_id);
        setColor(block_color);
        setValue(block_value);
        if (getId() == 1) {setWall(true);}
    }

    void makeTower(int tower_id, int pixelx, int pixely){
        this.level = 0;
        this.id = tower_id;
        this.x = pixelx;
        this.y = pixely;
        if (tower_id == 10){
            this.color = new Color(1,0,0,1);
            this.value = 0;
            this.range = 10*Main.map.getSize();
            this.active = false;
        }
    }

    public boolean isWall() {
        return wall;
    }
    void shoot(List<Monster> monsters){
        for (Monster monster:
             monsters) {
            int dist =(int) Math.round(Math.sqrt(Math.pow(getX()-monster.getX(),2)+Math.pow(getY()-monster.getY(),2)));
            if (dist<=getRange()){
                monster.setHp(monster.getHp()-getValue());
                Main.getGc().setStroke(getColor());
                Main.getGc().setLineWidth(0.5);
                Main.getGc().strokeLine(getX(),getY(),monster.getX(),monster.getY());
            }

        }
    }
    void drawRange(){
        Main.getGc().setFill(new Color(0,0,0,0.5));
        Main.getGc().fillOval(getX()-getRange(),getY()- getRange(),getRange()*2,getRange()*2);
    }

    private void setWall(boolean wall) {
        this.wall = wall;
    }

    Color getColor() {
        return color;
    }

    int getId() {
        return id;
    }

    int getValue() {
        return value;
    }

    void setColor(Color color) {
        this.color = color;
    }

    void setId(int id) {
        this.id = id;
    }

    void setValue(int value) {
        this.value = value;
    }

    public int getLevel() {
        return level;
    }

    private void setLevel(int level) {
        this.level = level;
    }

    private void setRange(int range) {
        this.range = range;
    }

    private void setX(int x) {
        this.x = x;
    }

    private void setY(int y) {
        this.y = y;
    }

    private int getRange() {
        return range;
    }

    private int getX() {
        return x;
    }

    private int getY() {
        return y;
    }

    boolean getActive() {
        return active;
    }

    void setActive(boolean active) {
        this.active = active;
    }
}
