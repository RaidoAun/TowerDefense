package map;

import blocks.Block;
import blocks.Spawnpoint;
import blocks.towers.Tower;
import entities.Entity;
import entities.Monster;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import towerdefense.Main;

import java.util.*;

public class Map {
    private int x;
    private int y;
    private Block nexus;
    private Block[][] map_matrix;
    private List<Spawnpoint> spawnpoints;
    private GraphicsContext gc;
    private int size;
    private List<Tower> towers;
    private int[][] map;
    private int minDisdanceBetweenSpawns;
    private int spawnCount;
    private HashSet<Monster> allMonsters;

    public Map(int rectCountx, int rectCounty, GraphicsContext gc) {
        this.towers = new ArrayList<>();
        this.x = rectCountx;
        this.y = rectCounty;
        this.gc = gc;
        this.spawnpoints = new ArrayList<>();
        this.map_matrix = new Block[rectCountx][rectCounty];
        this.map = new int[rectCounty][rectCountx];
        this.minDisdanceBetweenSpawns = Main.spawnSpacing;
        this.spawnCount = Main.spawnCount;
        this.allMonsters = new HashSet<>();
    }

    public void initMap() {
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                if (i == 0 || j == 0 || i == x - 1 || j == y - 1) {
                    map_matrix[i][j] = new Block(i, j, 1, 1, new Color(0, 0, 0, 1), 0);
                } else {
                    int rand = new Random().nextInt(2);
                    if (new Random().nextInt(40) == 1) {
                        map_matrix[i][j] = new Block(i, j, rand, 4, new Color(1 - rand, 1 - rand, 1 - rand, 1), 0);
                    } else {
                        map_matrix[i][j] = new Block(i, j, rand, rand, new Color(1 - rand, 1 - rand, 1 - rand, 1), 0);
                    }
                }

            }
        }
    }

    public void genMap(int a) {
        for (int w = 0; w < a; w++) {
            for (int i = 1; i < x - 1; i++) {
                for (int j = 1; j < y - 1; j++) {
                    int block_value_sum = 0;
                    for (int k = -1; k < 2; k++) {
                        for (int l = -1; l < 2; l++) {
                            block_value_sum += map_matrix[i + k][j + l].getValue();
                        }
                    }
                    if (block_value_sum > 5) {
                        map_matrix[i][j].setId(1);
                        map_matrix[i][j].setValue(1);
                        map_matrix[i][j].setColor(new Color(0, 0, 0, 1));
                    }
                    if (block_value_sum < 5) {
                        map_matrix[i][j].setId(0);
                        map_matrix[i][j].setValue(0);
                        map_matrix[i][j].setColor(new Color(1, 1, 1, 1));
                    }
                }
            }
        }
    }

    public void genFlippedMap() {
        int[][] flippedMap = new int[map_matrix[0].length][map_matrix.length];
        for (int i = 0; i < map_matrix.length; i++) {
            for (int j = 0; j < map_matrix[0].length; j++) {
                int id = map_matrix[i][j].getId();
                flippedMap[j][i] = id;
            }
        }
        this.map = flippedMap;
    }

    public int[][] getMap() {
        return map;
    }

    public void drawMap(int blocksize) {
        this.size = blocksize;

        gc.setLineWidth(0.1);
        gc.setStroke(Color.BLACK);
        for (int i = 0; i < this.x; i++) {
            for (int j = 0; j < this.y; j++) {
                gc.setFill(map_matrix[i][j].getColor());
                //System.out.print(map_matrix[i][j].getValue()+" ");
                gc.fillRect(i * this.size, j * this.size, this.size, this.size);
            }
            gc.strokeLine(i * this.size, 0, i * this.size, Main.screenH);
            //System.out.println();
        }
        for (int i = 0; i < this.y; i++) {
            gc.strokeLine(0, i * this.size, Main.screenW, i * this.size);
        }
        gc.stroke();
    }

    public void sellTower(Tower tower) {
        towers.remove(tower);
        editMap_matrix(tower.indexX, tower.indexY, new Block(tower.indexX, tower.indexY, 0, 1, Color.WHITE, 0));
    }

    public Block[][] getMap_matrix() {
        return this.map_matrix;
    }

    public void editMap_matrix(int i, int j, Block newblock) {
        this.map_matrix[i][j] = newblock;
        this.map[j][i] = newblock.getId();
    }

    private List<int[]> genOpenBlocks() {
        List<int[]> open = new ArrayList<>();
        for (int i = 0; i < map_matrix.length; i++) {
            for (int j = 0; j < map_matrix[0].length; j++) {
                if (map_matrix[i][j].getId() == 0) {
                    open.add(new int[]{i, j});
                }
            }
        }
        return open;
    }

    public List<Spawnpoint> getSpawnpoints() {
        return spawnpoints;
    }

    public void drawPath(int[][] path) {
        if (path.length > 0) {
            for (int i = 0; i < path.length - 1; i++) {
                int indexX = path[i][0];
                int indexY = path[i][1];
                int pathCount = getBlock(indexX, indexY).getPathCount();

                Block newBlock = new Block(indexX, indexY, 9, 5, Color.LIGHTGRAY, pathCount + 1);
                editMap_matrix(indexX, indexY, newBlock);
            }
        }
    }

    public void deletePath(int[][] path) {
        if (path.length > 0) {
            for (int i = 0; i < path.length - 1; i++) {
                int indexX = path[i][0];
                int indexY = path[i][1];
                int pathCount = getBlock(indexX, indexY).getPathCount();

                Block newBlock = new Block(indexX, indexY, 9, 5, Color.LIGHTGRAY, pathCount - 1);
                if (newBlock.getPathCount() == 0) {
                    newBlock.setId(0);
                    newBlock.setColor(Color.WHITE);
                }
                if (getBlock(indexX, indexY).getId() == 9) editMap_matrix(indexX, indexY, newBlock);
            }
        }
    }

    public void generateSpawnpoints() {

        int minDistance = this.minDisdanceBetweenSpawns;
        int count = this.spawnCount;

        double optimaalne = (this.x * this.y) / (Math.pow((double) minDistance / 2, 2) * Math.PI);
        if (count > optimaalne) {
            System.out.println("Praeguste parameetritega on võimalik luua maksimum " + (int) optimaalne + " spawnpointi!");
        }
        List<int[]> openBlocks = genOpenBlocks();
        List<int[]> openBlocksajutine = new ArrayList<>(openBlocks);
        List<int[]> spawns = new ArrayList<>();
        Random r = new Random();
        //Esimese spawnpoindi loomine.
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
            this.spawnpoints.add(new Spawnpoint(spawn[0], spawn[1], this));
        }
        //System.out.println(spawns.size() + " spawnpoindi genereermine õnnestus!");
    }

    public void spawnSpawnpoints() {
        for (Spawnpoint p : spawnpoints) {
            int xx = p.indexX;
            int yy = p.indexY;
            editMap_matrix(xx, yy, new Block(xx, yy, 2, 5, new Color(0.5, 1, 0, 0.9), 0));
        }
    }

    public List<Tower> getTowers() {
        return towers;
    }

    public void setNexusxy(int[] nexusxy) {
        for (Spawnpoint spawn : this.spawnpoints) {
            spawn.setNexusxy(nexusxy);
        }
    }

    public void genNexus() {
        int[] n = this.spawnpoints.get(0).getNexusxy();
        editMap_matrix(n[0], n[1], new Block(n[0], n[1], 3, 0, new Color(1, 0, 1, 1), 0));
    }

    public void genPathstoNexus(int gCost) {
        for (Spawnpoint spawn : this.spawnpoints) {
            spawn.genPath(gCost);
            spawn.setNexusWithPath(true);
        }
    }

    public List<Spawnpoint> pathsContain(int[] point) {
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

    public Block getBlock(int x, int y) {
        return map_matrix[x][y];
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.map = new int[this.y][x];
        this.map_matrix = new Block[x][this.y];
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.map = new int[y][this.x];
        this.map_matrix = new Block[this.x][y];
        this.y = y;
    }

    public Tower getTowerWithXY(int x, int y) {
        for (Tower tower : towers) {
            if (tower.indexX == x && tower.indexY == y) return tower;
        }
        return null;
    }

    public void addMonster(Monster monster) {
        this.allMonsters.add(monster);
    }

    public void noTowerRanges() {
        for (Tower tower : this.towers) tower.setActive(false);
    }

    public HashSet<Monster> getAllMonsters() {
        return allMonsters;
    }
}