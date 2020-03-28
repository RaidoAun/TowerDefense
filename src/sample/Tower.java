package sample;

public class Tower extends Block {

    private double x;
    private double y;
    private int level;
    private double range;
    private boolean active;
    private int damage;
    private int hind;
    private int maxLevel;

    Tower(Towers type, double x, double y) {
        super(type.getId() + 10, type.getDmg(), type.getColor());
        this.x = x;
        this.y = y;
        this.level = 1;
        this.range = type.getRange();
        this.active = false;
        this.damage = this.value;
        this.hind = type.getHind();
        this.maxLevel = type.getMaxLevel();
    }

    public Block getBlock() {
        return new Block(this.id, this.value, this.color);
    }

}
