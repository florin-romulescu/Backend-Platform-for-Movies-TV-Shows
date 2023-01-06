package fileio;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface Output {
    /**
     * Converts this object to an ObjectNode
     * for output purposes.
     * @return an ObjectNode with with all the
     * necessary data
     */
    ObjectNode convertToObjectNode();
}
