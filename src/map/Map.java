package map;

import blocks.Block;
import blocks.Nexus;
import blocks.Node;
import blocks.Spawnpoint;
import blocks.towers.Tower;
import entities.Monster;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import towerdefense.Main;

import java.util.*;

public class Map {
    private int x;
    private int y;
    private Nexus nexus;
    private Node[][] map_matrix;
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
        this.map_matrix = new Node[rectCountx][rectCounty];
        this.map = new int[rectCounty][rectCountx];
        this.minDisdanceBetweenSpawns = Main.spawnSpacing;
        this.spawnCount = Main.spawnCount;
        this.allMonsters = new HashSet<>();
    }

    public void initMap() {
        for (int x = 0; x < this.x; x++) {
            for (int y = 0; y < this.y; y++) {
                Node node;
                if (x == 0 || y == 0 || x == this.x - 1 || y == this.y - 1) {
                    //Value on by default id-ga võrdne.
                    node = new Node(x, y, 1);
                    node.setColor(Color.BLACK);
                } else {
                    int rand = new Random().nextInt(2);
                    node = new Node(x, y, rand);
                    if (new Random().nextInt(40) == 1) {
                        node.setValue(4);
                    }
                }
                map_matrix[x][y] = node;
            }
        }
        for (Node[] veerg : map_matrix) {
            for (Node node : veerg) {
                node.setNaabrid(getNeighbours(node));
            }
        }
    }

    public void genMap(int a) {
        for (int kordus = 0; kordus < a; kordus++) {
            for (int x = 1; x < this.x - 1; x++) {
                for (int y = 1; y < this.y - 1; y++) {
                    int block_value_sum = 0;
                    for (int xMuut = -1; xMuut < 2; xMuut++) {
                        for (int yMuut = -1; yMuut < 2; yMuut++) {
                            block_value_sum += map_matrix[x + xMuut][y + yMuut].getValue();
                        }
                    }
                    Node node = map_matrix[x][y];
                    if (block_value_sum > 5) {
                        node.setId(1);
                        node.setValue(1);
                        node.setColor(Color.BLACK);
                    } else if (block_value_sum < 5) {
                        node.setId(0);
                        node.setValue(0);
                        node.setColor(Color.WHITE);
                    } else {
                        if (node.id == 0) {
                            node.setColor(Color.WHITE);
                        } else if (node.id == 1) {
                            node.setColor(Color.BLACK);
                        }
                    }
                }
            }
        }
    }

    private HashSet<Node> getNeighbours(Node node) {
        int currentX  = node.indexX;
        int currentY = node.indexY;
        HashSet<Node> neighbours = new HashSet<>();
        for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
                int newX = currentX + x;
                int newY = currentY + y;
                boolean isInBorders = newX >= 0 && newX < map_matrix.length && newY >= 0 && newY < map_matrix[0].length;
                if (isInBorders) {
                    Node neighbour = map_matrix[newX][newY];
                    neighbours.add(neighbour);
                }
            }
        }
        return neighbours;
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
        for (int x = 0; x < this.x; x++) {
            for (int y = 0; y < this.y; y++) {
                gc.setFill(map_matrix[x][y].getColor());
                gc.fillRect(x * this.size, y * this.size, this.size, this.size);
            }
        }
    }

    public void sellTower(Tower tower) {
        tower.sell();
        towers.remove(tower);
        editMap_matrix(tower.indexX, tower.indexY, new Block(tower.indexX, tower.indexY, 0, 1, Color.WHITE, 0));
    }

    public Node[][] getMap_matrix() {
        return this.map_matrix;
    }

    public void editMap_matrix(int x, int y, Block newblock) {
        Node node = this.map_matrix[x][y];
        node.copyBlock(newblock);
        this.map[y][x] = newblock.getId();
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

    public Nexus getNexus() {
        return nexus;
    }

    public void setNexus(Nexus nexus) {
        this.nexus = nexus;
    }

    public List<Spawnpoint> getSpawnpoints() {
        return spawnpoints;
    }

    public void drawPath(int[][] path) {
        if (path.length > 0) {
            for (int i = 0; i < path.length - 1; i++) {
                int indexX = path[i][0];
                int indexY = path[i][1];
                int pathCount = getNode(indexX, indexY).getPathCount();

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
                int pathCount = getNode(indexX, indexY).getPathCount();

                Block newBlock = new Block(indexX, indexY, 9, 5, Color.LIGHTGRAY, pathCount - 1);
                if (newBlock.getPathCount() == 0) {
                    newBlock.setId(0);
                    newBlock.setColor(Color.WHITE);
                }
                if (getNode(indexX, indexY).getId() == 9) editMap_matrix(indexX, indexY, newBlock);
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

    public void colorAllBlocks(Color color, HashSet<Node> nodes) {
        if (nodes != null) {
            int size = Main.blockSize;
            gc.setFill(color);
            for (Node node : nodes) {
                gc.fillRect(node.indexX * size, node.indexY * size, size, size);
            }
        }
    }

    public void drawGrid(double lineWidth, Color color) {
        gc.setLineWidth(lineWidth);
        gc.setStroke(color);
        int size = Main.blockSize;
        for (int x = 0; x < this.x; x++) {
            gc.strokeLine(x * size, 0, x * size, Main.screenH);
        }
        for (int y = 0; y < this.y; y++) {
            gc.strokeLine(0, y * size, Main.screenW, y * size);
        }
        gc.stroke();
    }

    public Node getNode(int x, int y) {
        return map_matrix[x][y];
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
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

    public void setWall(int indexX, int indexY) {
        this.map_matrix[indexX][indexY].setWall();
    }
}