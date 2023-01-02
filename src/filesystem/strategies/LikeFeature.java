package filesystem.strategies;

import filesystem.FSActions;
import filesystem.FileSystem;

public class LikeFeature implements FeatureStrategy {

    @Override
    public boolean action() {
        FileSystem instance = FileSystem.getInstance();
        return FSActions.likeMovie(instance.getCurrentMovie());
    }
}
