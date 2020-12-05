package sweetComparators;

import sweetPackage.Sweet;

import java.util.Comparator;

public class WeightComparator implements Comparator<Sweet> {
    @Override
    public int compare(Sweet o1, Sweet o2) {
        return o1.getWeight() - o2.getWeight();
    }
}
