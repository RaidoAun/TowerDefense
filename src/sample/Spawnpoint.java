package sample;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Spawnpoint {

   private List<Monster> koletised;
   private int[] spawnpointxy;
   private int[] nexusxy;
   private int[][] path;


   Spawnpoint(int[] spawnpointxy, int[] nexusxy, int[][] map) {
       koletised = new ArrayList<>();
       this.spawnpointxy = spawnpointxy;
       this.nexusxy = nexusxy;
       Pathfinder tee = new Pathfinder(map, spawnpointxy, nexusxy);
       this.path = tee.getFinalPath();
   }

    public int[][] getPath() {
        return path;
    }

    public int[] getSpawnpointxy() {
        return spawnpointxy;
    }
}
