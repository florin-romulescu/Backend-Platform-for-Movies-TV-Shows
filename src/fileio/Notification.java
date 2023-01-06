package fileio;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Objects;

public final class Notification implements Output {
    private final String movieName;
    private final String message;

    public Notification(final String movieName,
                        final String message) {
        this.movieName = movieName;
        this.message = message;
    }


    @Override
    public ObjectNode convertToObjectNode() {
        ObjectNode obj = new ObjectMapper().createObjectNode();
        obj.put("movieName", movieName);
        obj.put("message", message);
        return obj;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Notification that = (Notification) o;
        return Objects.equals(movieName, that.movieName) && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(movieName, message);
    }
}
