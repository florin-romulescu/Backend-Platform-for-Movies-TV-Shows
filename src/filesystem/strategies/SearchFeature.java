package filesystem.strategies;

import fileio.ActionInput;
import fileio.MovieInput;
import filesystem.FSActions;
import filesystem.FileSystem;

import java.util.List;

public class SearchFeature implements FeatureStrategy {
    final private List<MovieInput> currentMovieList;

    final private List<MovieInput> movies;
    final private ActionInput action;

    public SearchFeature(List<MovieInput> currentMovieList, List<MovieInput> movies, ActionInput action) {
        this.currentMovieList = currentMovieList;
        this.movies = movies;
        this.action = action;
    }

    @Override
    public boolean action() {
        FileSystem instance = FileSystem.getInstance();
        boolean result = FSActions.search(currentMovieList,
                MovieInput.getUserMovies(instance.getCurrentUser(),
                        movies),
                action);
        instance.setCurrentMovies(currentMovieList);
        return result;
        // TODO
    }
}
