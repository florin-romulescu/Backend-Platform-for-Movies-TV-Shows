
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import database.Database;
import fileio.*;
import filesystem.FSConstants;
import filesystem.FileSystem;
import types.strategy.ActionBuilder;
import types.strategy.ActionContext;

import java.io.File;
import java.io.IOException;
import java.util.List;


public final class Main {

    private static String inPath = "checker/resources/in/basic_1.json";
    private static String outPath = "output.json";

    private Main() { }

    /**
     * Main function: Reads from input files and writes the expected
     * output in the given output path.
     * @param args first position contains the input file, the second contains the output file
     * @throws IOException if file cannot open
     */
    public static void main(final String[] args) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();

        ArrayNode output = new ObjectMapper().createArrayNode();
        if (args.length != 0) {
            inPath = args[0];
            outPath = args[1];
        }
        Input input = objectMapper.readValue(new File(inPath), Input.class);
        mainLoop(input, output);

        objectWriter.writeValue(new File(outPath), output);
    }

    /**
     * Loops through all the actions and executes the corresponding command.
     * @param input an input class with all the data
     * @param output where the output messages are going
     */
    public static void mainLoop(final Input input, final ArrayNode output) {
        Database.setInstanceNull();
        Database instance = Database.getInstance();
        FileSystem fileSystem = instance.getFileSystem();
        instance.setMovies(input.getMovies());
        instance.setUsers(input.getUsers());
        fileSystem.initCurrentMovies(null);

        for (ActionInput action : input.getActions()) {
            instance.setMoviesChangeable(true); instance.setDisplay(false);
            List<MovieInput> currentMoviesList = fileSystem.getCurrentMovies();
            OutputFactory.OutputType outputType;
            UserInput currentUser = null;
            String error = null;

            ActionContext actionContext = new ActionBuilder()
                    .currentMovieList(currentMoviesList)
                    .movies(instance.getMovies())
                    .users(instance.getUsers())
                    .action(action)
                    .build();
            actionContext.createStrategy();
            boolean ret = actionContext.action();
            if (instance.isMoviesChangeable()) {
                fileSystem.initCurrentMovies(action);
            }

            if (ret) {
                if (fileSystem.getCurrentUser() == null) {
                    outputType = OutputFactory.OutputType.StandardOutput;
                } else {
                    currentUser = fileSystem.getCurrentUser();
                    if (fileSystem.getCurrent().getName()
                            .equals(FSConstants.MOVIES_PAGE)) {
                        instance.setDisplay(true);
                        outputType = OutputFactory.OutputType.MoviesOutput;
                    } else if (fileSystem.getCurrent().getName()
                            .equals(FSConstants.SEE_DETAILS_PAGE)) {
                        if (fileSystem.getCurrentMovie() == null) {
                            error = FSConstants.ERROR;
                            outputType = OutputFactory.OutputType.StandardOutput;
                        } else {
                            outputType = OutputFactory.OutputType.MoviesOutput;
                        }
                        instance.setDisplay(true);
                    } else {
                        outputType = OutputFactory.OutputType.UserLoggedInOutput;
                    }
                }
            } else {
                outputType = OutputFactory.OutputType.StandardOutput;
                instance.setDisplay(true);
                error = "Error";
            }
            if (instance.isDisplay()) {
                output.add(OutputFactory.createOutput(
                        outputType,
                        error,
                        fileSystem.getCurrentMovies(),
                        currentUser));
            }
        }
    }
}
