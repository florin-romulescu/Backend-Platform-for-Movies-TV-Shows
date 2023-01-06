package features;

import fileio.ActionInput;
import fileio.UserInput;
import filesystem.FSConstants;
import filesystem.FileSystem;
import filesystem.Page;
import features.strategy.FeatureStrategy;

public final class TokensFeature implements FeatureStrategy {
    private final ActionInput action;

    public TokensFeature(final ActionInput action) {
        this.action = action;
    }

    /**
     * Changes the balance and current tokens of the user.
     * @return true if the operation was successful else false
     */
    private Boolean buyTokens() {
        FileSystem instance = FileSystem.getInstance();
        UserInput currentUser = instance.getCurrentUser();
        Page current = instance.getCurrent();

        if (!current.getName().equals(FSConstants.UPGRADES_PAGE)) {
            return false;
        }

        int count = Integer.parseInt(action.getCount());
        currentUser.getCredentials().setBalance(
                Integer.toString(Integer.parseInt(
                        currentUser.getCredentials().getBalance()
                ) - count)
        );

        currentUser.setTokensCount(
                currentUser.getTokensCount() + count
        );

        return true;
    }


    @Override
    public boolean action() {
        return buyTokens();
    }
}
