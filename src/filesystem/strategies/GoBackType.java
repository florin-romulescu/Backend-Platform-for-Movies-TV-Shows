package filesystem.strategies;

import filesystem.FileSystem;

public class GoBackType implements TypeStrategy {

    private static boolean go_back() {
        FileSystem instance = FileSystem.getInstance();
        if (instance.getStackList().isEmpty()) {
            return false;
        }
        instance.setCurrent(instance.getStackList().pop());
        return true;
    }

    @Override
    public boolean action() {
        return go_back();
    }
}
