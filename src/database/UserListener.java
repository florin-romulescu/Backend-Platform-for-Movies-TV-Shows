package database;

import fileio.UserInput;

public class UserListener {
    private final UserInput user;

    public UserListener(UserInput user) {
        this.user = user;
    }

    public void update(String msg) {
        user.getNotifications().add(msg);
    }
}
