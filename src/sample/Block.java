package sample;

import javafx.scene.paint.Color;

import java.util.List;

public class Block {
    int id;
    Color color;
    int value;

    Block(int block_id, int block_value, Color block_color){
        //0 - vaba; 1 - sein; 3 - nexus; 2 - start; 10 - tower; 9 - path;
        this.id = block_id;
        this.color = block_color;
        this.value = block_value;
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

}
