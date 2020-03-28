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
    private int gCost;

    Pathfinder(int[][] map, int[] start, int[] end, int gCost) {
        this.gCost = gCost;
        this.map = map;
        this.start = start;
        this.end = end;
        closedList = scanMap();
        if (closedList.size() > 0) {
            finalPath = invertPath(findPath());
        } else {
            finalPath = new int[0][0];
        }
    }

    int[][] getFinalPath() {
        return finalPath;
    }

    private int[][] findPath() {
        //Meetodi tööle vajalikud muutujad.
        int[] currentPoint;
        int[] currentParent;
        List<int[]> path = new ArrayList<>();
        List<int[]> parents = getParents();
        //Path hakkab lõpust pihta, sest lõpust algusesse saab olla ainult üks tee.
        int index = closedxy.size() - 1;
        //While loop töötab kuni algusesse jõudmiseni.
        while (index > 0) {
            currentPoint = closedxy.get(index);
            path.add(currentPoint);
            currentParent = parents.get(index);
            index = matrixIndexof(currentParent, closedxy);
        }
        if (path.size() > 0) {
            int[][] pathArray = new int[path.size()][path.get(0).length];
            return path.toArray(pathArray);
        } else {
            return new int[0][0];
        }
    }

    private int[][] invertPath(int[][] path) {
        if (path.length > 0) {
            int[][] invertedPath = new int[path.length][path[0].length];
            for (int i = path.length - 1; i >= 0; i--) {
                invertedPath[path.length - 1 - i] = path[i];
            }
            return invertedPath;
        } else {
            return new int[0][0];
        }
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
        double c = Math.sqrt(Math.pow(xpikkus, 2) + Math.pow(ypikkus, 2));
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
                Set<Integer> notPassable = new HashSet<>(Set.of(1, 10, 2));
                if (onpiirides && /*map[uusy][uusx] != 1*/ !notPassable.contains(map[uusy][uusx]) && !matrixContains(uusxy, openListxy)) { // kui blokk on läbitav ning ei asu juba open listis
                    int h = leiaHupotenuus(uusx, end[0], uusy, end[1]);
                    int g = current[4] + this.gCost;
                    int f = h + g;
                    int[] naaber = new int[]{uusx, uusy, current[0], current[1], g, h, f};
                    //Kui blokk asub closed listis ning temasse on minna soodsam.
                    if (matrixContains(uusxy, closedxy)) {
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

    private List<int[]> getParents() {
        List<int[]> paretnid = new ArrayList<>();
        for (int[] closed : closedList) {
            paretnid.add(new int[]{closed[2], closed[3]});
        }
        return paretnid;
    }

}