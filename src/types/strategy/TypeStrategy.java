package types.strategy;

public interface TypeStrategy {
    /**
     * Runs the corresponding action for the type object.
     * @return true if the operation was successful
     * else false
     */
    boolean action();
}
