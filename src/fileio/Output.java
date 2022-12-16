package fileio;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Output {
    private String error = null;
    private List<MovieInput> currentMovieList = new ArrayList<>();
    private UserInput currentUser = null;

    public Output(String error, List<MovieInput> currentMovieList, UserInput currentUser) {
        this.error = error;
        this.currentMovieList = currentMovieList;
        this.currentUser = currentUser;
    }

    public ObjectNode convertToObjectNode() {
        ObjectNode obj = new ObjectMapper().createObjectNode();
        obj.put("error", error);
        ArrayNode arr = new ObjectMapper().createArrayNode();

        for(MovieInput movie: currentMovieList) {
            arr.add(movie.convertToObjectNode());
        }
        obj.put("currentMovieList", arr);
        if (currentUser == null) {
            obj.put("currentUser", (JsonNode) null);
        } else {
            obj.put("currentUser", currentUser.convertToObjectNode());
        }
        return obj;
    }
}
