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
    public Block(int block_id,int block_value,Color block_color){
        setId(block_id);
        setColor(block_color);
        setValue(block_value);
    }
    public void makeTower(int tower_id,int pixelx, int pixely){
        setLevel(0);
        setId(tower_id);
        setX(pixelx);
        setY(pixely);
        if (tower_id == 10){
            setColor(new Color(1,0,0,1));
            setValue(1);
            setRange(10*Main.map.getSize());
            setActive(false);
        }
    }
    public void shoot(List<Monster> monsters){
        for (Monster monster:
             monsters) {
            int dist =(int) Math.round(Math.sqrt(Math.pow(getX()-monster.getX(),2)+Math.pow(getY()-monster.getY(),2)));
            if (dist<=getRange()){
                monster.setHp(monster.getHp()-getValue());
                Main.getGc().setStroke(getColor());
                Main.getGc().setLineWidth(2);
                Main.getGc().strokeLine(getX(),getY(),monster.getX(),monster.getY());
            }

        }
    }
    public void drawRange(){
        Main.getGc().setFill(new Color(0,0,0,0.5));
        Main.getGc().fillOval(getX()-getRange(),getY()- getRange(),getRange()*2,getRange()*2);
    }
    public Color getColor() {
        return color;
    }

    public int getId() {
        return id;
    }

    public int getValue() {
        return value;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getRange() {
        return range;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
