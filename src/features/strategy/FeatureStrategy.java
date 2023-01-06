package features.strategy;

public interface FeatureStrategy {
    /**
     * Runs the corresponding action for the feature object.
     * @return true if the operation was successful
     * else false
     */
    boolean action();
}
