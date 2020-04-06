package blocks.towers;

public class TowerChooser {

    public static Tower getTower(Towers towerType, int x, int y) {
        switch (towerType) {
            case MÜÜR:
                return new Müür(x, y);
            case KAHUR:
                return new Kahur(x, y);
            case LASER:
                return new Laser(x, y);
            case KÜLMUTAJA:
                return new Külmutaja(x, y);
            case KUULIPILDUJA:
                return new Kuulipilduja(x, y);
            default:
                return null;
        }
    }

}
