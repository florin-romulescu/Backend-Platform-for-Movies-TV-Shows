package filesystem;

import fileio.UserInput;

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
        authMoviesPage.getPermissions().add(FSConstants.searchPermission);
        authMoviesPage.getPermissions().add(FSConstants.filterPermission);

        authMoviesSeeDetails.getChildren().add(authPage);
        authMoviesSeeDetails.getChildren().add(authMoviesPage);
        authMoviesSeeDetails.getChildren().add(authUpgradesPage);
        authMoviesSeeDetails.getChildren().add(authLogout);
        authMoviesSeeDetails.getPermissions().add(FSConstants.purchasePermission);
        authMoviesSeeDetails.getPermissions().add(FSConstants.watchPermission);
        authMoviesSeeDetails.getPermissions().add(FSConstants.likePermission);
        authMoviesSeeDetails.getPermissions().add(FSConstants.ratePermission);

        authUpgradesPage.getChildren().add(authPage);
        authUpgradesPage.getChildren().add(authMoviesPage);
        authUpgradesPage.getChildren().add(authLogout);
        authUpgradesPage.getPermissions().add(FSConstants.premiumPermission);
        authUpgradesPage.getPermissions().add(FSConstants.tokensPermission);

//        authLogout.getChildren().add(unAuthPage);
        // -------------------------------- //
        current = unAuthPage;
    }
    private static FileSystem instance = null;
    private Page unAuthPage;
    private Page authPage;
    private Page current;
    private UserInput currentUser = null;

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
}
