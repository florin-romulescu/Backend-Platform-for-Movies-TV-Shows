package features;

import database.Database;
import database.NotificationService;
import database.UserListener;
import features.strategy.FeatureStrategy;
import fileio.ActionInput;
import fileio.UserInput;
import filesystem.FileSystem;
import types.strategy.TypeStrategy;


public class SubscribeFeature implements FeatureStrategy {
    ActionInput action;

    public SubscribeFeature(ActionInput action) {
        this.action = action;
    }

    private boolean subscribe() {
        FileSystem fileSystem = FileSystem.getInstance();
        UserInput currentUser  = fileSystem.getCurrentUser();
        if (fileSystem.getCurrentMovie() == null) {
            // We are not on a movie page
            return false;
        }
        if (currentUser == null) {
            return false;
        }
        // Verify if the genre exists
        if (! fileSystem.getCurrentMovie().getGenres().contains(action.getSubscribedGenre())) {
            return false;
        }

        // Verify if the user is already subscribed
        Database instance = Database.getInstance();
        NotificationService service = instance.getNotificationService();
        boolean userSubscribed = false;
        boolean listenerExists = false;
        UserListener newListener = new UserListener(action.getSubscribedGenre());

        for (UserListener listener: service.getListeners()) {
            if (listener.getName().equals(action.getSubscribedGenre())) {
                newListener = listener;
                listenerExists = true;
                for (UserInput user: listener.getUsers()) {
                    if (user.equals(currentUser)) {
                        userSubscribed = true;
                        break;
                    }
                }
                break;
            }
        }
        if (userSubscribed) {
            return false;
        }
        if (!listenerExists) {
            service.getListeners().add(newListener);
        }
        newListener.getUsers().add(currentUser);

        return true;
    }

    @Override
    public boolean action() {
        return subscribe();
    }
}
