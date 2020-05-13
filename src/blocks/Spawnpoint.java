package blocks;

import entities.Monster;
import entities.Monsters;
import javafx.scene.paint.Color;
import map.Map;
import map.Pathfinder;
import states.GameState;

import java.util.*;

public class Spawnpoint extends Block {

    private static Random r = new Random();
    private boolean nexusWithPath;
    private int[] nexusxy;
    private int[][] path;
    private Map map;
    private int spawnRate; //Mitme frame tagant spawnib uus koletis.
    private int bosstimer;
    private int startidletime;

    public Spawnpoint(int x, int y, Map map) {
        super(x, y, 2, 2, Color.LIGHTGREEN, 0);
        nexusWithPath = false;
        this.map = map;
        this.spawnRate = 30;
        this.bosstimer = 100;
        this.startidletime = 1000;
    }

    public void tick(int tick, int level) {
        if (startidletime <= 0) {
            if (tick % spawnRate == 0) {
                List<Monsters> monsters = new ArrayList<>(Arrays.asList(Monsters.values()));
                monsters.remove(Monsters.BOSS);
                Monster uusKoletis = new Monster(monsters.get(r.nextInt(monsters.size())), pixelX, pixelY, this);
                if (this.bosstimer == 0) {//võiks teha siin boss spawni
                    Monster boss = new Monster(Monsters.BOSS, pixelX, pixelY, this);
                    boss.setHp(boss.getHp() * (10 + level) / 10);
                    map.addMonster(boss);
                    this.bosstimer = 100;
                } else {
                    uusKoletis.setHp(uusKoletis.getHp() * (10 + level) / 10);
                    this.bosstimer--;
                }
                map.addMonster(uusKoletis);
            }
        } else {
            startidletime--;
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
