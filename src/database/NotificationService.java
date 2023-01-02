package database;

import java.util.ArrayList;
import java.util.List;

public class NotificationService {
    private final List<UserListener> users;

    public NotificationService() {
        users = new ArrayList<>();
    }

    public void subscribe(UserListener listener) {
        users.add(listener);
    }

    public void unsubscribe(UserListener listener) {
        users.remove(listener);
    }

    public void notifyUsers(String msg) {
        users.forEach(listener -> listener.update(msg));
    }
}
