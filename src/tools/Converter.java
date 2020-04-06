package tools;

import towerdefense.Main;

public class Converter {

    public static int pixToIndex(int pixel) {
        return (int) Math.floor((double) pixel / Main.blockSize);
    }

    public static int indexToPix(int index) {
        return index * Main.blockSize + Main.blockSize / 2;
    }

}
