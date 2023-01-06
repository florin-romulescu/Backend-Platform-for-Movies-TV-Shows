package types;

import features.strategy.FeatureBuilder;
import features.strategy.FeatureContext;
import fileio.ActionInput;
import types.strategy.TypeStrategy;

public final class DatabaseType implements TypeStrategy {
    private final ActionInput action;

    public DatabaseType(final ActionInput action) {
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
