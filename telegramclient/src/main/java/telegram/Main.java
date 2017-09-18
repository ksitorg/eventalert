package telegram;

/**
 * Created by Tim on 17.09.2017.
 */
public class Main {
    public static void main(String[] args) {
        TelegramUpdateServer server = new TelegramUpdateServer();
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                server.stop();
            }
        }));
        server.start();
    }
}
