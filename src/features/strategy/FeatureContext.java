package features.strategy;

import database.Database;
import features.*;
import features.FilterFeature;
import features.LikeFeature;
import features.LogInFeature;
import features.PremiumFeature;
import features.RateFeature;
import features.RegisterFeature;
import features.TokensFeature;
import features.WatchFeature;
import fileio.ActionInput;
import fileio.MovieInput;
import fileio.UserInput;
import filesystem.FSConstants;

import java.util.List;

public class FeatureContext{
    private FeatureStrategy strategy;
    private final List<MovieInput> currentMovieList;
    private final List<MovieInput> movies;
    private final List<UserInput> users;
    private final ActionInput action;

    FeatureContext(final List<MovieInput> currentMovieList,
                   final List<MovieInput> movies,
                   final List<UserInput> users,
                   final ActionInput action,
                   final MovieInput movie) {
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
            case FSConstants.PURCHASE_PERMISSION -> new features.PurchaseFeature();
            case FSConstants.WATCH_PERMISSION -> new WatchFeature();
            case FSConstants.LIKE_PERMISSION -> new LikeFeature();
            case FSConstants.RATE_PERMISSION -> new RateFeature(action);
            case FSConstants.DATABASE_ADD -> new DatabaseAddFeature(action);
            case FSConstants.DATABASE_DELETE -> new DatabaseDeleteFeature(action);
            case FSConstants.RECOMMENDATION -> new RecommendationFeature();
            case FSConstants.SUBSCRIBE -> new SubscribeFeature(action);
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
