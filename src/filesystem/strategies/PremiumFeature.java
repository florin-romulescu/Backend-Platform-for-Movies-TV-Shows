package filesystem.strategies;

import fileio.ActionInput;
import filesystem.FSActions;

public class PremiumFeature implements FeatureStrategy {
    @Override
    public boolean action() {
        return FSActions.buyPremiumAccount();
    }
}
