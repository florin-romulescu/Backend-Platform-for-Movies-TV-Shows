package features;

import database.Database;
import features.strategy.FeatureStrategy;
import fileio.ActionInput;
import fileio.MovieInput;
import fileio.Notification;
import fileio.UserInput;

public final class DatabaseDeleteFeature  implements FeatureStrategy {
    private final ActionInput action;

    public DatabaseDeleteFeature(final ActionInput action) {
        this.action = action;
    }

    private boolean delete() {
        Database instance = Database.getInstance();
        MovieInput movie = instance.getMovie(action.getDeletedMovie());

        if (movie == null) {
            return false;
        }

        Notification msg = new Notification(movie.getName(), "DELETE");
        instance.newMovieNotifier(msg, movie);

        for (UserInput userInput: instance.getUsers()) {
            if (userInput.getPurchasedMovies().contains(movie)) {
                if (userInput.getCredentials().getAccountType().equals("premium")) {
                    userInput.setNumFreePremiumMovies(
                            userInput.getNumFreePremiumMovies() + 1
                    );
                } else {
                    userInput.setTokensCount(
                            userInput.getTokensCount() + 2
                    );
                }
            }
            userInput.getLikedMovies().remove(movie);
            userInput.getRatedMovies().remove(movie);
            userInput.getWatchedMovies().remove(movie);
            userInput.getPurchasedMovies().remove(movie);
        }
        instance.getMovies().remove(movie);

        return true;
    }

    @Override
    public boolean action() {
        return delete();
    }
}
