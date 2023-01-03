package features;

import fileio.UserInput;
import filesystem.FSConstants;
import filesystem.FileSystem;
import filesystem.Page;
import features.strategy.FeatureStrategy;

public class PremiumFeature implements FeatureStrategy {

    /**
     * Change the account type of the user.
     * @return true if the operation was successful else false
     */
    private Boolean buyPremiumAccount() {
        FileSystem instance = FileSystem.getInstance();
        Page current = instance.getCurrent();

        if (!current.getName().equals(FSConstants.UPGRADES_PAGE)) {
            return false;
        }

        UserInput currentUser = instance.getCurrentUser();

        if (currentUser.getCredentials().getAccountType().equals("premium")) {
            return false;
        }
        int count = 10;
        // TODO change tokens count not balance
        currentUser.setTokensCount(
                currentUser.getTokensCount() - count
        );

        currentUser.getCredentials().setAccountType("premium");

        return true;
    }
    @Override
    public boolean action() {
        return buyPremiumAccount();
    }
}
