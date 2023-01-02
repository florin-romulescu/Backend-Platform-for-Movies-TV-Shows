package filesystem.strategies;

import fileio.ActionInput;
import fileio.UserInput;
import filesystem.FSActions;

import java.util.List;

public class RegisterFeature implements FeatureStrategy {
    List<UserInput> users;
    ActionInput action;

    public RegisterFeature(List<UserInput> users, ActionInput action) {
        this.users = users;
        this.action = action;
    }

    @Override
    public boolean action() {
        return FSActions.register(users, action);
    }
}
