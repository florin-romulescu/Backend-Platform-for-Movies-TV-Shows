package filesystem.strategies;

import database.Database;
import fileio.ActionInput;
import fileio.MovieInput;
import fileio.UserInput;
import filesystem.FSConstants;

import java.util.List;

public class FeatureContext{
    FeatureStrategy strategy;
    private List<MovieInput> currentMovieList;
    private List<MovieInput> movies;
    private List<UserInput> users;
    private ActionInput action;

    FeatureContext(List<MovieInput> currentMovieList, List<MovieInput> movies, List<UserInput> users, ActionInput action) {
        this.currentMovieList = currentMovieList;
        this.movies = movies;
        this.users = users;
        this.action = action;
    }

    public void createStrategy() {
        strategy = switch (action.getFeature()) {
            case FSConstants.LOGIN_PERMISSION -> new LogInFeature(users, action);
            case FSConstants.REGISTER_PERMISSION -> new RegisterFeature(users, action);
            case FSConstants.SEARCH_PERMISSION -> new SearchFeature(currentMovieList, movies, action);
            case FSConstants.FILTER_PERMISSION -> new FilterFeature(currentMovieList, movies, action);
            case FSConstants.TOKENS_PERMISSION -> new TokensFeature(action);
            case FSConstants.PREMIUM_PERMISSION -> new PremiumFeature();
            case FSConstants.PURCHASE_PERMISSION -> new PurchaseFeature();
            case FSConstants.WATCH_PERMISSION -> new WatchFeature();
            case FSConstants.LIKE_PERMISSION -> new LikeFeature();
            case FSConstants.RATE_PERMISSION -> new RateFeature(action);
            default -> null;
        };
    }

    public boolean action() {
        if (strategy == null) {
            return false;
        }
        boolean result =  strategy.action();
        if (!result || action.getFeature().equals(FSConstants.LOGIN_PERMISSION)
                || action.getFeature().equals(FSConstants.REGISTER_PERMISSION)) {
            Database.getInstance().setDisplay(true);
        }
        return result;
    }

}
