package types.strategy;

import fileio.ActionInput;
import fileio.MovieInput;
import fileio.UserInput;
import filesystem.FSConstants;
import types.*;

import java.util.List;

public final class ActionContext {
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

    /**
     * Sets the strategy attribute with the
     * corresponding TypeStrategy object by comparing
     * the action.type attribute with the corresponding
     * type from FSConstants. Strategy is set to null
     * if the corresponding failed.
     */
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

    /**
     * Calls the strategy.action method for
     * the corresponding type.
     * @return true if the operation was successful
     * else false
     */
    public boolean action() {
        if (strategy == null) {
            return false;
        }
        return strategy.action();
    }

}
