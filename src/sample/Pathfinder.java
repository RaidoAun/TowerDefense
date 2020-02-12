package sample;

import javafx.scene.paint.Color;
import java.util.*;

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
            looTee();
        } else {
            pathPossible = false;
            finalPath = new int[0][0];
        }
    }

    int[][] getFinalPath() {
        return finalPath;
    }

    int[][] getClosedList() {
        int[][] xy = new int[closedList.size()][7];
        xy = closedList.toArray(xy);
        return xy;
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
        List<int[]> parents = getParents();
        List<int[]> algusetaClosedxy = closedxy;
        List<Integer> algusetaF = getFcosts();
        algusetaClosedxy.remove(0);
        parents.remove(0);
        algusetaF.remove(0);
        int index = parents.size() - 1;
        System.out.println(Arrays.toString(parents.get(index)));
        List<Integer> uuedIndexid;
        List<int[]> uuedIndexidKoosf;
        while (true) {
            if (index > 0) {
                int[] xy = algusetaClosedxy.get(index);
                path.add(xy);
                uuedIndexid = matrixIndexesof(parents.get(index), algusetaClosedxy);
                if (uuedIndexid.size() > 1) {
                    System.out.println(Arrays.toString(uuedIndexid.toArray()));
                }
                if (matrixIndexesof(xy, parents).size() > 1) {
                    System.out.println(Arrays.toString(matrixIndexesof(xy, parents).toArray()));
                }
                uuedIndexidKoosf = new ArrayList<>();
                for (Integer integer : uuedIndexid) {
                    uuedIndexidKoosf.add(new int[]{integer, algusetaF.get(integer)});
                }

                Comparator<int[]> comparator = new Comparator<int[]>() {
                    public int compare(int[] o1, int[] o2) {
                        return Integer.compare(o1[1], o2[1]);
                    }
                };

                uuedIndexidKoosf.sort(comparator);
                //System.out.println(Arrays.deepToString(uuedIndexidKoosf.toArray()));
                if (uuedIndexidKoosf.size() == 0) {
                    System.out.println("ERROR");
                    System.out.println(Arrays.deepToString(path.toArray()));
                    System.out.println(Arrays.toString(start));
                    break;
                }
                index = uuedIndexidKoosf.get(0)[0];
            } else if (index == 0) {
                int[] xy = algusetaClosedxy.get(index);
                path.add(xy);
                break;
            }
        }
        path.remove(0);
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
                closedxy.add(currentxy);
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
                if (onpiirides && map[uusy][uusx] != 1 && !matrixContains(uusxy, openListxy)) { // kui blokk on läbitav ning ei asu juba open listis
                    int h = leiaHupotenuus(uusx, end[0], uusy, end[1]);
                    int g = current[4] + 1000;
                    int f = h + g;
                    int[] naaber = new int[]{uusx, uusy, current[0], current[1], g, h, f};
                    if (matrixContains(uusxy, closedxy)) { // kui blokk asub closed listis
                        int index = matrixIndexof(uusxy, closedxy);
                        int[] closedNode = closedList.get(index);
                        if (closedNode[6] > f) {
                            closedList.set(index, naaber);
                        }
                    } else {
                        naabrid.add(naaber);
                    }
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

    private int matrixIndexof(int[] otsitav, List<int[]> matrix) {
        for (int i = 0; i < matrix.size(); i++) {
            if (matrix.get(i)[0] == otsitav[0] && matrix.get(i)[1] == otsitav[1]) {
                return i;
            }
        }
        return -1;
    }

    private List<Integer> matrixIndexesof(int[] otsitav, List<int[]> matrix) {
        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < matrix.size(); i++) {
            if (matrix.get(i)[0] == otsitav[0] && matrix.get(i)[1] == otsitav[1]) {
                indexes.add(i);
            }
        }
        return indexes;
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

    private List<int[]> getParents() {
        List<int[]> paretnid = new ArrayList<>();
        for (int[] closed : closedList) {
            paretnid.add(new int[]{closed[2], closed[3]});
        }
        return paretnid;
    }

    private List<Integer> getFcosts() {
        List<Integer> f = new ArrayList<>();
        for (int[] closed : closedList) {
            f.add(closed[6]);
        }
        return f;
    }

    void printMap() {
        StringBuilder stringRida = new StringBuilder();
        for (int[] mapRida : map) {
            for (int ruut : mapRida) {
                if (ruut == 0) {
                    stringRida.append("⬜");
                } else if (ruut == 1) {
                    stringRida.append("⬛");
                } else if (ruut == 2){
                    stringRida.append("◇");
                } else if (ruut == 3) {
                    stringRida.append("◆");
                } else {
                    stringRida.append("◉");
                }
            }
            System.out.println(stringRida);
            stringRida.setLength(0);
        }
    }

    private void looTee() {
        for (int[] p : finalPath) {
            map[p[1]][p[0]] = 4;
        }
    }

    void draw_path_onMap(Map m) {
        if (finalPath.length > 0) {
            for (int[] p : finalPath) {
                m.editMap_matrix(p[0], p[1], new Block(5, 5, new Color(0, 1, 1, 1)));
            }
        }
    }

}