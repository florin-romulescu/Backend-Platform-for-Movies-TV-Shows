package database;

public class Database {
    private final NotificationService notificationService;

    public Database() {
        notificationService = new NotificationService();
    }

    public NotificationService getNotificationService() {
        return notificationService;
    }

    public void newMovieNotifier(String msg) {
        notificationService.notifyUsers(msg);
    }
}
