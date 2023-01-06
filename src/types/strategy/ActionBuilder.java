package types.strategy;

import fileio.ActionInput;
import fileio.MovieInput;
import fileio.UserInput;

import java.util.List;

public final class ActionBuilder {
    private List<MovieInput> currentMovieList = null;
    private List<UserInput> users = null;
    private List<MovieInput> movies = null;
    private ActionInput action = null;

    /**
     * Sets the currentMovieList field and returns this object.
     * @param currentMovieList the new value of the currentMovieList attribute
     * @return this object
     */
    public ActionBuilder currentMovieList(
            final List<MovieInput> currentMovieList) {
        this.currentMovieList = currentMovieList;
        return this;
    }

    /**
     * Sets the users field and returns this object.
     * @param users the new value of the users attribute
     * @return this object
     */
    public ActionBuilder users(final List<UserInput> users) {
        this.users = users;
        return this;
    }

    /**
     * Sets the movies field and returns this object.
     * @param movies the new value of the movies attribute
     * @return this object
     */
    public ActionBuilder movies(final List<MovieInput> movies) {
        this.movies = movies;
        return this;
    }

    /**
     * Sets the action field and returns this object.
     * @param action the new value of the action attribute
     * @return this object
     */
    public ActionBuilder action(final ActionInput action) {
        this.action = action;
        return this;
    }

    /**
     * Builds an ActionContext object with the given attributes.
     * @return an ActionContext object
     */
    public ActionContext build() {
        return new ActionContext(currentMovieList, users, movies, action);
    }
}
