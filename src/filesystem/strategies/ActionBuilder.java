package filesystem.strategies;

import fileio.ActionInput;
import fileio.MovieInput;
import fileio.UserInput;

import java.util.List;

public class ActionBuilder {
    private List<MovieInput> currentMovieList = null;
    private List<UserInput> users = null;
    private List<MovieInput> movies = null;
    private ActionInput action = null;

    public ActionBuilder currentMovieList(List<MovieInput> currentMovieList) {
        this.currentMovieList = currentMovieList;
        return this;
    }

    public ActionBuilder users(List<UserInput> users) {
        this.users = users;
        return this;
    }

    public ActionBuilder movies(List<MovieInput> movies) {
        this.movies = movies;
        return this;
    }

    public ActionBuilder action(ActionInput action) {
        this.action = action;
        return this;
    }

    public ActionContext build() {
        return new ActionContext(currentMovieList, users, movies, action);
    }
}
