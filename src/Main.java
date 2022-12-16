import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.*;
import filesystem.FSActions;
import filesystem.FSConstants;
import filesystem.FileSystem;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private Main() {}

    public static String path = "checker/resources/in/";
    public static String outPath = "checker/resources/out/";
    public static String test = "basic_5.json";
    public static String outName = "output.json";

    public static void main(String[] args) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Input input = objectMapper.readValue(new File(path + test), Input.class);
        ArrayNode output = new ObjectMapper().createArrayNode();
//        output.add(21);
        mainLoop(input, output);
        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        objectWriter.writeValue(new File(outPath + outName), output);
    }

    private static void mainLoop(Input input, ArrayNode output) {
        List<UserInput> users = input.getUsers();
        FileSystem instance = FileSystem.getInstance();
        System.out.println(input.getMovies());
        for (ActionInput action: input.getActions()) {
            String type = action.getType();
            boolean ret = false;
            boolean display = false;
            boolean movieListChangeable = true;
            OutputFactory.OutputType outputType; UserInput currentUser = null; List<MovieInput> currentMovieList = new ArrayList<>();
            String error = null;

            if (type.equals(FSConstants.changePage)) {
                ret = FSActions.changePage(action);
                if (action.getPage().equals(FSConstants.seeDetailsPage)) {
                    for (MovieInput movie: input.getMovies()) { // search the see details movie
                        if (movie.getName().equals(action.getMovie())) {
                            currentMovieList.add(movie);
                            break;
                        }
                    }
                    if (currentMovieList.size() == 0) {
                        ret = false;
                    }
                }
                if (!ret) {
                    display = true;
                }
            } else {
                switch (action.getFeature()) {
                    case FSConstants.loginPermission -> {
                        ret = FSActions.login(users, action);
                    }
                    case FSConstants.registerPermission -> {
                        ret = FSActions.register(users, action);
                    }
                    case FSConstants.searchPermission -> {
                        ret = FSActions.search(currentMovieList,
                                MovieInput.getUserMovies(instance.getCurrentUser(), input.getMovies()),
                                action);
                        display = true;
                        movieListChangeable = false;
                    }

                    case FSConstants.filterPermission -> {
                        ret = FSActions.filter(currentMovieList,
                                MovieInput.getUserMovies(instance.getCurrentUser(), input.getMovies()),
                                action);
                        movieListChangeable = false;
                    }
                }
                if (!ret || action.getFeature().equals(FSConstants.loginPermission)
                    || action.getFeature().equals(FSConstants.registerPermission)) {
                    display = true;
                }
            }

            if (ret) {
                if (instance.getCurrentUser() == null) {
                    outputType = OutputFactory.OutputType.StandardOutput;
                } else {
                    currentUser = instance.getCurrentUser();
                    if (instance.getCurrent().getName().equals("movies")) {
                        display = true;
                        outputType = OutputFactory.OutputType.MoviesOutput;
                        if (movieListChangeable) {
                            currentMovieList = MovieInput.getUserMovies(instance.getCurrentUser(), input.getMovies());
                        }

                    } else if (instance.getCurrent().getName().equals("see details")) {
                        outputType = OutputFactory.OutputType.MoviesOutput;
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
//                System.out.println(action.getType() + " " + action.getFeature() + " " + action.getPage() + " " + ret);
                output.add(OutputFactory.createOutput(outputType, error, currentMovieList, currentUser));
            }
        }
    }
}