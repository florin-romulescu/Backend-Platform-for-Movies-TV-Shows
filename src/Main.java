
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.*;
import filesystem.FSActions;
import filesystem.FSConstants;
import filesystem.FileSystem;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public class Main {

    private Main() {}

    public static String path = "checker/resources/in/";
    public static String outPath = "";
    public static String test = "basic_2.json";
    public static String outName = "results.json";

    public static void main(String[] args) throws IOException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
//
//        ArrayNode output = new ObjectMapper().createArrayNode();
//        Input input = objectMapper.readValue(new File(args[0]), Input.class);
//        mainLoop(input, output);
//
//        objectWriter.writeValue(new File(args[1]), output);
        File directory = new File(path);
        for (File file : Objects.requireNonNull(directory.listFiles())) {
            String fileName = file.getName();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            Input input = objectMapper.readValue(new File(path + fileName), Input.class);
            ArrayNode output = new ObjectMapper().createArrayNode();

            mainLoop(input, output);
            ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
            objectWriter.writeValue(new File(outPath + fileName), output);
        }
    }

    private static void mainLoop(Input input, ArrayNode output) {
        FileSystem.init();
        List<UserInput> users = input.getUsers();
        FileSystem instance = FileSystem.getInstance();
        instance.setAllMovies(input.getMovies());
        instance.initCurrentMovies(null);
        for (ActionInput action: input.getActions()) {
            String type = action.getType();
            boolean moviesChangeable = true;
            boolean ret = false;
            boolean display = false;
            OutputFactory.OutputType outputType; UserInput currentUser = null;
            List<MovieInput> currentMovieList = instance.getCurrentMovies();
            String error = null;

            if (type.equals(FSConstants.changePage)) {
                ret = FSActions.changePage(action);
                if (!ret) {
                    display = true;
                }
            } else {
                switch (action.getFeature()) {
                    case FSConstants.loginPermission -> ret = FSActions.login(users, action);
                    case FSConstants.registerPermission -> ret = FSActions.register(users, action);
                    case FSConstants.searchPermission -> {
                        ret = FSActions.search(currentMovieList,
                                MovieInput.getUserMovies(instance.getCurrentUser(), input.getMovies()),
                                action);
                        instance.setCurrentMovies(currentMovieList);
                        display = true;
                        moviesChangeable = false;
                    }
                    case FSConstants.filterPermission -> {
                        ret = FSActions.filter(currentMovieList,
                                MovieInput.getUserMovies(instance.getCurrentUser(), input.getMovies()),
                                action);
                        instance.setCurrentMovies(currentMovieList);
                        moviesChangeable = false;
                    }
                    case FSConstants.tokensPermission -> ret = FSActions.buyTokens(action);
                    case FSConstants.premiumPermission -> ret = FSActions.buyPremiumAccount();
                    case FSConstants.purchasePermission -> ret = FSActions.purchaseMovie(instance.getCurrentMovie());
                    case FSConstants.watchPermission -> ret  = FSActions.watchMovie(instance.getCurrentMovie());
                    case FSConstants.likePermission -> ret = FSActions.likeMovie(instance.getCurrentMovie());
                    case FSConstants.ratePermission -> ret = FSActions.rateMovie(instance.getCurrentMovie(), action);
                }
                if (!ret || action.getFeature().equals(FSConstants.loginPermission)
                    || action.getFeature().equals(FSConstants.registerPermission)) {
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
                    if (instance.getCurrent().getName().equals("movies")) {
                        display = true;
                        outputType = OutputFactory.OutputType.MoviesOutput;
                    } else if (instance.getCurrent().getName().equals("see details")) {
                        if (instance.getCurrentMovie() == null) {
                            error = "Error";
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
                output.add(OutputFactory.createOutput(outputType, error, instance.getCurrentMovies(), currentUser));
            }
        }
    }
}