package features;

import fileio.MovieInput;
import fileio.UserInput;
import filesystem.FSConstants;
import filesystem.FileSystem;
import filesystem.Page;
import features.strategy.FeatureStrategy;

public class WatchFeature implements FeatureStrategy {

    /**
     * Changes the watched movies list of the user.
     * @param movie the mobie to watch
     * @return true if the operation was successful else false
     */
    private Boolean watchMovie(final MovieInput movie) {
        FileSystem instance = FileSystem.getInstance();
        Page current = instance.getCurrent();
        UserInput currentUser = instance.getCurrentUser();

        if (!current.getName().equals(FSConstants.SEE_DETAILS_PAGE)) {
            return false;
        }

        boolean purchased = false;

        for (MovieInput moviePurchased: currentUser.getPurchasedMovies()) {
            if (movie.equals(moviePurchased)) {
                purchased = true;
                break;
            }
        }

        if (!purchased) {
            return false;
        }

        currentUser.getWatchedMovies().add(movie);

        return true;
    }

    @Override
    public boolean action() {
        FileSystem instance = FileSystem.getInstance();
        return watchMovie(instance.getCurrentMovie());
    }
}
