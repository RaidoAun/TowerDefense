package tools;

import towerdefense.Main;

public class Converter {

    public static int pixToIndex(double pixel) {
        return (int) Math.floor(pixel / Main.blockSize);
    }

    public static double indexToPix(int index) {
        return index * Main.blockSize + (double) Main.blockSize / 2;
    }

}
