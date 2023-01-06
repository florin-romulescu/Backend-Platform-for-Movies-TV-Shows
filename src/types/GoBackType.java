package types;

import filesystem.FileSystem;
import types.strategy.TypeStrategy;

public final class GoBackType implements TypeStrategy {

    private static boolean goBack() {
        FileSystem instance = FileSystem.getInstance();
        if (instance.getStackList().isEmpty()) {
            return false;
        }
        if (instance.getCurrentUser() == null) {
            return false;
        }
        instance.setCurrent(instance.getStackList().pop());
        return true;
    }

    @Override
    public boolean action() {
        return goBack();
    }
}
