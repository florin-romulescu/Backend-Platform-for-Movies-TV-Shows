package database;

import fileio.MovieInput;
import fileio.Notification;
import fileio.UserInput;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserListener {
    private final String name;
    private List<UserInput> users;

    public UserListener(String name) {
        this.name = name;
        users = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<UserInput> getUsers() {
        return users;
    }

    public void setUsers(List<UserInput> users) {
        this.users = users;
    }

    public void update(Notification msg, String genre) {
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

    public void update(Notification msg, MovieInput movie) {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserListener listener = (UserListener) o;
        return Objects.equals(name, listener.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
