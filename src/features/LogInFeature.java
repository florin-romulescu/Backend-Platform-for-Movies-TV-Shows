package features;

import fileio.ActionInput;
import fileio.UserInput;
import filesystem.FSConstants;
import filesystem.FileSystem;
import filesystem.Page;
import features.strategy.FeatureStrategy;

import java.util.List;

public final class LogInFeature implements FeatureStrategy {
    private final List<UserInput> users;
    private final ActionInput action;

    public LogInFeature(final List<UserInput> users,
                        final ActionInput action) {
        this.users = users;
        this.action = action;
    }

    /**
     * Login in the given page.
     * @return true if the operation was successful else false
     */
    private Boolean login() {
        FileSystem instance = FileSystem.getInstance();
        Page current = instance.getCurrent();

        if (!current.permissionExists(FSConstants.LOGIN_PERMISSION)) {
            return false;
        }
        if (!action.getFeature().equals(FSConstants.LOGIN_PERMISSION)) {
            return false;
        }

        for (UserInput user: users) {
            if (user.exists(action.getCredentials().getName(),
                    action.getCredentials().getPassword())) {
                instance.setCurrent(instance.getAuthPage());
                instance.setCurrentUser(user);
                instance.getStackList().clear();
                return true;
            }
        }
        instance.setCurrent(instance.getUnAuthPage());
        return  false;
    }

    @Override
    public boolean action() {
        return login();
    }
}
