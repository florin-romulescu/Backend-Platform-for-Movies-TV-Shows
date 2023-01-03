package filesystem;

import database.Database;
import fileio.ActionInput;

import fileio.MovieInput;
import fileio.UserInput;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class FSActions {
    private FSActions() {

    }

    /**
     * Change the current page to one of its children.
     * @param action the given action
     * @return true if operation was successful else false
     */
    public static Boolean changePage(final ActionInput action) {
        FileSystem instance = FileSystem.getInstance();
        Page current = instance.getCurrent();

        String pageTitle = action.getPage();
        for (Page page: current.getChildren()) {
            if (page.getName().equals(pageTitle)) {
                if (pageTitle.equals(FSConstants.LOGOUT_PAGE)) {
                    instance.setCurrent(instance.getUnAuthPage());
                    instance.setCurrentUser(null);
                    return true;
                } else if (pageTitle.equals(FSConstants.SEE_DETAILS_PAGE)) {
                    boolean found = false;
                    for (MovieInput movie: instance.getCurrentMovies()) {
                        if (movie.getName().equals(action.getMovie())) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        return false;
                    }
                }
                instance.setCurrent(page);
                return true;
            }
        }
        return false;
    }

    /**
     * Login in the given page.
     * @param users the input users
     * @param action the given action
     * @return true if the operation was successful else false
     */
    public static Boolean login(final List<UserInput> users,
                                final ActionInput action) {
        FileSystem instance = FileSystem.getInstance();
        Page current = instance.getCurrent();

        if (!current.permissionExists(FSConstants.LOGIN_PERMISSION)) {
            return false;
        }
        if (!action.getFeature().equals(FSConstants.LOGIN_PERMISSION)) {
            return false;
        }

        for (UserInput user: users) {
            if (user.exists(action.getCredentials().getName(),
                    action.getCredentials().getPassword())) {
                instance.setCurrent(instance.getAuthPage());
                instance.setCurrentUser(user);
                return true;
            }
        }
        instance.setCurrent(instance.getUnAuthPage());
        return  false;
    }

    /**
     *  Register a new user and authenticate it.
     * @param users all users
     * @param action the given action
     * @return true if the operation was successful else false
     */
    public static Boolean register(final List<UserInput> users,
                                   final ActionInput action) {
        FileSystem instance = FileSystem.getInstance();
        Page current = instance.getCurrent();

        if (!current.permissionExists(FSConstants.REGISTER_PERMISSION)) {
            return false;
        }
        if (!action.getFeature().equals(FSConstants.REGISTER_PERMISSION)) {
            instance.setCurrent(instance.getUnAuthPage());
            return false;
        }

        for (UserInput user: users) {
            if (user.getCredentials().getName().equals(action.getCredentials().getName())
                && user.getCredentials().getPassword()
                    .equals(action.getCredentials().getPassword())) {
                return false;
            }
        }

        UserInput user = new UserInput(action.getCredentials());
        users.add(user);

        instance.setCurrent(instance.getAuthPage());
        instance.setCurrentUser(user);
        return true;
    }

    /**
     * Search through the movie list. All the movies that starts with the given search input
     * are stored in the movies variable.
     * @param movies stocks all the movies that starts with the given search input
     * @param currentMovies all input movies
     * @param action the current action
     * @return true if the operation was successful else false
     */
    public static Boolean search(final List<MovieInput> movies,
                                 final List<MovieInput> currentMovies,
                                 final ActionInput action) {
        FileSystem instance = FileSystem.getInstance();
        Page current = instance.getCurrent();
        movies.clear();

        if (!current.getName().equals(FSConstants.MOVIES_PAGE)) {
            return false;
        }

        for (MovieInput movie: currentMovies) {
            if (movie.getName().startsWith(action.getStartsWith())) {
                movies.add(movie);
            }
        }

        return true;
    }

    /**
     * Filter the list of movies based on actors, genre, rate and duration.
     * @param movies the final result
     * @param currentMovies the input list
     * @param action the current action
     * @return true if the operation was successful else false
     */
    public static Boolean filter(final List<MovieInput> movies,
                                 final List<MovieInput> currentMovies,
                                 final ActionInput action) {
        FileSystem instance = FileSystem.getInstance();
        Page current = instance.getCurrent();

        if (!current.getName().equals(FSConstants.MOVIES_PAGE)) {
            return false;
        }

        movies.clear();

        if (action.getFilters().getContains() != null) {
            for (MovieInput movie: currentMovies) {
                boolean add = true;
                if (action.getFilters().getContains().getGenre() != null) {
                    if (!movie.getGenres().containsAll(
                            action.getFilters().getContains().getGenre())) {
                        add = false;
                    }
                }
                if (action.getFilters().getContains().getActors() != null) {
                    if (!movie.getActors().containsAll(
                            action.getFilters().getContains().getActors())) {
                        add = false;
                    }
                }
                if (add) {
                    movies.add(movie);
                }
            }
        } else {
            movies.addAll(currentMovies);
        }

        if (action.getFilters().getSort() != null) {
            if (action.getFilters().getSort().getDuration() != null) {
                for (int i = 0; i < movies.size() - 1; ++i) {
                    for (int j = i + 1; j < movies.size(); ++j) {
                        if (action.getFilters().getSort().getDuration().equals("increasing")) {
                            if (movies.get(i).getDuration() > movies.get(j).getDuration()) { // greater
                                Collections.swap(movies, i, j);
                            } else if (Objects.equals(movies.get(i).getDuration(), movies.get(j).getDuration())) { // equals
                                if (action.getFilters().getSort().getRating() != null) {
                                    if (action.getFilters().getSort().getRating().equals("increasing")) {
                                        if (movies.get(i).getRating() > movies.get(j).getRating()) {
                                            Collections.swap(movies, i, j);
                                        }
                                    } else {
                                        if (movies.get(i).getRating() < movies.get(j).getRating()) {
                                            Collections.swap(movies, i, j);
                                        }
                                    }
                                }
                            }
                        } else {
                            if (movies.get(i).getDuration() < movies.get(j).getDuration()) { // greater
                                Collections.swap(movies, i, j);
                            } else if (Objects.equals(movies.get(i).getDuration(), movies.get(j).getDuration())) { // equals
                                if (action.getFilters().getSort().getRating() != null) {
                                    if (action.getFilters().getSort().getRating().equals("increasing")) {
                                        if (movies.get(i).getRating() > movies.get(j).getRating()) {
                                            Collections.swap(movies, i, j);
                                        }
                                    } else {
                                        if (movies.get(i).getRating() < movies.get(j).getRating()) {
                                            Collections.swap(movies, i, j);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                if (action.getFilters().getSort().getRating() != null) {
                    for (int i = 0; i < movies.size() - 1; ++i) {
                        for (int j = i + 1; j < movies.size(); ++j) {
                            if (action.getFilters().getSort().getRating().equals("increasing")) {
                                if (movies.get(i).getRating() > movies.get(j).getRating()) {
                                    Collections.swap(movies, i, j);
                                }
                            } else {
                                if (movies.get(i).getRating() < movies.get(j).getRating()) {
                                    Collections.swap(movies, i, j);
                                }
                            }
                        }
                    }
                }
            }
        }

        return  true;
    }

    /**
     * Changes the balance and current tokens of the user.
     * @param action the current action
     * @return true if the operation was successful else false
     */
    public static Boolean buyTokens(final ActionInput action) {
        FileSystem instance = FileSystem.getInstance();
        UserInput currentUser = instance.getCurrentUser();
        Page current = instance.getCurrent();

        if (!current.getName().equals(FSConstants.UPGRADES_PAGE)) {
            return false;
        }

        int count = Integer.parseInt(action.getCount());
        currentUser.getCredentials().setBalance(
                Integer.toString(Integer.parseInt(
                        currentUser.getCredentials().getBalance()
                ) - count)
        );

        currentUser.setTokensCount(
                currentUser.getTokensCount() + count
        );

        return true;
    }

    /**
     * Change the account type of the user.
     * @return true if the operation was successful else false
     */
    public static Boolean buyPremiumAccount() {
        FileSystem instance = FileSystem.getInstance();
        Page current = instance.getCurrent();

        if (!current.getName().equals(FSConstants.UPGRADES_PAGE)) {
            return false;
        }

        UserInput currentUser = instance.getCurrentUser();

        if (currentUser.getCredentials().getAccountType().equals("premium")) {
            return false;
        }
        int count = 10;
        // TODO change tokens count not balance
        currentUser.setTokensCount(
                currentUser.getTokensCount() - count
        );

        currentUser.getCredentials().setAccountType("premium");

        return true;
    }

    /**
     * Changes the purchase movies list and the current tokens
     * of the user.
     * @param movie the movie to buy
     * @return true if the operation was successful else false
     */
    public static Boolean purchaseMovie(final MovieInput movie) {
        FileSystem instance = FileSystem.getInstance();
        Page current = instance.getCurrent();
        UserInput currentUser = instance.getCurrentUser();
        if (!current.getName().equals(FSConstants.SEE_DETAILS_PAGE)) {
            return false;
        }
        boolean purchasable = false;
        for (MovieInput m: Database.getInstance().getMovies()) {
            if (m.equals(movie)) {
                purchasable = true;
                break;
            }
        }

        if (!purchasable) {
            return false;
        }

        if (currentUser.getCredentials().getAccountType().equals("premium")) {
            if (currentUser.getNumFreePremiumMovies() > 0) {
                currentUser.setNumFreePremiumMovies(
                        currentUser.getNumFreePremiumMovies() - 1
                );
                currentUser.getPurchasedMovies().add(movie);
                return true;
            }
        }

        int count = 2;
        int tokens = currentUser.getTokensCount();

        if (tokens < count) {
            return false;
        }

        currentUser.setTokensCount(tokens - count);
        currentUser.getPurchasedMovies().add(movie);

        return true;
    }

    /**
     * Changes the watched movies list of the user.
     * @param movie the mobie to watch
     * @return true if the operation was successful else false
     */
    public static Boolean watchMovie(final MovieInput movie) {
        FileSystem instance = FileSystem.getInstance();
        Page current = instance.getCurrent();
        UserInput currentUser = instance.getCurrentUser();

        if (!current.getName().equals(FSConstants.SEE_DETAILS_PAGE)) {
            return false;
        }

        boolean purchased = false;

        for (MovieInput moviePurchased: currentUser.getPurchasedMovies()) {
            if (movie.equals(moviePurchased)) {
                purchased = true;
                break;
            }
        }

        if (!purchased) {
            return false;
        }

        currentUser.getWatchedMovies().add(movie);

        return true;
    }

    /**
     * Changes the liked movies list of the user.
     * @param movie the movie to like
     * @return true if the operation was successful else false
     */
    public static Boolean likeMovie(final MovieInput movie) {
        FileSystem instance = FileSystem.getInstance();
        Page current = instance.getCurrent();
        UserInput currentUser = instance.getCurrentUser();

        if (!current.getName().equals(FSConstants.SEE_DETAILS_PAGE)) {
            return false;
        }

        boolean watched = false;

        for (MovieInput movieWatched: currentUser.getWatchedMovies()) {
            if (movie.equals(movieWatched)) {
                watched = true;
                break;
            }
        }

        if (!watched) {
            return false;
        }

        movie.setNumLikes(movie.getNumLikes() + 1);
        currentUser.getLikedMovies().add(movie);

        return true;
    }

    /**
     * Changes the rated movies list of the user
     * and the rate of the movie
     * @param movie the movie to rate
     * @param action the current action
     * @return true if the operation was successful else false
     */
    public static Boolean rateMovie(final MovieInput movie,
                                    final ActionInput action) {
        FileSystem instance = FileSystem.getInstance();
        Page current = instance.getCurrent();
        UserInput currentUser = instance.getCurrentUser();

        if (!current.getName().equals(FSConstants.SEE_DETAILS_PAGE)) {
            return false;
        }

        boolean watched = false;

        for (MovieInput movieWatched: currentUser.getWatchedMovies()) {
            if (movie.getName().equals(movieWatched.getName())) {
                watched = true;
                break;
            }
        }

        if (!watched) {
            return false;
        }
        double rating = action.getRate();
        movie.getRatings().add(rating);
        movie.setNumRatings(
                movie.getNumRatings() + 1
        );

        double sum = 0;
        for (double rate: movie.getRatings()) {
            sum += rate;
        }
        movie.setRating(sum / movie.getRatings().size());
        currentUser.getRatedMovies().add(movie);

        return true;
    }

}
