package tools;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import blocks.Block;
import map.Map;

public class Click {

    public double pixelX;
    public double pixelY;
    public int indexX;
    public int indexY;
    public Block eventblock;
    public MouseEvent event;
    public boolean primary;
    public boolean secondary;

    public Click(double pixelX, double pixelY, Map map, MouseEvent event) {
        this.pixelX = pixelX;
        this.pixelY = pixelY;
        this.indexX = Converter.pixToIndex((int) pixelX);
        this.indexY = Converter.pixToIndex((int) pixelY);
        this.event = event;

        if (event.getButton() == MouseButton.PRIMARY) {
            this.primary = true;
            this.secondary = false;
        } else if (event.getButton() == MouseButton.SECONDARY) {
            this.primary = false;
            this.secondary = true;
        }

        if (map != null) {
            this.eventblock = map.getBlock(indexX, indexY);
        }
    }

    public int getPixelX() {
        return (int) pixelX;
    }

    public int getPixelY() {
        return (int) pixelY;
    }
}
