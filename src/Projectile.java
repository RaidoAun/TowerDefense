import javafx.scene.paint.Color;

public class Projectile {

    private final int damage;
    private final double step; //Mitu pikslit liigub ühes sekundis.
    private final double diameter;
    private final Color color;
    private double currnetX;
    private double currnetY;
    private double endX;
    private double endY;

    public Projectile(double startX, double startY, int damage, double step, double diameter, Color color) {
        this.currnetX = startX;
        this.currnetY = startY;
        this.damage = damage;
        this.step = step;
        this.diameter = diameter;
        this.color = color;
    }

    public void setEndPoint(double endX, double endY) {
        this.endX = endX;
        this.endY = endY;
    }

    /*
    public void moveMissile() {
        double currentStep = Game.getLastFrameTime() * this.step;
        double xDist = endX - currnetX;
        double yDist = endY - currnetY;
        double distance = Math.hypot(xDist, yDist);
        if (currentStep <= distance) {
            this.currnetX += (xDist * currentStep) / distance;
            this.currnetY += (yDist * currentStep) / distance;
        } else {
            this.currnetX = endX;
            this.currnetY = endY;
        }
        Game.getG().setFill(this.color);
        Game.getG().fillOval(currnetX - diameter / 2, currnetY - diameter / 2, diameter, diameter);
    }

     */

    public boolean hasReachedEnd() {
        return currnetX == endX && currnetY == endY;
    }

    public int getDamage() {
        return damage;
    }

    public double getStep() {
        return step;
    }
}
