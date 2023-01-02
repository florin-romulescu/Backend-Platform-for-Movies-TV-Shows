package filesystem.strategies;

import fileio.ActionInput;
import fileio.MovieInput;
import fileio.UserInput;
import filesystem.FSConstants;

import java.util.List;

public class ActionContext {
    private TypeStrategy strategy;
    private List<MovieInput> currentMovieList;
    private List<UserInput> users;
    private List<MovieInput> movies;
    private ActionInput action;

    public ActionContext(List<MovieInput> currentMovieList, List<UserInput> users, List<MovieInput> movies, ActionInput action) {
        this.currentMovieList = currentMovieList;
        this.users = users;
        this.movies = movies;
        this.action = action;
    }

    public ActionContext(ActionInput action) {
        this.action = action;
    }

    public void createStrategy() {
        strategy = switch (action.getType()) {
            case FSConstants.CHANGE_PAGE -> new ChangePageType(action);
            case FSConstants.ON_PAGE -> new OnPageType(currentMovieList, users, movies, action);
            default -> null;
        };
    }

    public boolean action() {
        return strategy.action();
    }

}
