package filesystem.strategies;

import filesystem.FSActions;
import filesystem.FileSystem;

public class PurchaseFeature implements FeatureStrategy {
    @Override
    public boolean action() {
        FileSystem instance = FileSystem.getInstance();
        return FSActions.purchaseMovie(instance.getCurrentMovie());
    }
}
