package filesystem;

import java.util.ArrayList;

public class Page {

    private final String name;
    private ArrayList<Page> children = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();

    public Page(String name) {
        this.name = name;
    }

    public ArrayList<Page> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<Page> children) {
        this.children = children;
    }

    public ArrayList<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(ArrayList<String> permissions) {
        this.permissions = permissions;
    }

    public String getName() {
        return name;
    }

    public boolean permissionExists(String p) {
        for (String permission: permissions) {
            if (permission.equals(p)) {
                return true;
            }
        }
        return false;
    }

    public boolean pageExists(String title) {
        for (Page page: children) {
            if (page.name.equals(title))
                return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Page{" +
                "name=" + name +
                ", permissions=" + permissions +
                '}';
    }
}
