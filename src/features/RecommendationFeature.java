package features;

import database.Database;
import features.strategy.FeatureStrategy;
import fileio.MovieInput;
import fileio.Notification;
import fileio.UserInput;
import filesystem.FileSystem;

import java.util.*;

public final class RecommendationFeature implements FeatureStrategy {

    private Map<String, Integer> generateLikedGenres(
            final UserInput currentUser) {
        Map<String, Integer> genresMap = new HashMap<>();
        for (MovieInput movie: currentUser.getLikedMovies()) {
            for (String genre: movie.getGenres()) {
                if (!genresMap.containsKey(genre)) {
                    genresMap.put(genre, 1);
                } else {
                    genresMap.put(
                            genre,
                            genresMap.get(genre) + 1
                    );
                }
            }
        }
        return genresMap;
    }

    private Map<MovieInput, Integer> generateLikedMovies() {
        List<MovieInput> movies = Database.getInstance().getMovies();
        Map<MovieInput, Integer> moviesMap = new HashMap<>();

        movies.forEach(movie -> moviesMap.put(movie, movie.getNumLikes()));
        return moviesMap;
    }

    private boolean recommendation() {
        FileSystem fileSystem = FileSystem.getInstance();
        UserInput currentUser = fileSystem.getCurrentUser();

        if (currentUser == null) {
            return false;
        }

        if (currentUser.getCredentials().getAccountType().equals("standard")) {
            return false;
        }

        if (currentUser.getPurchasedMovies().size() == 0
            || currentUser.getWatchedMovies().size() == 0
            || currentUser.getLikedMovies().size() == 0) {
            currentUser.getNotifications().add(
                    new Notification("No recommendation", "Recommendation")
            );
            return true;
        }
        // Generate genres
        Map<String, Integer> genresMap = generateLikedGenres(currentUser);

        // Descending sort
        List<Map.Entry<String, Integer>> genresList = new LinkedList<>(genresMap.entrySet());
        genresList.sort(new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(final Map.Entry<String, Integer> e1,
                               final Map.Entry<String, Integer> e2) {
                if (e1.getValue().equals(e2.getValue())) {
                    return e1.getKey().compareTo(e2.getKey());
                }
                return e2.getValue().compareTo(e1.getValue());
            }
        });

        Map<String, Integer> topGenres = new LinkedHashMap<>();
        genresList.forEach(entry -> topGenres.put(entry.getKey(), entry.getValue()));

        // Generate movies
        Map<MovieInput, Integer> moviesMap = generateLikedMovies();

        // Descending sort
        List<Map.Entry<MovieInput, Integer>> moviesList = new LinkedList<>(moviesMap.entrySet());
        moviesList.sort(new Comparator<Map.Entry<MovieInput, Integer>>() {
            @Override
            public int compare(final Map.Entry<MovieInput, Integer> e1,
                               final Map.Entry<MovieInput, Integer> e2) {
                if (e1.getValue().equals(e2.getValue())) {
                    return e1.getKey().getName().compareTo(e2.getKey().getName());
                }
                return e2.getValue().compareTo(e1.getValue());
            }
        });

        Map<MovieInput, Integer> topMovies = new LinkedHashMap<>();
        moviesList.forEach(entry -> topMovies.put(entry.getKey(), entry.getValue()));

        for (String genre: topGenres.keySet()) {
            for (MovieInput movie: topMovies.keySet()) {
                if (movie.getGenres().contains(genre)) {
                    if (!currentUser.getWatchedMovies().contains(movie)) {
                        currentUser.getNotifications()
                                .add(new Notification(movie.getName(), "Recommendation"));
                        return true;
                    }
                }
            }
        }

        currentUser.getNotifications()
                .add(new Notification("No recommendation", "Recommendation"));

        return true;
    }

    @Override
    public boolean action() {
        return recommendation();
    }
}
