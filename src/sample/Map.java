package sample;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Map {
    private List<int[]> openBlocks;
    private int x;
    private int y;
    private Block[][] map_matrix;
    private Spawnpoint[] spawnpoints;
    private Canvas canvas;
    private int size;
    private List<Block> towers;

    Map(int rectCountx, int rectCounty, int blocksize, Canvas map_canvas){
        towers = new ArrayList<>();
        x = rectCountx;
        y = rectCounty;
        canvas = map_canvas;
        size = blocksize;
        spawnpoints = new Spawnpoint[3];
        map_matrix = new Block[rectCountx][rectCounty];
        openBlocks = new ArrayList<>();
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
        editMap_matrix(100, 50, new Block(3, 0, new Color(1, 0, 0, 1))); //0 - vaba; 1 - sein; 3 - nexus; 2 - start
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

    Block[][] getMap_matrix() {
        return map_matrix;
    }

    int[][] getFlippedMap() {
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

    private int[][] flipMap () {
        int[][] flippedMap = new int[map_matrix[0].length][map_matrix.length];
        for (int i = 0; i < map_matrix.length; i++) {
            for (int j = 0; j < map_matrix[0].length; j++) {
                int id = map_matrix[i][j].getId();
                flippedMap[j][i] = id;
            }
        }
        return flippedMap;
    }

    int getSize() {
        return size;
    }

    void genOpenBlocks() {
        List<int[]> open = new ArrayList<>();
        for (int i = 0; i < map_matrix.length; i++) {
            for (int j = 0; j < map_matrix[0].length; j++) {
                if (map_matrix[i][j].getId() == 0) {
                    open.add(new int[]{i, j});
                }
            }
        }
        this.openBlocks = open;
    }

    Spawnpoint[] getSpawnpoints() {
        return spawnpoints;
    }

    void drawPath(int[][] path) {
        if (path.length > 0) {
            for (int[] p : path) {
                editMap_matrix(p[0], p[1], new Block(0, 5, new Color(0, 1, 1, 1)));
            }
        }
    }

    void generateSpawnpoints() {
        List<Integer> spawnIndexes = new ArrayList<>();
        List<int[]> spawns = new ArrayList<>();
        Random r = new Random();
        while (spawnIndexes.size() < 3) {
            int index = r.nextInt(openBlocks.size());
            if (!spawnIndexes.contains(index)) {
                spawnIndexes.add(index);
                int[] point = openBlocks.get(index);
                spawns.add(point);
                map_matrix[point[0]][point[1]].setId(1);
            }
        }
        for (int i = 0; i < 3; i++) {
            this.spawnpoints[i] = new Spawnpoint(spawns.get(i), new int[]{100, 50}, getFlippedMap()); //x - 50 y - 10 on nexus hetkel!
        }
    }

    void spawnSpawnpoints() {
        for (Spawnpoint p : spawnpoints) {
            editMap_matrix(p.getSpawnpointxy()[0], p.getSpawnpointxy()[1], new Block(6, 5, new Color(0.5, 1, 0.5, 0.5)));
        }
    }

    List<Block> getTowers() {
        return towers;
    }

}
