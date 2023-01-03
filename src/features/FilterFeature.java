package features;

import database.Database;
import fileio.ActionInput;
import fileio.MovieInput;
import filesystem.FSConstants;
import filesystem.FileSystem;
import filesystem.Page;
import features.strategy.FeatureStrategy;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class FilterFeature implements FeatureStrategy {
    final private List<MovieInput> currentMovieList;

    final private List<MovieInput> movies;
    final private ActionInput action;

    public FilterFeature(List<MovieInput> currentMovieList, List<MovieInput> movies, ActionInput action) {
        this.currentMovieList = currentMovieList;
        this.movies = movies;
        this.action = action;
    }

    /**
     * Filter the list of movies based on actors, genre, rate and duration.
     * @param movies the final result
     * @param currentMovies the input list
     * @param action the current action
     * @return true if the operation was successful else false
     */
    private static Boolean filter(final List<MovieInput> movies,
                                 final List<MovieInput> currentMovies,
                                 final ActionInput action) {
        FileSystem instance = FileSystem.getInstance();
        Page current = instance.getCurrent();

        if (!current.getName().equals(FSConstants.MOVIES_PAGE)) {
            return false;
        }

        movies.clear();

        if (action.getFilters().getContains() != null) {
            for (MovieInput movie: currentMovies) {
                boolean add = true;
                if (action.getFilters().getContains().getGenre() != null) {
                    if (!movie.getGenres().containsAll(
                            action.getFilters().getContains().getGenre())) {
                        add = false;
                    }
                }
                if (action.getFilters().getContains().getActors() != null) {
                    if (!movie.getActors().containsAll(
                            action.getFilters().getContains().getActors())) {
                        add = false;
                    }
                }
                if (add) {
                    movies.add(movie);
                }
            }
        } else {
            movies.addAll(currentMovies);
        }

        if (action.getFilters().getSort() != null) {
            if (action.getFilters().getSort().getDuration() != null) {
                for (int i = 0; i < movies.size() - 1; ++i) {
                    for (int j = i + 1; j < movies.size(); ++j) {
                        if (action.getFilters().getSort().getDuration().equals("increasing")) {
                            if (movies.get(i).getDuration() > movies.get(j).getDuration()) { // greater
                                Collections.swap(movies, i, j);
                            } else if (Objects.equals(movies.get(i).getDuration(), movies.get(j).getDuration())) { // equals
                                if (action.getFilters().getSort().getRating() != null) {
                                    if (action.getFilters().getSort().getRating().equals("increasing")) {
                                        if (movies.get(i).getRating() > movies.get(j).getRating()) {
                                            Collections.swap(movies, i, j);
                                        }
                                    } else {
                                        if (movies.get(i).getRating() < movies.get(j).getRating()) {
                                            Collections.swap(movies, i, j);
                                        }
                                    }
                                }
                            }
                        } else {
                            if (movies.get(i).getDuration() < movies.get(j).getDuration()) { // greater
                                Collections.swap(movies, i, j);
                            } else if (Objects.equals(movies.get(i).getDuration(), movies.get(j).getDuration())) { // equals
                                if (action.getFilters().getSort().getRating() != null) {
                                    if (action.getFilters().getSort().getRating().equals("increasing")) {
                                        if (movies.get(i).getRating() > movies.get(j).getRating()) {
                                            Collections.swap(movies, i, j);
                                        }
                                    } else {
                                        if (movies.get(i).getRating() < movies.get(j).getRating()) {
                                            Collections.swap(movies, i, j);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                if (action.getFilters().getSort().getRating() != null) {
                    for (int i = 0; i < movies.size() - 1; ++i) {
                        for (int j = i + 1; j < movies.size(); ++j) {
                            if (action.getFilters().getSort().getRating().equals("increasing")) {
                                if (movies.get(i).getRating() > movies.get(j).getRating()) {
                                    Collections.swap(movies, i, j);
                                }
                            } else {
                                if (movies.get(i).getRating() < movies.get(j).getRating()) {
                                    Collections.swap(movies, i, j);
                                }
                            }
                        }
                    }
                }
            }
        }

        return  true;
    }


    @Override
    public boolean action() {
        FileSystem instance = FileSystem.getInstance();
        boolean result = filter(currentMovieList,
                MovieInput.getUserMovies(instance.getCurrentUser(),
                        movies),
                action);
        instance.setCurrentMovies(currentMovieList);
        Database.getInstance().setMoviesChangeable(false);
        return result;
    }
}
