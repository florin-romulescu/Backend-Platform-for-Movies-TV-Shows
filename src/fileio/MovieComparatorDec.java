package fileio;

import java.util.Comparator;

public class MovieComparatorDec implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        MovieInput obj1 = (MovieInput) o1;
        MovieInput obj2 = (MovieInput) o2;

        int result = obj2.getDuration().compareTo(obj1.getDuration());
        if (result == 0) {
            return obj2.getRating().compareTo(obj1.getRating());
        }
        return result;
    }
}