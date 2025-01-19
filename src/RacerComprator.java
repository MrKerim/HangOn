import java.util.Comparator;

public class RacerComprator  implements Comparator<Racer> {
    @Override
    public int compare(Racer r1, Racer r2) {
        return (int) (r2.get_z()-r1.get_z());
    }
}
