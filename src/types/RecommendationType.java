package types;

import features.strategy.FeatureBuilder;
import features.strategy.FeatureContext;
import fileio.ActionInput;
import types.strategy.TypeStrategy;

public final class RecommendationType implements TypeStrategy {
    private final ActionInput action;

    public RecommendationType(final ActionInput action) {
        this.action = action;
    }

    @Override
    public boolean action() {
        FeatureContext featureContext = new FeatureBuilder()
                .action(action)
                .build();
        featureContext.createStrategy();
        return featureContext.action();
    }
}
