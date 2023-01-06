package features;

import fileio.ActionInput;
import fileio.UserInput;
import filesystem.FSConstants;
import filesystem.FileSystem;
import filesystem.Page;
import features.strategy.FeatureStrategy;

import java.util.List;

public final class RegisterFeature implements FeatureStrategy {
    private final List<UserInput> users;
    private final ActionInput action;

    public RegisterFeature(final List<UserInput> users,
                           final ActionInput action) {
        this.users = users;
        this.action = action;
    }

    /**
     *  Register a new user and authenticate it.
     * @return true if the operation was successful else false
     */
    private Boolean register() {
        FileSystem instance = FileSystem.getInstance();
        Page current = instance.getCurrent();

        if (!current.permissionExists(FSConstants.REGISTER_PERMISSION)) {
            return false;
        }
        if (!action.getFeature().equals(FSConstants.REGISTER_PERMISSION)) {
            instance.setCurrent(instance.getUnAuthPage());
            return false;
        }

        for (UserInput user: users) {
            if (user.getCredentials().getName().equals(action.getCredentials().getName())
                    && user.getCredentials().getPassword()
                    .equals(action.getCredentials().getPassword())) {
                return false;
            }
        }

        UserInput user = new UserInput(action.getCredentials());
        users.add(user);

        instance.setCurrent(instance.getAuthPage());
        instance.setCurrentUser(user);
        instance.getStackList().clear();
        return true;
    }

    @Override
    public boolean action() {
        return register();
    }
}
