package sample;

import java.util.Arrays;

public class MapTest {
    private int[] start;
    private int[] end;
    private int x;
    private int y;
    private int[][] seinad;
    private int[][] mapMatrix;

    MapTest(int x, int y, int[][] seinad, int[] start, int[] end) {
        this.x = x;
        this.y = y;
        this.seinad = seinad;
        this.start = start;
        this.end = end;
        mapMatrix = genMap();
    }

    public int[][] getMap() {
        return mapMatrix;
    }

    public void printMap() {
        for (int[] yp : mapMatrix) {
            for (int xp : yp) {
                System.out.print(xp);
            }
            System.out.println();
        }
    }

    private int[][] genMap() {
        int[][] map = new int[y][x];
        for (int i = 0; i < y; i++) {   //i = y ja j = x
            for (int j = 0; j < x; j++) {
                int[] xy = new int[]{j, i};
                if (matrixContains(xy, seinad)) {   //vaba = 0; suletud = 1; start = 2; lõpp = 3
                    map[i][j] = 1;
                } else if (Arrays.equals(start, xy)) {
                    map[i][j] = 2;
                } else if (Arrays.equals(end, xy)) {
                    map[i][j] = 3;
                } else {
                    map[i][j] = 0;
                }
            }
        }
        return map;
    }

    private boolean matrixContains(int[] otsitav, int[][] matrix) {
        for (int[] liige : matrix) {
            if (Arrays.equals(liige, otsitav)) {
                return true;
            }
        }
        return false;
    }

}
