
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.*;
import filesystem.FSActions;
import filesystem.FSConstants;
import filesystem.FileSystem;

import java.io.File;
import java.io.IOException;
import java.util.List;


public final class Main {

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
        Input input = objectMapper.readValue(new File(args[0]), Input.class);
        mainLoop(input, output);

        objectWriter.writeValue(new File(args[1]), output);
    }

    /**
     * Loops through all the actions and executes the corresponding command.
     * @param input an input class with all the data
     * @param output where the output messages are going
     */
    private static void mainLoop(final Input input, final ArrayNode output) {
        // Initialise the filesystem
        FileSystem.setInstanceNull();
        List<UserInput> users = input.getUsers();
        FileSystem instance = FileSystem.getInstance();
        instance.setAllMovies(input.getMovies());
        instance.initCurrentMovies(null);
        // -------------------------------- //
        boolean ret;
        for (ActionInput action: input.getActions()) {
            String type = action.getType();
            boolean moviesChangeable = true;
            boolean display = false;
            OutputFactory.OutputType outputType; UserInput currentUser = null;
            List<MovieInput> currentMovieList = instance.getCurrentMovies();
            String error = null;

            if (type.equals(FSConstants.CHANGE_PAGE)) {
                ret = FSActions.changePage(action);
                if (!ret) {
                    display = true;
                }
            } else {
                switch (action.getFeature()) {
                    case FSConstants.LOGIN_PERMISSION -> ret = FSActions.login(users, action);
                    case FSConstants.REGISTER_PERMISSION -> ret = FSActions.register(users, action);
                    case FSConstants.SEARCH_PERMISSION -> {
                        ret = FSActions.search(currentMovieList,
                                MovieInput.getUserMovies(instance.getCurrentUser(),
                                                        input.getMovies()),
                                action);
                        instance.setCurrentMovies(currentMovieList);
                        display = true;
                        moviesChangeable = false;
                    }
                    case FSConstants.FILTER_PERMISSION -> {
                        ret = FSActions.filter(currentMovieList,
                                MovieInput.getUserMovies(instance.getCurrentUser(),
                                                        input.getMovies()),
                                action);
                        instance.setCurrentMovies(currentMovieList);
                        moviesChangeable = false;
                    }
                    case FSConstants.TOKENS_PERMISSION ->
                            ret = FSActions.buyTokens(action);
                    case FSConstants.PREMIUM_PERMISSION ->
                            ret = FSActions.buyPremiumAccount();
                    case FSConstants.PURCHASE_PERMISSION ->
                            ret = FSActions.purchaseMovie(instance.getCurrentMovie());
                    case FSConstants.WATCH_PERMISSION ->
                            ret  = FSActions.watchMovie(instance.getCurrentMovie());
                    case FSConstants.LIKE_PERMISSION ->
                            ret = FSActions.likeMovie(instance.getCurrentMovie());
                    case FSConstants.RATE_PERMISSION ->
                            ret = FSActions.rateMovie(instance.getCurrentMovie(), action);
                    default -> ret = false;
                }
                if (!ret || action.getFeature().equals(FSConstants.LOGIN_PERMISSION)
                    || action.getFeature().equals(FSConstants.REGISTER_PERMISSION)) {
                    display = true;
                }
            }
            if (moviesChangeable) {
                instance.initCurrentMovies(action);
            }

            if (ret) {
                if (instance.getCurrentUser() == null) {
                    outputType = OutputFactory.OutputType.StandardOutput;
                } else {
                    currentUser = instance.getCurrentUser();
                    if (instance.getCurrent().getName()
                            .equals(FSConstants.MOVIES_PAGE)) {
                        display = true;
                        outputType = OutputFactory.OutputType.MoviesOutput;
                    } else if (instance.getCurrent().getName()
                            .equals(FSConstants.SEE_DETAILS_PAGE)) {
                        if (instance.getCurrentMovie() == null) {
                            error = FSConstants.ERROR;
                            outputType = OutputFactory.OutputType.StandardOutput;
                        } else {
                            outputType = OutputFactory.OutputType.MoviesOutput;
                        }
                        display = true;
                    } else {
                        outputType = OutputFactory.OutputType.UserLoggedInOutput;
                    }
                }
            } else {
                outputType = OutputFactory.OutputType.StandardOutput;
                error = "Error";
            }
            if (display) {
                output.add(OutputFactory.createOutput(
                        outputType,
                        error,
                        instance.getCurrentMovies(),
                        currentUser));
            }
        }
    }
}
