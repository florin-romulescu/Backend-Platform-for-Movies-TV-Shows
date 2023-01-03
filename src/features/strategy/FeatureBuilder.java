package features.strategy;

import fileio.ActionInput;
import fileio.MovieInput;
import fileio.UserInput;

import java.util.List;

public class FeatureBuilder {
    private List<MovieInput> currentMovieList;
    private List<UserInput> users;
    private List<MovieInput> movies;
    private ActionInput action;

    public FeatureBuilder currentMovieList(List<MovieInput> currentMovieList) {
        this.currentMovieList = currentMovieList;
        return this;
    }

    public FeatureBuilder users(List<UserInput> users) {
        this.users = users;
        return this;
    }

    public FeatureBuilder movies(List<MovieInput> movies) {
        this.movies = movies;
        return this;
    }

    public FeatureBuilder action(ActionInput action) {
        this.action = action;
        return this;
    }

    public FeatureContext build() {
        return new FeatureContext(currentMovieList, movies, users, action);
    }
}
