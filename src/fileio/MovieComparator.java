package fileio;

import java.util.Comparator;

public class MovieComparator implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        MovieInput obj1 = (MovieInput) o1;
        MovieInput obj2 = (MovieInput) o2;

        int result = obj1.getDuration().compareTo(obj2.getDuration());
        if (result == 0) {
            return obj1.getRating().compareTo(obj2.getRating());
        }
        return result;
    }
}

