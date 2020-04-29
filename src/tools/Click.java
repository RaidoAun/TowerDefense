package tools;

import blocks.Node;
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

    public Click(MouseEvent event, Map map) {
        this.pixelX = event.getX();
        this.pixelY = event.getY();
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
            this.eventblock = new Node(map.getNode(indexX, indexY));
        }
    }

    public int getPixelX() {
        return (int) pixelX;
    }

    public int getPixelY() {
        return (int) pixelY;
    }
}
