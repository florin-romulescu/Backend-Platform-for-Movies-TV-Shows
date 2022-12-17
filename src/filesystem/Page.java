package filesystem;

import java.util.ArrayList;

public final class Page {

    private final String name;
    private ArrayList<Page> children = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();

    public Page(final String name) {
        this.name = name;
    }

    public ArrayList<Page> getChildren() {
        return children;
    }

    public void setChildren(final ArrayList<Page> children) {
        this.children = children;
    }

    public ArrayList<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(final ArrayList<String> permissions) {
        this.permissions = permissions;
    }

    public String getName() {
        return name;
    }

    /**
     * Verify if this page has the given permission.
     * @param p the given permission
     * @return true if the page has the permission else false
     */
    public boolean permissionExists(final String p) {
        for (String permission: permissions) {
            if (permission.equals(p)) {
                return true;
            }
        }
        return false;
    }

}
