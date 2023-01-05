package fileio;

import com.fasterxml.jackson.databind.node.ObjectNode;

public interface Output {
    ObjectNode convertToObjectNode();
}
