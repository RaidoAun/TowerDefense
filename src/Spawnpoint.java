class Spawnpoint {

    private boolean nexusWithPath;
    private int[] spawnpointxy;
    private int[] nexusxy;
    private int[][] path;
    private int[][] map;

    Spawnpoint(int[] spawnpointxy, int[][] map) {
        nexusWithPath = false;
        this.spawnpointxy = spawnpointxy;
        this.map = map;
    }

    int[][] getPath() {
        return path;
    }

    void setPath(int[][] path) {
        this.path = path;
    }

    int[] getSpawnpointxy() {
        return spawnpointxy;
    }

    int[] getNexusxy() {
        return nexusxy;
    }

    void setNexusxy(int[] xy) {
        this.nexusxy = xy;
    }

    public void genMonster(Monsters type) {
        double x = (this.spawnpointxy[0] + 0.5) * Main.getMap().getSize();
        double y = (this.spawnpointxy[1] + 0.5) * Main.getMap().getSize();
        Monster monster = new Monster(type, x, y, this);
        Main.getMap().addMonster(monster);
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
