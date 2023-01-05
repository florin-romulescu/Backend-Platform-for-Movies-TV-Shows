package database;

import fileio.ActionInput;
import fileio.MovieInput;
import fileio.Notification;
import fileio.UserInput;
import filesystem.FSConstants;
import filesystem.FileSystem;
import filesystem.Page;

import java.util.ArrayList;
import java.util.List;

public class Database {
    private NotificationService notificationService;
    private static Database instance = null;
    private FileSystem fileSystem;
    private List<UserInput> users;
    private List<MovieInput> movies;
    private boolean display;
    private boolean moviesChangeable;

    private Database() {
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

    public void createNotificationService() {
        notificationService = new NotificationService();
    }

    public NotificationService getNotificationService() {
        return notificationService;
    }

    public void newMovieNotifier(Notification msg, String genre) {
        notificationService.notifyUsers(msg, genre);
    }

    public void newMovieNotifier(Notification msg, MovieInput movie) {
        notificationService.notifyUsers(msg, movie);
    }

    public MovieInput getMovie(String m) {
        for (MovieInput movie: movies) {
            if (m.equals(movie.getName())) {
                return movie;
            }
        }
        return null;
    }

    public UserInput getUser(String u) {
        for (UserInput user: users) {
            if (u.equals(user.getCredentials().getName())) {
                return user;
            }
        }
        return null;
    }
}
