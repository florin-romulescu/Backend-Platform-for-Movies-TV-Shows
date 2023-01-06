package database;

import fileio.MovieInput;
import fileio.Notification;
import fileio.UserInput;
import filesystem.FileSystem;
import java.util.ArrayList;
import java.util.List;

public final class Database {
    private NotificationService notificationService;
    private static Database instance = null;
    private final FileSystem fileSystem;
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

    public void setUsers(final List<UserInput> users) {
        this.users = users;
    }

    public List<MovieInput> getMovies() {
        return movies;
    }

    public void setMovies(final List<MovieInput> movies) {
        this.movies = movies;
    }

    public FileSystem getFileSystem() {
        return fileSystem;
    }

    public boolean isDisplay() {
        return display;
    }

    public void setDisplay(final boolean display) {
        this.display = display;
    }

    public boolean isMoviesChangeable() {
        return moviesChangeable;
    }

    public void setMoviesChangeable(final boolean moviesChangeable) {
        this.moviesChangeable = moviesChangeable;
    }

    /**
     * Get the instance of the class. This instance
     * is unique since the class uses a singleton pattern.
     * @return the instance
     */
    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    /**
     * Set the instance null for reinitialisation
     * purposes.
     */
    public static void setInstanceNull() {
        instance = null;
    }

    /**
     * Initialise the notificationService attribute
     * with a new object.
     */
    public void createNotificationService() {
        notificationService = new NotificationService();
    }

    public NotificationService getNotificationService() {
        return notificationService;
    }

    /**
     * Calls the notificationService.notifyUsers method
     * to send a notification to the users that are subscribed
     * to the given genre.
     * @param msg the message attribute of the notification
     * @param genre the movieName attribute of the notification
     */
    public void newMovieNotifier(final Notification msg,
                                 final String genre) {
        notificationService.notifyUsers(msg, genre);
    }

    /**
     * Calls the notificationService.notifyUsers method
     * to send a notification to all users.
     * @param msg the message attribute of the notification
     * @param movie the movieName attribute of the notification
     */
    public void newMovieNotifier(final Notification msg,
                                 final MovieInput movie) {
        notificationService.notifyUsers(msg, movie);
    }

    /**
     * This method returns a MovieInput object
     * that has the given title.
     * @param m the title of the movie
     * @return a MovieInput object
     */
    public MovieInput getMovie(final String m) {
        for (MovieInput movie: movies) {
            if (m.equals(movie.getName())) {
                return movie;
            }
        }
        return null;
    }

    /**
     * This method returns a UserInput object
     * that has the given title.
     * @param u the name of the user
     * @return a MovieInput object
     */
    public UserInput getUser(final String u) {
        for (UserInput user: users) {
            if (u.equals(user.getCredentials().getName())) {
                return user;
            }
        }
        return null;
    }
}
