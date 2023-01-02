package filesystem.strategies;

import fileio.ActionInput;
import filesystem.FSActions;

public class TokensFeature implements FeatureStrategy {
    final private ActionInput action;

    public TokensFeature(ActionInput action) {
        this.action = action;
    }

    @Override
    public boolean action() {
        return FSActions.buyTokens(action);
    }
}
