package blocks;

import entities.Monster;
import entities.Monsters;
import javafx.scene.paint.Color;
import map.Map;
import map.Pathfinder;

import java.util.Random;

public class Spawnpoint extends Block {

    private static Random r = new Random();
    private boolean nexusWithPath;
    private int[] nexusxy;
    private int[][] path;
    private Map map;
    private int spawnRate; //Mitme frame tagant spawnib uus koletis.
    private int idleTime; //Aeg framedes, kui kaua spawnpoint pole koletist spawninud.

    public Spawnpoint(int x, int y, Map map) {
        super(x, y, 2, 2, Color.LIGHTGREEN, 0);
        nexusWithPath = false;
        this.map = map;
        this.spawnRate = 30;
        this.idleTime = spawnRate;
    }

    public void tick() {
        if (idleTime >= spawnRate) {
            Monsters[] monsters = Monsters.values();
            Monster uusKoletis = new Monster(monsters[r.nextInt(monsters.length)], pixelX, pixelY, this);
            map.addMonster(uusKoletis);
            idleTime = 0;
        } else {
            idleTime += 1;
        }
    }

    public int[][] getPath() {
        return path;
    }

    public void setPath(int[][] path) {
        this.path = path;
    }

    public int[] getNexusxy() {
        return nexusxy;
    }

    public void setNexusxy(int[] xy) {
        this.nexusxy = xy;
    }

    public void genPath(int gCost) {
        Pathfinder tee = new Pathfinder(map.getMap(), new int[]{indexX, indexY}, nexusxy, gCost);
        this.path = tee.getFinalPath();
    }

    public boolean isNexusWithPath() {
        return this.nexusWithPath;
    }

    public void setNexusWithPath(boolean nexusWithPath) {
        this.nexusWithPath = nexusWithPath;
    }

    //Mõeldud ühekordseks kasutuseks, ei muuda mingeid klassi muutujaid, tagastab vaid tee
    public int[][] genPathReturn(int gCost) {
        Pathfinder tee = new Pathfinder(map.getMap(), new int[]{indexX, indexY}, nexusxy, gCost);
        return tee.getFinalPath();
    }
}
