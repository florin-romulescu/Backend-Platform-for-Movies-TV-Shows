package filesystem.strategies;

import fileio.ActionInput;
import fileio.UserInput;
import filesystem.FSActions;

import java.util.List;

public class LogInFeature implements FeatureStrategy{
    private List<UserInput> users;
    private ActionInput action;

    public LogInFeature(List<UserInput> users, ActionInput action) {
        this.users = users;
        this.action = action;
    }

    @Override
    public boolean action() {
        return FSActions.login(users, action);
    }
}
