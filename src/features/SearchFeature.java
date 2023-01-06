package features;

import database.Database;
import fileio.ActionInput;
import fileio.MovieInput;
import filesystem.FSConstants;
import filesystem.FileSystem;
import filesystem.Page;
import features.strategy.FeatureStrategy;

import java.util.List;

public final class SearchFeature implements FeatureStrategy {
    private final List<MovieInput> currentMovieList;

    private final List<MovieInput> movies;
    private final ActionInput action;

    public SearchFeature(final List<MovieInput> currentMovieList,
                         final List<MovieInput> movies,
                         final ActionInput action) {
        this.currentMovieList = currentMovieList;
        this.movies = movies;
        this.action = action;
    }

    /**
     * Search through the movie list. All the movies that starts with the given search input
     * are stored in the movies variable.
     * @return true if the operation was successful else false
     */
    private static Boolean search(final List<MovieInput> movies,
                                 final List<MovieInput> currentMovies,
                                 final ActionInput action) {
        FileSystem instance = FileSystem.getInstance();
        Page current = instance.getCurrent();
        movies.clear();

        if (!current.getName().equals(FSConstants.MOVIES_PAGE)) {
            return false;
        }

        for (MovieInput movie: currentMovies) {
            if (movie.getName().startsWith(action.getStartsWith())) {
                movies.add(movie);
            }
        }

        return true;
    }

    @Override
    public boolean action() {
        FileSystem instance = FileSystem.getInstance();
        boolean result = search(currentMovieList,
                MovieInput.getUserMovies(instance.getCurrentUser(),
                        movies),
                action);
        instance.setCurrentMovies(currentMovieList);
        Database.getInstance().setMoviesChangeable(false);
        return result;
    }
}
