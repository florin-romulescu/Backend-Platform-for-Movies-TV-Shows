
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

    private static String inPath = "test.json";
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
        // This if statement is for testing purposes.
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
        instance.createNotificationService();
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
            if (action.getType().equals("database") && ret) {
                continue;
            }
            if (ret && action.getType().equals(FSConstants.ON_PAGE)) {
                if (action.getFeature().equals(FSConstants.SUBSCRIBE)) {
                    continue;
                }
            }

            if (ret) {
                // if there is no error
                if (fileSystem.getCurrentUser() == null) {
                    // if there is no user logged in
                    outputType = OutputFactory.OutputType.StandardOutput;
                } else { // if a user is logged in
                    currentUser = fileSystem.getCurrentUser();
                    if (fileSystem.getCurrent().getName()
                            .equals(FSConstants.MOVIES_PAGE)) {
                        // if the current page is 'movies'
                        instance.setDisplay(true);
                        outputType = OutputFactory.OutputType.MoviesOutput;
                    } else if (fileSystem.getCurrent().getName()
                            .equals(FSConstants.SEE_DETAILS_PAGE)) {
                        // if the current page is 'see details'
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
                // if there is an error
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
        ActionInput recommendationAction = new ActionInput();
        recommendationAction.setType(FSConstants.RECOMMENDATION);
        recommendationAction.setFeature(FSConstants.RECOMMENDATION);

        ActionContext actionContext = new ActionBuilder()
                .action(recommendationAction)
                .build();
        actionContext.createStrategy();
        boolean ret = actionContext.action();
        if (ret) {
            output.add(OutputFactory.createOutput(
                    OutputFactory.OutputType.RecommendationOutput,
                    null,
                    null,
                    fileSystem.getCurrentUser()
            ));
        }
    }
}
