import javafx.scene.paint.Color;

class Spawnpoint extends Block {

    private int[][] path;

    Spawnpoint(int x, int y) {
        super(x, y, 2, 5, Color.LIGHTGREEN, 0);
    }

    int[][] getPath() {
        return path;
    }

    void setPath(int[][] path) {
        this.path = path;
    }

    public void genMonster(Monsters type) {
        double x = (this.x + 0.5) * Game.getBlockSize();
        double y = (this.y + 0.5) * Game.getBlockSize();
        Monster monster = new Monster(type, x, y, this);
        Game.getMap().addMonster(monster);
    }

}
