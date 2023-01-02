package filesystem.strategies;

import fileio.ActionInput;
import filesystem.FSActions;

public class ChangePageType implements TypeStrategy{
    private final ActionInput action;

    public ChangePageType(ActionInput action) {
        this.action = action;
    }

    @Override
    public boolean action() {
        return FSActions.changePage(action);
    }
}
