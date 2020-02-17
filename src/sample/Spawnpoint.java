package sample;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

class Spawnpoint {

   private List<Monster> monsters;
   private int[] spawnpointxy;
   private int[] nexusxy;
   private int[][] path;
   private int[][] map;
   private List<Block> towers;


   Spawnpoint(int[] spawnpointxy, int[][] map) {
       towers = Main.map.getTowers();
       monsters = new ArrayList<>();
       this.spawnpointxy = spawnpointxy;
       monsters.add(new Monster(0,this.spawnpointxy[0]*Main.map.getSize(),this.spawnpointxy[1]*Main.map.getSize()));
       this.map = map;
   }

    public int[][] getPath() {
        return path;
    }

    public int[] getSpawnpointxy() {
        return spawnpointxy;
    }

    void moveMonsters(){
        monsters.add(new Monster(0,this.spawnpointxy[0]*Main.map.getSize(),this.spawnpointxy[1]*Main.map.getSize()));
        for (Monster monster:this.monsters) {
            if (monster.step<this.path.length){
                monster.move(this.path[monster.step][0],this.path[monster.step][1]);
            }else{
                monster.setHp(0);
            }
        }
    }

    void drawMonsters(){
        for (int i = getMonsters().size()-1; i >=0 ; i--) {
            if (getMonsters().get(i).getHp()<=0){
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
        for (Block tower:
                Main.map.getTowers()) {
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

}
