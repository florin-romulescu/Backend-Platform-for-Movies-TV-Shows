package database;

import fileio.ActionInput;
import fileio.MovieInput;
import fileio.UserInput;
import filesystem.FSConstants;
import filesystem.FileSystem;
import filesystem.Page;

import java.util.ArrayList;
import java.util.List;

public class Database {
    private final NotificationService notificationService;
    private static Database instance = null;
    private FileSystem fileSystem;
    private List<UserInput> users;
    private List<MovieInput> movies;
    private boolean display;
    private boolean moviesChangeable;

    private Database() {
        notificationService = new NotificationService();
        FileSystem.setInstanceNull();
        fileSystem = FileSystem.getInstance();
        users = new ArrayList<>();
        movies = new ArrayList<>();
        display = false;
        moviesChangeable = true;
    }

    public List<UserInput> getUsers() {
        return users;
    }

    public void setUsers(List<UserInput> users) {
        this.users = users;
    }

    public List<MovieInput> getMovies() {
        return movies;
    }

    public void setMovies(List<MovieInput> movies) {
        this.movies = movies;
    }

    public FileSystem getFileSystem() {
        return fileSystem;
    }

    public boolean isDisplay() {
        return display;
    }

    public void setDisplay(boolean display) {
        this.display = display;
    }

    public boolean isMoviesChangeable() {
        return moviesChangeable;
    }

    public void setMoviesChangeable(boolean moviesChangeable) {
        this.moviesChangeable = moviesChangeable;
    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public static void setInstanceNull() {
        instance = null;
    }

    public NotificationService getNotificationService() {
        return notificationService;
    }

    public void newMovieNotifier(String msg) {
        notificationService.notifyUsers(msg);
    }
}
