package sample;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

class Spawnpoint {

   private List<Monster> monsters;
   private int[] spawnpointxy;
   private int[] nexusxy;
   private int[][] path;
   private List<Block> towers;


   Spawnpoint(int[] spawnpointxy, int[] nexusxy, int[][] map) {
       towers = Main.map.getTowers();
       monsters = new ArrayList<>();
       this.spawnpointxy = spawnpointxy;
       this.nexusxy = nexusxy;
       Pathfinder tee = new Pathfinder(map, spawnpointxy, nexusxy);
       this.path = tee.getFinalPath();
       monsters.add(new Monster(0,100,100));
   }

    public int[][] getPath() {
        return path;
    }

    public int[] getSpawnpointxy() {
        return spawnpointxy;
    }

    void moveMonsters(){
        for (Monster monster:this.monsters) {
            monster.move(this.path[monster.step][0],this.path[monster.step][1]);
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

}
