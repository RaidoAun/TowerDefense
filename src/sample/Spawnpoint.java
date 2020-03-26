package sample;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

class Spawnpoint {

    private boolean nexusWithPath;
    private List<Monster> monsters;
    private int[] spawnpointxy;
    private int[] nexusxy;
    private int[][] path;
    private int[][] map;
    private List<Block> towers;

    Spawnpoint(int[] spawnpointxy, int[][] map) {
        nexusWithPath = false;
        towers = Main.getMap().getTowers();
        monsters = new ArrayList<>();
        this.spawnpointxy = spawnpointxy;
        this.map = map;
    }

    void setPath(int[][] path) {
        this.path = path;
    }

    int[][] getPath() {
        return path;
    }

    int[] getSpawnpointxy() {
        return spawnpointxy;
    }

    int[] getNexusxy() {
        return nexusxy;
    }

    void moveMonsters(){
        for (Monster monster:this.monsters) {
            monster.move(this.path);
        }
    }

    public void genMonster(Monsters type) {
        double x = (this.spawnpointxy[0]+0.5)*Main.getMap().getSize();
        double y = (this.spawnpointxy[1]+0.5)*Main.getMap().getSize();
        monsters.add(new Monster(type, x, y));
    }

    void drawMonsters(){
        for (int i = getMonsters().size()-1; i >=0 ; i--) {
            if (getMonsters().get(i).getHp()<=0){
                Game.updateMoney(getMonsters().get(i).getMoney());
                monsters.remove(getMonsters().get(i));
            }
        }
        for (Monster monster:monsters) {
            monster.drawMonster();
        }
    }

    private List<Monster> getMonsters() {
        return monsters;
    }

    void shootTowers(){
        for (Block tower : Main.getMap().getTowers()) {
            tower.shoot(getMonsters());
        }
    }

    void setNexusxy(int[] xy) {
       this.nexusxy = xy;
    }

    void genPath(int gCost) {
        Pathfinder tee = new Pathfinder(map, spawnpointxy, nexusxy, gCost);
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
        Pathfinder tee = new Pathfinder(map, spawnpointxy, nexusxy, gCost);
        return tee.getFinalPath();
    }

}
