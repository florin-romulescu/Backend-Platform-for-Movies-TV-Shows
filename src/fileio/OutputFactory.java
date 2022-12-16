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

    public StandardOutput(String error) {
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

    public UserLoggedInOutput(String error, UserInput user) {
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

    public MoviesOutput(String error, List<MovieInput> currentMovieList, UserInput currentUser) {
        super.error = error;
        super.currentUser = currentUser;
        super.currentMovieList = currentMovieList;
    }

    @Override
    public ObjectNode convertToObjectNode() {
        ObjectNode obj = new ObjectMapper().createObjectNode();
        obj.put("error", error);
        ArrayNode arr = new ObjectMapper().createArrayNode();

        for(MovieInput movie: currentMovieList) {
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

public class OutputFactory {
    public enum OutputType {
        StandardOutput, UserLoggedInOutput, MoviesOutput
    }

    public static ObjectNode createOutput(OutputType type, String error, List<MovieInput> currentMovieList, UserInput currentUser) {
        switch (type) {
            case StandardOutput : return new StandardOutput(error).convertToObjectNode();
            case UserLoggedInOutput: return new UserLoggedInOutput(error, currentUser).convertToObjectNode();
            case MoviesOutput: return new MoviesOutput(error, currentMovieList, currentUser).convertToObjectNode();
        }
        throw new IllegalArgumentException("The output type" + type + "is not recognized");
    }
}