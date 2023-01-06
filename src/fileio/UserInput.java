package fileio;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class UserInput implements Output {
    private static final int NUM_FREE_PREM_MOVIES = 15;
    private CredentialsInput credentials;
    private Integer tokensCount = 0;
    private Integer numFreePremiumMovies = NUM_FREE_PREM_MOVIES;
    private List<MovieInput> purchasedMovies = new ArrayList<>();
    private List<MovieInput> watchedMovies = new ArrayList<>();
    private List<MovieInput> likedMovies = new ArrayList<>();
    private List<MovieInput> ratedMovies = new ArrayList<>();
    private List<Notification> notifications = new ArrayList<>();

    private final List<String> subscribedGenres = new ArrayList<>();

    public UserInput(final CredentialsInput credentials) {
        this.credentials = credentials;
    }

    public UserInput() {
    }

    public CredentialsInput getCredentials() {
        return credentials;
    }

    public void setCredentials(final CredentialsInput credentials) {
        this.credentials = credentials;
    }

    public Integer getTokensCount() {
        return tokensCount;
    }

    public void setTokensCount(final Integer tokensCount) {
        this.tokensCount = tokensCount;
    }

    public Integer getNumFreePremiumMovies() {
        return numFreePremiumMovies;
    }

    public void setNumFreePremiumMovies(final Integer numFreePremiumMovies) {
        this.numFreePremiumMovies = numFreePremiumMovies;
    }

    public List<MovieInput> getPurchasedMovies() {
        return purchasedMovies;
    }

    public void setPurchasedMovies(final List<MovieInput> purchasedMovies) {
        this.purchasedMovies = purchasedMovies;
    }

    public List<MovieInput> getWatchedMovies() {
        return watchedMovies;
    }

    public void setWatchedMovies(final List<MovieInput> watchedMovies) {
        this.watchedMovies = watchedMovies;
    }

    public List<MovieInput> getLikedMovies() {
        return likedMovies;
    }

    public void setLikedMovies(final List<MovieInput> likedMovies) {
        this.likedMovies = likedMovies;
    }

    public List<MovieInput> getRatedMovies() {
        return ratedMovies;
    }

    public void setRatedMovies(final List<MovieInput> ratedMovies) {
        this.ratedMovies = ratedMovies;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(final List<Notification> notifications) {
        this.notifications = notifications;
    }

    public List<String> getSubscribedGenres() {
        return subscribedGenres;
    }

    /**
     * Verify if this user has the given credentials
     * @param name name of the user
     * @param password password of the user
     * @return true if the parameters coincide else false
     */
    public boolean exists(final String name, final String password) {
        return credentials.getName().equals(name) && credentials.getPassword().equals(password);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserInput userInput = (UserInput) o;
        return Objects.equals(credentials, userInput.credentials);
    }

    @Override
    public int hashCode() {
        return Objects.hash(credentials);
    }

    /**
     * Converts this object into an object node for output purposes.
     * @return an ObjectNode object with this object data
     */
    public ObjectNode convertToObjectNode() {
        ObjectNode obj = new ObjectMapper().createObjectNode();
        obj.put("credentials", credentials.convertToObjectNode());
        obj.put("tokensCount", tokensCount);
        obj.put("numFreePremiumMovies", numFreePremiumMovies);
        ArrayNode arr = new ObjectMapper().createArrayNode();
        for (MovieInput movie: purchasedMovies) {
            arr.add(movie.convertToObjectNode());
        }
        obj.put("purchasedMovies", arr);

        arr = new ObjectMapper().createArrayNode();
        for (MovieInput movie: watchedMovies) {
            arr.add(movie.convertToObjectNode());
        }
        obj.put("watchedMovies", arr);

        arr = new ObjectMapper().createArrayNode();
        for (MovieInput movie: likedMovies) {
            arr.add(movie.convertToObjectNode());
        }
        obj.put("likedMovies", arr);

        arr = new ObjectMapper().createArrayNode();
        for (MovieInput movie: ratedMovies) {
            arr.add(movie.convertToObjectNode());
        }
        obj.put("ratedMovies", arr);

        arr = new ObjectMapper().createArrayNode();
        for (Notification notification: notifications) {
            arr.add(notification.convertToObjectNode());
        }

        obj.put("notifications", arr);

        return obj;
    }

}
