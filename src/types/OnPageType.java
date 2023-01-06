package types;

import features.strategy.FeatureBuilder;
import features.strategy.FeatureContext;
import fileio.ActionInput;
import fileio.MovieInput;
import fileio.UserInput;
import types.strategy.TypeStrategy;

import java.util.List;

public final class OnPageType implements TypeStrategy {

    private List<MovieInput> currentMovieList = null;
    private List<UserInput> users = null;
    private List<MovieInput> movies = null;
    private ActionInput action = null;

    public OnPageType(final List<MovieInput> currentMovieList,
                      final List<UserInput> users,
                      final List<MovieInput> movies,
                      final ActionInput action) {
        this.currentMovieList = currentMovieList;
        this.users = users;
        this.movies = movies;
        this.action = action;
    }

    @Override
    public boolean action() {
        FeatureContext featureContext = new FeatureBuilder()
                .currentMovieList(currentMovieList)
                .movies(movies)
                .users(users)
                .action(action)
                .build();
        featureContext.createStrategy();
        return featureContext.action();
    }
}
