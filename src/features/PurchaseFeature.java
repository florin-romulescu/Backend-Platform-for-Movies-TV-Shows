package features;

import database.Database;
import fileio.MovieInput;
import fileio.UserInput;
import filesystem.FSConstants;
import filesystem.FileSystem;
import filesystem.Page;
import features.strategy.FeatureStrategy;

public final class PurchaseFeature implements FeatureStrategy {

    /**
     * Changes the purchase movies list and the current tokens
     * of the user.
     * @param movie the movie to buy
     * @return true if the operation was successful else false
     */
    private Boolean purchaseMovie(final MovieInput movie) {
        FileSystem instance = FileSystem.getInstance();
        Page current = instance.getCurrent();
        UserInput currentUser = instance.getCurrentUser();
        if (!current.getName().equals(FSConstants.SEE_DETAILS_PAGE)) {
            return false;
        }
        boolean purchasable = false;
        for (MovieInput m: Database.getInstance().getMovies()) {
            if (m.equals(movie)) {
                purchasable = true;
                break;
            }
        }

        if (currentUser.getPurchasedMovies().contains(movie)) {
            return false;
        }

        if (!purchasable) {
            return false;
        }

        if (currentUser.getCredentials().getAccountType().equals("premium")) {
            if (currentUser.getNumFreePremiumMovies() > 0) {
                currentUser.setNumFreePremiumMovies(
                        currentUser.getNumFreePremiumMovies() - 1
                );
                currentUser.getPurchasedMovies().add(movie);
                return true;
            }
        }

        int count = 2;
        int tokens = currentUser.getTokensCount();

        if (tokens < count) {
            return false;
        }

        currentUser.setTokensCount(tokens - count);
        currentUser.getPurchasedMovies().add(movie);

        return true;
    }

    @Override
    public boolean action() {
        FileSystem instance = FileSystem.getInstance();
        return purchaseMovie(instance.getCurrentMovie());
    }
}
