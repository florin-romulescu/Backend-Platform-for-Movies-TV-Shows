package database;

import fileio.MovieInput;
import fileio.Notification;

import java.util.ArrayList;
import java.util.List;

public class NotificationService {
    private final List<UserListener> listeners;
    private final UserListener all;

    public NotificationService() {
        listeners = new ArrayList<>();
        all = new UserListener("All");
        all.setUsers(Database.getInstance().getUsers());
    }

    public void subscribe(UserListener listener) {
        listeners.add(listener);
    }

    public void unsubscribe(UserListener listener) {
        listeners.remove(listener);
    }

    public List<UserListener> getListeners() {
        return listeners;
    }

    public void notifyUsers(Notification msg, String genre) {
        for (UserListener listener: listeners) {
            listener.update(msg, genre);
        }
    }

    public void notifyUsers(Notification msg, MovieInput movie) {
        all.update(msg, movie);
    }
}
