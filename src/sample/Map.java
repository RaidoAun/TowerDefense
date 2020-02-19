package sample;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.PathElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

class Map {
    private List<int[]> openBlocks;
    private int x;
    private int y;
    private Block[][] map_matrix;
    private List<Spawnpoint> spawnpoints;
    private Canvas canvas;
    private double size;
    private List<Block> towers;
    private int[][] map;

    Map(int rectCountx, int rectCounty,Canvas map_canvas){
        this.towers = new ArrayList<>();
        this.x = rectCountx;
        this.y = rectCounty;
        this.canvas = map_canvas;
        this.spawnpoints = new ArrayList<>();
        this.map_matrix = new Block[rectCountx][rectCounty];
        this.map = new int[rectCounty][rectCountx];
        this.openBlocks = new ArrayList<>();
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

    void genMap(int a){
        for (int w = 0; w < a; w++) {
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
    }

    void genFlippedMap() {
        int[][] flippedMap = new int[map_matrix[0].length][map_matrix.length];
        for (int i = 0; i < map_matrix.length; i++) {
            for (int j = 0; j < map_matrix[0].length; j++) {
                int id = map_matrix[i][j].getId();
                flippedMap[j][i] = id;
            }
        }
        this.map = flippedMap;
    }

    void drawMap(double blocksize){
        this.size = blocksize;
        this.canvas.setWidth(size*this.x);
        this.canvas.setHeight(size*this.y);
        //editMap_matrix(100, 50, new Block(3, 0, new Color(1, 0, 0, 1))); //0 - vaba; 1 - sein; 3 - nexus; 2 - start
        GraphicsContext gc = canvas.getGraphicsContext2D();
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                gc.setFill(map_matrix[i][j].getColor());
                //System.out.print(map_matrix[i][j].getValue()+" ");
                gc.fillRect(i*this.size,j*this.size, this.size, this.size);
            }
            //System.out.println();
        }
    }

    Block[][] getMap_matrix() {
        return this.map_matrix;
    }

    void editMap_matrix(int i, int j, Block newblock) {
        this.map_matrix[i][j] = newblock;
        this.map[j][i] = newblock.getId();
    }

    void drawBlock(int i, int j){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(map_matrix[i][j].getColor());
        gc.fillRect(i*size,j*size,size,size);
    }

    double getSize() {
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

    List<Spawnpoint> getSpawnpoints() {
        return spawnpoints;
    }

    void drawPath(int[][] path) {
        if (path.length > 0) {
            for (int i = 0; i < path.length - 1; i++) {
                //Path bloki id on 9.
                editMap_matrix(path[i][0], path[i][1], new Block(9, 5, new Color(0.8, 0.76, 0.72, 0.9)));
            }
        }
    }

    void deletePath(int[][] path) {
        if (path.length > 0) {
            for (int i = 0; i < path.length - 1; i++) {
                if (map_matrix[path[i][0]][path[i][1]].getId() == 9) {
                    editMap_matrix(path[i][0], path[i][1], new Block(0, 5, new Color(1, 1, 1, 1)));
                }
            }
        }
    }

    void generateSpawnpoints(int count, int minDistance) {
        double optimaalne = (this.x * this.y) / (Math.pow((double) minDistance/2, 2) * Math.PI);
        if (count > optimaalne) {
            System.out.println("Praeguste parameetritega on võimalik luua maksimum " + (int) optimaalne + " spawnpointi!");
        }
        System.out.println("Alustan spawnpointide genereerimist...");
        List<int[]> openBlocksajutine;
        List<int[]> spawns = new ArrayList<>();
        Random r = new Random();
        //Esimese spawnpoindi loomine.
        openBlocksajutine = new ArrayList<>(this.openBlocks);
        int index = r.nextInt(openBlocksajutine.size());
        int[] point = openBlocksajutine.get(index);
        openBlocksajutine.remove(index);
        spawns.add(point);
        //Forloop, mis tagab, et spawnpoindid oleks üksteisest distance kaugusel.
        for (int j = 0; j < openBlocksajutine.size(); j++) {
            int c = (int) Math.hypot(point[0] - openBlocksajutine.get(j)[0], point[1] - openBlocksajutine.get(j)[1]);
            if (c <= minDistance) {
                openBlocksajutine.remove(j);
                j -= 1;
            }
        }
        //Ülejäänud spawnpointide loomine.
        for (int i = 0; i < count - 1; i++) {
            //Kui openBlocksajutine on tühi, valib programm uue esimese spawnpoindi.
            if (openBlocksajutine.size() == 0) {
                break;
            }
            int index2 = r.nextInt(openBlocksajutine.size());
            int[] point2 = openBlocksajutine.get(index2);
            Pathfinder p = new Pathfinder(this.map, point, point2, 1);
            if (p.getFinalPath().length > 0) {
                spawns.add(point2);
                //Forloop, mis tagab, et spawnpoindid oleks üksteisest distance kaugusel.
                for (int j = 0; j < openBlocksajutine.size(); j++) {
                    int c = (int) Math.hypot(point2[0] - openBlocksajutine.get(j)[0], point2[1] - openBlocksajutine.get(j)[1]);
                    if (c <= minDistance) {
                        openBlocksajutine.remove(j);
                        j -= 1;
                    }
                }
            } else {
                i -= 1;
                openBlocksajutine.remove(index2);
            }
        }
        //Spawnpointide kirjutamine klassi.
        for (int[] spawn : spawns) {
            //x - 100 y - 50 on nexus hetkel!
            this.spawnpoints.add(new Spawnpoint(spawn, this.map));
        }
        System.out.println(spawns.size() + " spawnpoindi genereermine õnnestus!");
    }

    void spawnSpawnpoints() {
        for (Spawnpoint p : spawnpoints) {
            editMap_matrix(p.getSpawnpointxy()[0], p.getSpawnpointxy()[1], new Block(2, 5, new Color(0.5, 1, 0, 0.9)));
        }
    }

    List<Block> getTowers() {
        return towers;
    }

    void setNexusxy(int[] nexusxy) {
        for (Spawnpoint spawn : this.spawnpoints) {
            spawn.setNexusxy(nexusxy);
        }
    }

    void genNexus() {
        int[] n = this.spawnpoints.get(0).getNexusxy();
        editMap_matrix(n[0], n[1], new Block(3, 0, new Color(1, 0, 1, 1)));
    }

    void genPathstoNexus(int gCost) {
        //Random r = new Random();
        //int randomGCost;
        for (Spawnpoint spawn : this.spawnpoints) {
            //randomGCost = (r.nextInt(gCost * 2) + 1 - 500) + gCost;
            //System.out.println(randomGCost);
            spawn.genPath(gCost);
        }
    }

    List<Spawnpoint> pathsContain(int[] point) {
        List<Spawnpoint> needChange = new ArrayList<>();
        for (Spawnpoint spawn : spawnpoints) {
            for (int[] p : spawn.getPath()) {
                if (Arrays.equals(p, point)) {
                    needChange.add(spawn);
                }
            }
        }
        return needChange;
    }

    Block getBlock(int x, int y) {
        return map_matrix[x][y];
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
