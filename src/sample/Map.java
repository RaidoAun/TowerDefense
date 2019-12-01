package sample;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Random;

public class Map {
    int x;
    int y;
    Block[][] map_matrix;
    Canvas canvas;
    int size;
    public Map(int rectCountx, int rectCounty, int blocksize){
        x = rectCountx;
        y = rectCounty;
        canvas = Main.getCanvas();
        size = blocksize;
        map_matrix = new Block[rectCountx][rectCounty];
        canvas.setWidth(size*rectCountx);
        canvas.setHeight(size*rectCounty);
        canvas.setOnMouseClicked(e -> {
            map_matrix[convertPixelToIndex((e.getX()))][convertPixelToIndex(e.getY())].makeTower(10);
            drawBlock(convertPixelToIndex((e.getX())),convertPixelToIndex(e.getY()));
        });
    }
    public int convertPixelToIndex (double pixel_coords){
        int index = (int)pixel_coords/size;
        return index;
    }
    public void initMap(){
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                if (i == 0 || j == 0 || i == x-1 || j == y-1){
                    map_matrix[i][j] = new Block(1,1,new Color(0,0,0,1));
                }else{
                    int rand = new Random().nextInt(2);
                    if (new Random().nextInt(40)==1){
                        map_matrix[i][j] = new Block(rand,4,new Color(1-rand,1-rand,1-rand,1));
                    }else {
                        map_matrix[i][j] = new Block(rand,rand,new Color(1-rand,1-rand,1-rand,1));
                    }
                }
            }
        }
    }
    public void genMap(){
        for (int i = 1; i < x-1; i++) {
            for (int j = 1; j < y-1; j++) {
                int block_value_sum = 0;
                for (int k = -1; k < 2; k++) {
                    for (int l = -1; l < 2; l++) {
                        block_value_sum+=map_matrix[i+k][j+l].getValue();
                    }
                }
                if (block_value_sum>5){
                    map_matrix[i][j].setId(1);
                    map_matrix[i][j].setValue(1);
                    map_matrix[i][j].setColor(new Color(0,0,0,1));
                }
                if (block_value_sum<5){
                    map_matrix[i][j].setId(0);
                    map_matrix[i][j].setValue(0);
                    map_matrix[i][j].setColor(new Color(1,1,1,1));
                }
            }
        }
    }

    public void drawMap(){
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                Main.gc.setFill(map_matrix[i][j].getColor());
                Main.getGc().fillRect(i*size,j*size,size,size);
            }
        }
    }

    public Block[][] getMap_matrix() {
        return map_matrix;
    }

    public void editMap_matrix(int i,int j,Block newblock) {
        map_matrix[i][j] = newblock;
    }
    public void drawBlock(int i,int j){
        Main.getGc().setFill(map_matrix[i][j].getColor());
        Main.getGc().fillRect(i*size,j*size,size,size);
    }

}
