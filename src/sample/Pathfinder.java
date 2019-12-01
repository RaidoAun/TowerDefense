package sample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class Pathfinder {

    private int[][] map;
    private int[] start;
    private int[] end;
    private List<int[]> openList = new ArrayList<>();
    private List<int[]> openListxy = new ArrayList<>();
    private List<Integer> openListF = new ArrayList<>();
    private List<int[]> closedList = new ArrayList<>(); // [x, y, parentx, parenty]
    private List<int[]> closedxy = new ArrayList<>(); // [x, y]
    private int[] current;
    private int[][] finalPath;
    private String[][] markedPath;
    private boolean pathPossible;

    Pathfinder(int[][] maatrix, int[] algus, int[] lopp) {
        map = maatrix;
        start = algus;
        end = lopp;
        closedList = scanMap();
        if (closedList.size() > 0) {
            finalPath = invertPath(findPath());
            markedPath = drawPath();
            pathPossible = true;
        } else {
            pathPossible = false;
            finalPath = new int[0][0];
        }
    }

    public List<int[]> getClosedList() {
        return closedList;
    }

    public int[][] getFinalPath() {
        return finalPath;
    }

    private String[][] drawPath() {
        String[][] m = new String[map.length][map[0].length];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (matrixContains(new int[]{j, i}, Arrays.asList(finalPath))) {
                    m[i][j] = "●";

                } else if (map[i][j] == 1) {
                    m[i][j] = "■";
                } else {
                    m[i][j] = Integer.toString(map[i][j]);
                }
            }
        }
        return m;
    }

    private int[][] findPath() {
        List<int[]> path = new ArrayList<>();
        int[][] closedArray = new int[closedList.size()][closedList.get(0).length];
        closedArray = closedList.toArray(closedArray);
        int[] parent = end;
        int[] c;
        for (int i = closedArray.length - 1; i >= 0; i--) {
            c = new int[]{closedArray[i][0], closedArray[i][1]};
            if (Arrays.equals(c, parent)) {
                path.add(c);
                parent = new int[]{closedArray[i][2], closedArray[i][3]};
            } else if (Arrays.equals(c, start)) {
                break;
            }
        }
        path.remove(0);
        path.remove(path.size() - 1);
        int[][] pathArray = new int[path.size()][path.get(0).length];
        return path.toArray(pathArray);
    }

    private int[][] invertPath(int[][] path) {
        int[][] invertedPath = new int[path.length][path[0].length];
        for (int i = path.length - 1; i >= 0; i--) {
            invertedPath[path.length - 1 - i] = path[i];
        }
        return invertedPath;
    }

    private List<int[]> scanMap() {
        int h_algus = leiaHupotenuus(start[0], end[0], start[1], end[1]);
        int[] startBlock = {start[0], start[1], start[0], start[1], 0, h_algus, h_algus}; // [x, y, parentx, parenty, G, H, F]
        openList.add(startBlock);
        openListxy.add(start);
        openListF.add(startBlock[6]);
        boolean leitud = false;
        while (openList.size() > 0) {
            int minIndex = openListF.indexOf(Collections.min(openListF));
            current = openList.get(minIndex);
            int[] currentxy = new int[]{current[0], current[1]};
            if (Arrays.equals(currentxy, end)) {
                leitud = true;
                closedList.add(current);
                break;
            }
            openList.remove(minIndex);
            openListF.remove(minIndex);
            openListxy.remove(minIndex);
            closedList.add(current);
            closedxy.add(currentxy);
            int[][] naabrid = leiaNaabrid();
            if (naabrid.length > 0) {
                updateOpenlist(naabrid);
            }
        }
        if (leitud) {
            return closedList;
        } else {
            return new ArrayList<>();
        }
    }

    private int leiaHupotenuus(int x1, int x2, int y1, int y2) {
        int xpikkus = Math.abs(x2 - x1);
        int ypikkus = Math.abs(y2 - y1);
        double c = (Math.pow(xpikkus, 2) + Math.pow(ypikkus, 2)) * 10;
        return (int) c;
    }

    private int[][] leiaNaabrid() {
        List<int[]> naabrid = new ArrayList<>();
        int uusx;
        int uusy;
        for (int i = -1; i < 2; i += 2) {
            for (int j = 0; j < 2; j++) {
                if (j == 0) {
                    uusx = current[0] + i;
                    uusy = current[1];
                } else {
                    uusx = current[0];
                    uusy = current[1] + i;
                }
                int[] uusxy = new int[]{uusx, uusy};
                boolean uusxonpiirides = uusx >= 0 && uusx <= map[0].length - 1;
                boolean uusyonpiirides = uusy >= 0 && uusy <= map.length - 1;
                boolean onpiirides = uusxonpiirides && uusyonpiirides;
                if (onpiirides && map[uusy][uusx] != 1 && !matrixContains(uusxy, closedxy) && !matrixContains(uusxy, openListxy)) { // kui blokk on läbitav, ei ole closed ning ei asu open listis
                    int h = leiaHupotenuus(uusx, end[0], uusy, end[1]);
                    int g = current[4] + 10;
                    int f = h + g;
                    int[] naaber = new int[]{uusx, uusy, current[0], current[1], g, h, f};
                    naabrid.add(naaber);
                }
            }
        }
        int[][] naabridarray = new int[naabrid.size()][7];
        naabridarray = naabrid.toArray(naabridarray);
        return naabridarray;
    }

    private void updateOpenlist(int[][] naabrid) {
        for (int[] naaber : naabrid) {
            int[] naabrixy = new int[]{naaber[0], naaber[1]};
            if (!matrixContains(naabrixy, closedxy) && !matrixContains(naabrixy, openListxy)) {
                openList.add(naaber);
                openListF.add(naaber[6]);
                openListxy.add(new int[]{naaber[0], naaber[1]});
            }
        }
    }

    private boolean matrixContains(int[] otsitav, List<int[]> matrix) {
        for (int[] i : matrix) {
            if (Arrays.equals(i, otsitav)) {
                return true;
            }
        }
        return false;
    }

    void printPath() {
        if (pathPossible) {
            for (String[] i : markedPath) {
                for (String j : i) {
                    System.out.print(j + " ");
                }
                System.out.println();
            }
        } else {
            System.out.println("Impossible!!!");
        }
    }

}