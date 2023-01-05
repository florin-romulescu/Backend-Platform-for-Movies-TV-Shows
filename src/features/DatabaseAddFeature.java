package features;

import database.Database;
import features.strategy.FeatureStrategy;
import fileio.ActionInput;
import fileio.MovieInput;
import fileio.Notification;

public class DatabaseAddFeature implements FeatureStrategy {
    ActionInput action;

    public DatabaseAddFeature(final ActionInput action) {
        this.action = action;
    }

    private boolean add() {
        MovieInput addedMovie = action.getAddedMovie();
        Database instance = Database.getInstance();

        if (instance.getMovies().contains(addedMovie)) {
            return false;
        }
        instance.getMovies().add(addedMovie);
        Notification msg = new Notification(addedMovie.getName(), "ADD");
        addedMovie.getGenres().forEach(
                genre -> instance.newMovieNotifier(msg, genre)
        );
        return true;
    }

    @Override
    public boolean action() {
        return add();
    }
}
