package fileio;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserInput {
    CredentialsInput credentials;
    Integer tokensCount = 0;
    Integer numFreePremiumMovies = 15;
    List<MovieInput> purchasedMovies = new ArrayList<>();
    List<MovieInput> watchedMovies = new ArrayList<>();
    List<MovieInput> likedMovies = new ArrayList<>();
    List<MovieInput> ratedMovies = new ArrayList<>();

    public UserInput(CredentialsInput credentials) {
        this.credentials = credentials;
    }

    public UserInput() {
    }

    public CredentialsInput getCredentials() {
        return credentials;
    }

    public void setCredentials(CredentialsInput credentials) {
        this.credentials = credentials;
    }

    public Integer getTokensCount() {
        return tokensCount;
    }

    public void setTokensCount(Integer tokensCount) {
        this.tokensCount = tokensCount;
    }

    public Integer getNumFreePremiumMovies() {
        return numFreePremiumMovies;
    }

    public void setNumFreePremiumMovies(Integer numFreePremiumMovies) {
        this.numFreePremiumMovies = numFreePremiumMovies;
    }

    public List<MovieInput> getPurchasedMovies() {
        return purchasedMovies;
    }

    public void setPurchasedMovies(List<MovieInput> purchasedMovies) {
        this.purchasedMovies = purchasedMovies;
    }

    public List<MovieInput> getWatchedMovies() {
        return watchedMovies;
    }

    public void setWatchedMovies(List<MovieInput> watchedMovies) {
        this.watchedMovies = watchedMovies;
    }

    public List<MovieInput> getLikedMovies() {
        return likedMovies;
    }

    public void setLikedMovies(List<MovieInput> likedMovies) {
        this.likedMovies = likedMovies;
    }

    public List<MovieInput> getRatedMovies() {
        return ratedMovies;
    }

    public void setRatedMovies(List<MovieInput> ratedMovies) {
        this.ratedMovies = ratedMovies;
    }

    public boolean exists(String name, String password) {
        return credentials.name.equals(name) && credentials.password.equals(password);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserInput userInput = (UserInput) o;
        return Objects.equals(credentials, userInput.credentials);
    }

    @Override
    public int hashCode() {
        return Objects.hash(credentials);
    }

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

        return obj;
    }

    @Override
    public String toString() {
        return "UserInput{" +
                "credentials=" + credentials +
                '}';
    }

}
