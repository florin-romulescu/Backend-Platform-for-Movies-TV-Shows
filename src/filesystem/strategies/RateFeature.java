package filesystem.strategies;

import fileio.ActionInput;
import filesystem.FSActions;
import filesystem.FileSystem;

public class RateFeature implements FeatureStrategy {
    private final ActionInput action;

    public RateFeature(ActionInput action) {
        this.action = action;
    }

    @Override
    public boolean action() {
        FileSystem instance = FileSystem.getInstance();
        return FSActions.rateMovie(instance.getCurrentMovie(), action);
    }
}
