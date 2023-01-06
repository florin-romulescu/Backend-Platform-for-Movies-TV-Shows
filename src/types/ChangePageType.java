package types;

import fileio.ActionInput;
import fileio.MovieInput;
import filesystem.FSConstants;
import filesystem.FileSystem;
import filesystem.Page;
import types.strategy.TypeStrategy;

public final class ChangePageType implements TypeStrategy {
    private final ActionInput action;

    public ChangePageType(final ActionInput action) {
        this.action = action;
    }

    /**
     * Change the current page to one of its children.
     * @return true if operation was successful else false
     */
    private Boolean changePage() {
        FileSystem instance = FileSystem.getInstance();
        Page current = instance.getCurrent();

        String pageTitle = action.getPage();
        for (Page page: current.getChildren()) {
            if (page.getName().equals(pageTitle)) {
                if (pageTitle.equals(FSConstants.LOGOUT_PAGE)) {
                    instance.setCurrent(instance.getUnAuthPage());
                    instance.setCurrentUser(null);
                    instance.getStackList().clear();
                    return true;
                } else if (pageTitle.equals(FSConstants.SEE_DETAILS_PAGE)) {
                    boolean found = false;
                    for (MovieInput movie: instance.getCurrentMovies()) {
                        if (movie.getName().equals(action.getMovie())) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        return false;
                    }
                }
                if (instance.getCurrentUser() != null) {
                    instance.getStackList().push(instance.getCurrent());
                }
                instance.setCurrent(page);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean action() {
        return changePage();
    }
}
