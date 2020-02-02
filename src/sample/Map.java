package sample;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Random;

public class Map {
    private int x;
    private int y;
    private Block[][] map_matrix;
    private Canvas canvas;
    private int size;
    Map(int rectCountx, int rectCounty, int blocksize, Canvas map_canvas){
        x = rectCountx;
        y = rectCounty;
        canvas = map_canvas;
        size = blocksize;
        map_matrix = new Block[rectCountx][rectCounty];
        canvas.setWidth(size*rectCountx);
        canvas.setHeight(size*rectCounty);
    }


    void initMap(){
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

    void genMap(){
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

    void drawMap(){
        editMap_matrix(100, 20, new Block(3, 0, new Color(1, 0, 0, 1)));
        editMap_matrix(10, 5, new Block(2, 0, new Color(0, 0, 1, 1))); //0 - vaba; 1 - sein; 3 - nexus; 2 - start
        GraphicsContext gc = canvas.getGraphicsContext2D();
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                gc.setFill(map_matrix[i][j].getColor());
                //System.out.print(map_matrix[i][j].getValue()+" ");
                gc.fillRect(i*size,j*size,size,size);
            }
            //System.out.println();
        }
    }

    public Block[][] getMap_matrix() {
        return map_matrix;
    }

    public int[][] getFlippedMap() {
        return flipMap();
    }

    void editMap_matrix(int i, int j, Block newblock) {
        this.map_matrix[i][j] = newblock;
    }

    void drawBlock(int i, int j){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(map_matrix[i][j].getColor());
        gc.fillRect(i*size,j*size,size,size);
    }

    public int[][] numbriMatrix(Block[][] m) {
        int[][] matrix = new int[m.length][m[0].length];
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                matrix[i][j] = m[i][j].getId();
            }
        }
        return matrix;
    }

    private int[][] flipMap () {
        int[][] flippedMap = new int[map_matrix[0].length][map_matrix.length];
        for (int i = 0; i < map_matrix.length; i++) {
            for (int j = 0; j < map_matrix[0].length; j++) {
                flippedMap[j][i] = map_matrix[i][j].getId();
            }
        }
        return flippedMap;
    }

}
