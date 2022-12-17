package filesystem;

import fileio.ActionInput;
import fileio.MovieInput;
import fileio.UserInput;

import java.util.ArrayList;
import java.util.List;

public class FileSystem {
    private FileSystem() {
        // -------------------------------- //

       unAuthPage = new Page("homepage neautentificat");
       Page unAuthLoginPage = new Page("login");
       unAuthLoginPage.getPermissions().add(FSConstants.loginPermission);
       Page unAuthRegisterPage = new Page("register");
       unAuthRegisterPage.getPermissions().add(FSConstants.registerPermission);
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
        authMoviesPage.getPermissions().add(FSConstants.searchPermission);
        authMoviesPage.getPermissions().add(FSConstants.filterPermission);

        authMoviesSeeDetails.getChildren().add(authPage);
        authMoviesSeeDetails.getChildren().add(authMoviesPage);
        authMoviesSeeDetails.getChildren().add(authUpgradesPage);
        authMoviesSeeDetails.getChildren().add(authLogout);
        authMoviesSeeDetails.getChildren().add(authMoviesSeeDetails);
        authMoviesSeeDetails.getPermissions().add(FSConstants.purchasePermission);
        authMoviesSeeDetails.getPermissions().add(FSConstants.watchPermission);
        authMoviesSeeDetails.getPermissions().add(FSConstants.likePermission);
        authMoviesSeeDetails.getPermissions().add(FSConstants.ratePermission);

        authUpgradesPage.getChildren().add(authPage);
        authUpgradesPage.getChildren().add(authMoviesPage);
        authUpgradesPage.getChildren().add(authLogout);
        authUpgradesPage.getChildren().add(authUpgradesPage);
        authUpgradesPage.getPermissions().add(FSConstants.premiumPermission);
        authUpgradesPage.getPermissions().add(FSConstants.tokensPermission);

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

    public static FileSystem getInstance() {
        if (instance == null) {
            instance = new FileSystem();
        }
        return instance;
    }

    public Page getCurrent() {
        return current;
    }

    public void setCurrent(Page current) {
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

    public void setCurrentUser(UserInput currentUser) {
        this.currentUser = currentUser;
    }

    public List<MovieInput> getCurrentMovies() {
        return currentMovies;
    }

    public void setCurrentMovies(List<MovieInput> currentMovies) {
        this.currentMovies = currentMovies;
    }

    public MovieInput getCurrentMovie() {
        return currentMovie;
    }

    public void setCurrentMovie(MovieInput currentMovie) {
        this.currentMovie = currentMovie;
    }

    public List<MovieInput> getAllMovies() {
        return allMovies;
    }

    public void setAllMovies(List<MovieInput> allMovies) {
        this.allMovies = allMovies;
    }

    public Boolean initCurrentMovies(ActionInput action) {
        if (!current.getName().equals(FSConstants.seeDetailsPage)) {
            currentMovies = new ArrayList<>();
        }

        if (current.getName().equals("movies")) {
            visited = true;
            currentMovies = MovieInput.getUserMovies(currentUser, allMovies);
        } else {
            visited = false;
        }

        if (current.getName().equals(FSConstants.seeDetailsPage)) {
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
        return true;
    }

    public static void init() {
        instance = null;
    }
}
