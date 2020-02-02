import java.util.*;

public class TestSite {
    public static void main(String[] args) {
        List<Maja> majad = new ArrayList<>(
                Arrays.asList(new Maja(20, "Valge"), new Maja(30, "Viru"), new Maja(1, "Must"))
        );
        Comparator<Maja> comparator = new Comparator<Maja>() {
            public int compare(Maja o1, Maja o2) {
                return Integer.compare(o1.getHind(), o2.getHind());
            }
        };
        for (Maja maja : majad) {
            System.out.println(maja.getHind());
        }
        majad.sort(comparator);
        for (Maja maja : majad) {
            System.out.println(maja.getHind());
        }
    }
}
