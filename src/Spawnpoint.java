import javafx.scene.paint.Color;

class Spawnpoint extends Block {

    private int[][] path;

    Spawnpoint(int x, int y, int size) {
        super(x, y, 2, 5, Color.LIGHTGREEN, 0, size);
    }

    int[][] getPath() {
        return path;
    }

    void setPath(int[][] path) {
        this.path = path;
    }

    public void genMonster(Monsters type) {
        double x = (this.x + 0.5) * size;
        double y = (this.y + 0.5) * size;
        Monster monster = new Monster(type, x, y, this, size);
    }

}
