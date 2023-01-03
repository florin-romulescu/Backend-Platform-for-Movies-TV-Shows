package features;

import fileio.ActionInput;
import fileio.MovieInput;
import fileio.UserInput;
import filesystem.FSConstants;
import filesystem.FileSystem;
import filesystem.Page;
import features.strategy.FeatureStrategy;

public class RateFeature implements FeatureStrategy {
    private final ActionInput action;

    public RateFeature(ActionInput action) {
        this.action = action;
    }

    /**
     * Changes the rated movies list of the user
     * and the rate of the movie
     * @param movie the movie to rate
     * @return true if the operation was successful else false
     */
    private Boolean rateMovie(final MovieInput movie) {
        FileSystem instance = FileSystem.getInstance();
        Page current = instance.getCurrent();
        UserInput currentUser = instance.getCurrentUser();

        if (!current.getName().equals(FSConstants.SEE_DETAILS_PAGE)) {
            return false;
        }

        boolean watched = false;

        for (MovieInput movieWatched: currentUser.getWatchedMovies()) {
            if (movie.getName().equals(movieWatched.getName())) {
                watched = true;
                break;
            }
        }

        if (!watched) {
            return false;
        }
        double rating = action.getRate();
        movie.getRatings().add(rating);
        movie.setNumRatings(
                movie.getNumRatings() + 1
        );

        double sum = 0;
        for (double rate: movie.getRatings()) {
            sum += rate;
        }
        movie.setRating(sum / movie.getRatings().size());
        currentUser.getRatedMovies().add(movie);

        return true;
    }


    @Override
    public boolean action() {
        FileSystem instance = FileSystem.getInstance();
        return rateMovie(instance.getCurrentMovie());
    }
}
