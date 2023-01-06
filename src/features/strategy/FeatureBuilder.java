package features.strategy;

import fileio.ActionInput;
import fileio.MovieInput;
import fileio.UserInput;

import java.util.List;

public final class FeatureBuilder {
    private List<MovieInput> currentMovieList = null;
    private List<UserInput> users = null;
    private List<MovieInput> movies = null;
    private ActionInput action = null;
    private MovieInput movie = null;

    /**
     * Sets the currentMovieList field and returns this object.
     * @param currentMovieList the new value of the currentMovieList attribute
     * @return this object
     */
    public FeatureBuilder currentMovieList(
            final List<MovieInput> currentMovieList) {
        this.currentMovieList = currentMovieList;
        return this;
    }

    /**
     * Sets the users field and returns this object.
     * @param users the new value of the users attribute
     * @return this object
     */
    public FeatureBuilder users(final List<UserInput> users) {
        this.users = users;
        return this;
    }

    /**
     * Sets the movies field and returns this object.
     * @param movies the new value of the movies attribute
     * @return this object
     */
    public FeatureBuilder movies(final List<MovieInput> movies) {
        this.movies = movies;
        return this;
    }

    /**
     * Sets the action field and returns this object.
     * @param action the new value of the action attribute
     * @return this object
     */
    public FeatureBuilder action(final ActionInput action) {
        this.action = action;
        return this;
    }

    /**
     * Sets the movie field and returns this object.
     * @param movie the new value of the movie attribute
     * @return this object
     */
    public FeatureBuilder movie(final MovieInput movie) {
        this.movie = movie;
        return this;
    }

    /**
     * Builds a new FeatureContext object with the
     * current attributes.
     * @return a FeatureContext object
     */
    public FeatureContext build() {
        return new FeatureContext(currentMovieList, movies, users, action, movie);
    }
}
