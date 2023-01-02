package filesystem.strategies;

import filesystem.FSActions;
import filesystem.FileSystem;

import java.io.File;

public class WatchFeature implements FeatureStrategy {
    @Override
    public boolean action() {
        FileSystem instance = FileSystem.getInstance();
        return FSActions.watchMovie(instance.getCurrentMovie());
    }
}
