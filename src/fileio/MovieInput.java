package fileio;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MovieInput {
    private String name;
    private String year;
    private Integer duration;
    private ArrayList<String> genres;
    private ArrayList<String> actors;
    private ArrayList<String> countriesBanned;
    private List<Double> ratings = new ArrayList<>();
    private Integer numLikes = 0;
    private Double rating = 0.0;
    private Integer numRatings = 0;


    public MovieInput(String name, String year, Integer duration, ArrayList<String> genres, ArrayList<String> actors, ArrayList<String> countriesBanned) {
        this.name = name;
        this.year = year;
        this.duration = duration;
        this.genres = genres;
        this.actors = actors;
        this.countriesBanned = countriesBanned;
    }

    public MovieInput() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<String> genres) {
        this.genres = genres;
    }

    public ArrayList<String> getActors() {
        return actors;
    }

    public void setActors(ArrayList<String> actors) {
        this.actors = actors;
    }

    public ArrayList<String> getCountriesBanned() {
        return countriesBanned;
    }

    public void setCountriesBanned(ArrayList<String> countriesBanned) {
        this.countriesBanned = countriesBanned;
    }

    public Integer getNumLikes() {
        return numLikes;
    }

    public void setNumLikes(Integer numLikes) {
        this.numLikes = numLikes;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Integer getNumRatings() {
        return numRatings;
    }

    public void setNumRatings(Integer numRatings) {
        this.numRatings = numRatings;
    }

    public List<Double> getRatings() {
        return ratings;
    }

    public void setRatings(List<Double> ratings) {
        this.ratings = ratings;
    }

    public static List<MovieInput> getUserMovies(UserInput user, List<MovieInput> moviesList) {
        List<MovieInput> allowedMovies = new ArrayList<>();

        for (MovieInput movie: moviesList) {
            if (movie.getCountriesBanned().contains(user.getCredentials().getCountry())) {
                continue;
            }
            allowedMovies.add(movie);
        }

        return allowedMovies;
    }

    /**
     * Convert the object to an ObjectNode for implementing the output.
     * @return  an ObjectNode with all the object's data
     */
    public ObjectNode convertToObjectNode() {
        ObjectNode obj = new ObjectMapper().createObjectNode();
        obj.put("name", name);
        obj.put("duration", duration);
        ArrayNode arr = new ObjectMapper().createArrayNode();

        for (String actor: actors) {
            arr.add(actor);
        }
        obj.put("actors", arr);
        arr = new ObjectMapper().createArrayNode();

        for (String genre: genres) {
            arr.add(genre);
        }
        obj.put("genres", arr);

        arr = new ObjectMapper().createArrayNode();
        for (String country: countriesBanned) {
            arr.add(country);
        }
        obj.put("countriesBanned", arr);

        obj.put("numLikes", numLikes);
        obj.put("rating", rating);
        obj.put("numRatings", numRatings);
        obj.put("year", Integer.parseInt(year));

        return obj;
    }

    @Override
    public String toString() {
        return "MovieInput{" +
                "name='" + name + '\'' +
                ", year='" + year + '\'' +
                ", duration=" + duration +
                ", genres=" + genres +
                ", actors=" + actors +
                ", countriesBanned=" + countriesBanned +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovieInput that = (MovieInput) o;
        return Objects.equals(name, that.name) && Objects.equals(year, that.year);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, year);
    }
}
