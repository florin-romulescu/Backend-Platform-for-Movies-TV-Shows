package database;

import fileio.MovieInput;
import fileio.Notification;
import fileio.UserInput;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class UserListener {
    private final String name;
    private List<UserInput> users;

    public UserListener(final String name) {
        this.name = name;
        users = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<UserInput> getUsers() {
        return users;
    }

    public void setUsers(final List<UserInput> users) {
        this.users = users;
    }

    /**
     * Adds the given notification to the user
     * notifications attribute if the current genre
     * corresponds with the name of this listener.
     * @param msg the message attribute of the notification
     * @param genre the movieTitle attribute of the notification
     */
    public void update(final Notification msg,
                       final String genre) {
        if (!genre.equals(name)) {
            return;
        }
        for (UserInput user: users) {
            boolean exists = false;
            for (Notification notification: user.getNotifications()) {
                if (notification.equals(msg)) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                user.getNotifications().add(msg);
            }
        }
    }

    /**
     * Adds the given notification to the user
     * notifications attribute if the current movie
     * was purchased by the user.
     * @param msg the message attribute of the notification
     * @param movie the movieTitle attribute of the notification
     */
    public void update(final Notification msg,
                       final MovieInput movie) {
        for (UserInput user: users) {
            for (MovieInput movieInput: user.getPurchasedMovies()) {
                if (movie.equals(movieInput)) {
                    user.getNotifications().add(msg);
                    break;
                }
            }
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserListener listener = (UserListener) o;
        return Objects.equals(name, listener.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
