package filesystem;

import fileio.ActionInput;
import fileio.MovieInput;
import fileio.UserInput;

import java.io.File;
import java.lang.constant.Constable;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FSActions {
    private FSActions() {

    }

    /**
     * Change the current page to one of its children.
     * @param action the given action
     * @return true if operation was successful esle false
     */
    public static Boolean changePage(ActionInput action) {
        FileSystem instance = FileSystem.getInstance();
        Page current = instance.getCurrent();

        String pageTitle = action.getPage();
//        System.out.println(pageTitle + " " + current.getName());
//        System.out.println();
        System.out.println(current.getName());
        System.out.println(current.getChildren());
        for (Page page: current.getChildren()) {
            if (page.getName().equals(pageTitle)) {
                if (pageTitle.equals(FSConstants.logoutPage)) {
                    instance.setCurrent(instance.getUnAuthPage());
                    instance.setCurrentUser(null);
                    return true;
                } else if (pageTitle.equals(FSConstants.seeDetailsPage)) {
                }
                instance.setCurrent(page);
                return true;
            }
        }
//        instance.setCurrent(instance.getUnAuthPage());
        return false;
    }

    /**
     * Login in the given page.
     * @param users the input users
     * @param action the given action
     * @return true if the operation was successful else false
     */
    public static Boolean login(List<UserInput> users, ActionInput action) {
        FileSystem instance = FileSystem.getInstance();
        Page current = instance.getCurrent();

        if (!current.permissionExists(FSConstants.loginPermission)) {
            return false;
        }
        if (!action.getFeature().equals(FSConstants.loginPermission)) {
            return false;
        }

        for (UserInput user: users) {
            if (user.exists(action.getCredentials().getName(), action.getCredentials().getPassword())) {
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
    public static Boolean register(List<UserInput> users, ActionInput action) {
        FileSystem instance = FileSystem.getInstance();
        Page current = instance.getCurrent();

        if (!current.permissionExists(FSConstants.registerPermission)) {
            return false;
        }
        if (!action.getFeature().equals(FSConstants.registerPermission)) {
            instance.setCurrent(instance.getUnAuthPage());
            return false;
        }

        for (UserInput user: users) {
            if (user.getCredentials().getName().equals(action.getCredentials().getName())
                && user.getCredentials().getPassword().equals(action.getCredentials().getPassword())) {
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
    public static Boolean search(List<MovieInput> movies, List<MovieInput> currentMovies, ActionInput action) {
        FileSystem instance = FileSystem.getInstance();
        Page current = instance.getCurrent();
        movies.clear();

        if (!current.getName().equals("movies")) {
            return false;
        }

        for (MovieInput movie: currentMovies) {
            if (movie.getName().startsWith(action.getStartsWith())) {
                movies.add(movie);
            }
        }

        return true;
    }

    public static Boolean filter(List<MovieInput> movies, List<MovieInput> currentMovies, ActionInput action) {
        FileSystem instance = FileSystem.getInstance();
        Page current = instance.getCurrent();

        if (!current.getName().equals("movies")) {
            return false;
        }

        movies.clear();
//        List<MovieInput> resList = new ArrayList<>();

        if (action.getFilters().getContains() != null) {
            for (MovieInput movie: currentMovies) {
                boolean add = movie.getActors().containsAll(action.getFilters().getContains().getActors());
                if (!movie.getGenres().containsAll(action.getFilters().getContains().getGenre())) {
                    add = false;
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
                if (action.getFilters().getSort().getDuration().equals("increasing")) {
                    movies.sort(Comparator.comparing(MovieInput::getDuration));
                } else {
                    movies.sort((o1,o2)-> o2.getDuration().compareTo(o1.getDuration()));
                }
            }
            if (action.getFilters().getSort().getRating() != null) {
                if (action.getFilters().getSort().getRating().equals("increasing")) {
                    movies.sort(Comparator.comparing(MovieInput::getRating));
                } else {
                    movies.sort((o1, o2) -> o2.getRating().compareTo(o1.getRating()));
                }
            }
        }

        return  true;
    }
}
