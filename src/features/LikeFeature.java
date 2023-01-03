package features;

import fileio.MovieInput;
import fileio.UserInput;
import filesystem.FSConstants;
import filesystem.FileSystem;
import filesystem.Page;
import features.strategy.FeatureStrategy;

public class LikeFeature implements FeatureStrategy {

    /**
     * Changes the liked movies list of the user.
     * @param movie the movie to like
     * @return true if the operation was successful else false
     */
    private Boolean likeMovie(final MovieInput movie) {
        FileSystem instance = FileSystem.getInstance();
        Page current = instance.getCurrent();
        UserInput currentUser = instance.getCurrentUser();

        if (!current.getName().equals(FSConstants.SEE_DETAILS_PAGE)) {
            return false;
        }

        boolean watched = false;

        for (MovieInput movieWatched: currentUser.getWatchedMovies()) {
            if (movie.equals(movieWatched)) {
                watched = true;
                break;
            }
        }

        if (!watched) {
            return false;
        }

        movie.setNumLikes(movie.getNumLikes() + 1);
        currentUser.getLikedMovies().add(movie);

        return true;
    }

    @Override
    public boolean action() {
        FileSystem instance = FileSystem.getInstance();
        return likeMovie(instance.getCurrentMovie());
    }
}
