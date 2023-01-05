package types.strategy;

import features.SubscribeFeature;
import fileio.ActionInput;
import fileio.MovieInput;
import fileio.UserInput;
import filesystem.FSConstants;
import types.*;

import java.util.List;

public class ActionContext {
    private TypeStrategy strategy;
    private final List<MovieInput> currentMovieList;
    private final List<UserInput> users;
    private final List<MovieInput> movies;
    private final ActionInput action;

    public ActionContext(final List<MovieInput> currentMovieList,
                         final List<UserInput> users,
                         final List<MovieInput> movies,
                         final ActionInput action) {
        this.currentMovieList = currentMovieList;
        this.users = users;
        this.movies = movies;
        this.action = action;
    }

    public void createStrategy() {
        strategy = switch (action.getType()) {
            case FSConstants.CHANGE_PAGE -> new ChangePageType(action);
            case FSConstants.ON_PAGE -> new OnPageType(currentMovieList, users, movies, action);
            case FSConstants.GO_BACK -> new GoBackType();
            case FSConstants.DATABASE -> new DatabaseType(action);
            case FSConstants.RECOMMENDATION -> new RecommendationType(action);
            default -> null;
        };
    }

    public boolean action() {
        if (strategy == null) {
            return false;
        }
        return strategy.action();
    }

}
