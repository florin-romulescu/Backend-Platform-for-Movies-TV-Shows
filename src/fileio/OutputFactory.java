package fileio;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;

abstract class OutTest {
    protected String error;
    protected List<MovieInput> currentMovieList;
    protected UserInput currentUser;

    public abstract ObjectNode convertToObjectNode();
}

class StandardOutput extends OutTest {

    StandardOutput(final String error) {
        super.error = error;
    }

    @Override
    public ObjectNode convertToObjectNode() {
        ObjectNode obj = new ObjectMapper().createObjectNode();
        obj.put("error", error);
        ArrayNode arr = new ObjectMapper().createArrayNode();

        obj.put("currentMoviesList", arr);
        obj.put("currentUser", (JsonNode) null);
        return obj;
    }
}

class UserLoggedInOutput extends OutTest {

    UserLoggedInOutput(final String error, final UserInput user) {
        super.error = error;
        super.currentUser = user;
    }

    @Override
    public ObjectNode convertToObjectNode() {
        ObjectNode obj = new ObjectMapper().createObjectNode();
        obj.put("error", error);
        ArrayNode arr = new ObjectMapper().createArrayNode();

        obj.put("currentMoviesList", arr);
        if (currentUser == null) {
            obj.put("currentUser", (JsonNode) null);
        } else {
            obj.put("currentUser", currentUser.convertToObjectNode());
        }
        return obj;
    }
}

class MoviesOutput extends OutTest {

    MoviesOutput(final String error,
                 final List<MovieInput> currentMovieList,
                 final UserInput currentUser) {
        super.error = error;
        super.currentUser = currentUser;
        super.currentMovieList = currentMovieList;
    }

    @Override
    public ObjectNode convertToObjectNode() {
        ObjectNode obj = new ObjectMapper().createObjectNode();
        obj.put("error", error);
        ArrayNode arr = new ObjectMapper().createArrayNode();

        for (MovieInput movie: currentMovieList) {
            arr.add(movie.convertToObjectNode());
        }
        obj.put("currentMoviesList", arr);
        if (currentUser == null) {
            obj.put("currentUser", (JsonNode) null);
        } else {
            obj.put("currentUser", currentUser.convertToObjectNode());
        }
        return obj;
    }
}

class RecommendationOutput extends OutTest {

    public RecommendationOutput(final UserInput currentUser) {
        super.error = null;
        super.currentUser = currentUser;
        super.currentMovieList = null;
    }

    @Override
    public ObjectNode convertToObjectNode() {
        ObjectNode obj = new ObjectMapper().createObjectNode();
        obj.put("error", (JsonNode) null);
        ArrayNode arr = new ObjectMapper().createArrayNode();

        obj.put("currentMoviesList", (JsonNode) null);
        if (currentUser == null) {
            obj.put("currentUser", (JsonNode) null);
        } else {
            obj.put("currentUser", currentUser.convertToObjectNode());
        }
        return obj;
    }
}

public class OutputFactory {
    public enum OutputType {
        StandardOutput, UserLoggedInOutput, MoviesOutput, RecommendationOutput
    }

    /**
     * Creates a specific output based on its type.
     * @param type the type of the output
     * @param error error message
     * @param currentMovieList the current movies that the user can watch
     * @param currentUser the current user
     * @return and ObjectNode object for output purposes
     */
    public static ObjectNode createOutput(final OutputType type,
                                          final String error,
                                          final List<MovieInput> currentMovieList,
                                          final UserInput currentUser) {
        return switch (type) {
            case StandardOutput -> new StandardOutput(error).convertToObjectNode();
            case UserLoggedInOutput ->
                    new UserLoggedInOutput(error, currentUser).convertToObjectNode();
            case MoviesOutput ->
                    new MoviesOutput(error, currentMovieList, currentUser).convertToObjectNode();
            case RecommendationOutput -> new RecommendationOutput(currentUser).convertToObjectNode();
        };
    }
}
