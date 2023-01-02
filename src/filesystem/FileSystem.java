package filesystem;

import fileio.ActionInput;
import fileio.MovieInput;
import fileio.UserInput;

import java.util.ArrayList;
import java.util.List;

public final class FileSystem {
    private FileSystem() {
        // -------------------------------- //

       unAuthPage = new Page("homepage neautentificat");
       Page unAuthLoginPage = new Page("login");
       unAuthLoginPage.getPermissions().add(FSConstants.LOGIN_PERMISSION);
       Page unAuthRegisterPage = new Page("register");
       unAuthRegisterPage.getPermissions().add(FSConstants.REGISTER_PERMISSION);
       unAuthPage.getChildren().add(unAuthLoginPage);
       unAuthPage.getChildren().add(unAuthRegisterPage);

        // -------------------------------- //

        authPage = new Page("homepage autentificat");
        Page authMoviesPage = new Page("movies");
        Page authMoviesSeeDetails = new Page("see details");
        Page authLogout = new Page("logout");
        Page authUpgradesPage = new Page("upgrades");

        authPage.getChildren().add(authMoviesPage);
        authPage.getChildren().add(authUpgradesPage);
        authPage.getChildren().add(authLogout);

        authMoviesPage.getChildren().add(authPage);
        authMoviesPage.getChildren().add(authMoviesSeeDetails);
        authMoviesPage.getChildren().add(authLogout);
        authMoviesPage.getChildren().add(authMoviesPage);
        authMoviesPage.getPermissions().add(FSConstants.SEARCH_PERMISSION);
        authMoviesPage.getPermissions().add(FSConstants.FILTER_PERMISSION);

        authMoviesSeeDetails.getChildren().add(authPage);
        authMoviesSeeDetails.getChildren().add(authMoviesPage);
        authMoviesSeeDetails.getChildren().add(authUpgradesPage);
        authMoviesSeeDetails.getChildren().add(authLogout);
        authMoviesSeeDetails.getChildren().add(authMoviesSeeDetails);
        authMoviesSeeDetails.getPermissions().add(FSConstants.PURCHASE_PERMISSION);
        authMoviesSeeDetails.getPermissions().add(FSConstants.WATCH_PERMISSION);
        authMoviesSeeDetails.getPermissions().add(FSConstants.LIKE_PERMISSION);
        authMoviesSeeDetails.getPermissions().add(FSConstants.RATE_PERMISSION);

        authUpgradesPage.getChildren().add(authPage);
        authUpgradesPage.getChildren().add(authMoviesPage);
        authUpgradesPage.getChildren().add(authLogout);
        authUpgradesPage.getChildren().add(authUpgradesPage);
        authUpgradesPage.getPermissions().add(FSConstants.PREMIUM_PERMISSION);
        authUpgradesPage.getPermissions().add(FSConstants.TOKENS_PERMISSION);

//        authLogout.getChildren().add(unAuthPage);
        // -------------------------------- //
        current = unAuthPage;
    }
    private static FileSystem instance = null;
    private final Page unAuthPage;
    private final Page authPage;
    private Page current;
    private UserInput currentUser = null;
    private List<MovieInput> currentMovies = null;
    private MovieInput currentMovie = null;
    private  List<MovieInput> allMovies = null;
    private boolean visited = false;

    /**
     * Singleton pattern initialisation.
     * @return the instance of this class
     */
    public static FileSystem getInstance() {
        if (instance == null) {
            instance = new FileSystem();
        }
        return instance;
    }

    public Page getCurrent() {
        return current;
    }

    public void setCurrent(final Page current) {
        this.current = current;
    }

    public Page getUnAuthPage() {
        return unAuthPage;
    }

    public Page getAuthPage() {
        return authPage;
    }

    public UserInput getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(final UserInput currentUser) {
        this.currentUser = currentUser;
    }

    public List<MovieInput> getCurrentMovies() {
        return currentMovies;
    }

    public void setCurrentMovies(final List<MovieInput> currentMovies) {
        this.currentMovies = currentMovies;
    }

    public MovieInput getCurrentMovie() {
        return currentMovie;
    }

    public void setCurrentMovie(final MovieInput currentMovie) {
        this.currentMovie = currentMovie;
    }

    public List<MovieInput> getAllMovies() {
        return allMovies;
    }

    public void setAllMovies(final List<MovieInput> allMovies) {
        this.allMovies = allMovies;
    }

    /**
     * Init the current movies with all the movies that the current
     * user can watch based on its country.
     * @param action the current action
     */
    public void initCurrentMovies(final ActionInput action) {
        if (!current.getName().equals(FSConstants.SEE_DETAILS_PAGE)) {
            currentMovies = new ArrayList<>();
        }

        if (current.getName().equals("movies")) {
            visited = true;
            currentMovies = MovieInput.getUserMovies(currentUser, allMovies);
        } else {
            visited = false;
        }

        if (current.getName().equals(FSConstants.SEE_DETAILS_PAGE)) {
            if (action.getMovie() != null) {
                for (MovieInput movie : currentMovies) {
                    if (movie.getName().equals(action.getMovie())) {
                        currentMovie = movie;
                        break;
                    }
                }
            }
            if (currentMovie != null) {
                currentMovies.clear();
                currentMovies.add(currentMovie);
            }
        } else {
            currentMovie = null;
        }
    }

    /**
     * Set the instance null for reinitialisation.
     */
    public static void setInstanceNull() {
        instance = null;
    }
}
