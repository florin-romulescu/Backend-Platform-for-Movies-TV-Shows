package database;

import fileio.MovieInput;
import fileio.Notification;

import java.util.ArrayList;
import java.util.List;

public final class NotificationService {
    private final List<UserListener> listeners;
    private final UserListener all;

    public NotificationService() {
        listeners = new ArrayList<>();
        all = new UserListener("All");
        all.setUsers(Database.getInstance().getUsers());
    }

    /**
     * Adds a new listener to the listeners list.
     * @param listener the new added listener
     */
    public void subscribe(final UserListener listener) {
        listeners.add(listener);
    }

    /**
     * Removes a listener from the listeners list
     * @param listener the removed listener
     */
    public void unsubscribe(final UserListener listener) {
        listeners.remove(listener);
    }

    public List<UserListener> getListeners() {
        return listeners;
    }

    /**
     * Loops through the listeners and calls
     * the update method for each one.
     * @param msg the message attribute of the notification
     * @param genre the message attribute of the notification
     */
    public void notifyUsers(final Notification msg,
                            final String genre) {
        for (UserListener listener: listeners) {
            listener.update(msg, genre);
        }
    }

    /**
     * Calls the update method for all listener.
     * @param msg the message attribute of the notification
     * @param movie the movieTitle attribute of the notification
     */
    public void notifyUsers(final Notification msg,
                            final MovieInput movie) {
        all.update(msg, movie);
    }
}
