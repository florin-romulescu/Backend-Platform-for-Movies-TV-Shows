package types;

import features.strategy.FeatureBuilder;
import features.strategy.FeatureContext;
import fileio.ActionInput;
import types.strategy.TypeStrategy;
import filesystem.FSConstants;

public class DatabaseType implements TypeStrategy {
    ActionInput action;

    public DatabaseType(ActionInput action) {
        this.action = action;
    }

    @Override
    public boolean action() {
        FeatureContext featureContext = new FeatureBuilder()
                .action(action)
                .movie(action.getAddedMovie())
                .build();
        featureContext.createStrategy();
        return featureContext.action();
    }
}
