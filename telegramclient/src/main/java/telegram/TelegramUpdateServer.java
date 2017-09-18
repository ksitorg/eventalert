package telegram;

import com.github.badoualy.telegram.api.Kotlogram;
import com.github.badoualy.telegram.api.TelegramApp;
import com.github.badoualy.telegram.api.TelegramClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static telegram.ApiConfiguration.*;

/**
 * Created by Tim on 15.09.2017.
 */
public class TelegramUpdateServer {
    public static final Logger LOG = LoggerFactory.getLogger(TelegramUpdateServer.class);

    private boolean isRunning = false;

    private TelegramApp application;
    private ApiStorage apiStorage;
    private TelegramClient client;

    public TelegramUpdateServer() {
        application = new TelegramApp(API_ID, API_HASH, MODEL, SYSTEM_VERSION, APP_VERSION, LANG_CODE);
        apiStorage = new ApiStorage();
    }

    public void start() {
        isRunning = true;
        run();
    }

    public void stop() {
        isRunning = false;
        LOG.info("TelegramUpdateServer will stop");
        client.close();
    }

    private void run() {
        client = Kotlogram.getDefaultClient(application, apiStorage, apiStorage.loadDc(), new TelegramMessageCallback());
        LOG.info("TelegramUpdateServer was started");

        while (!client.isClosed()) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                client.close();
            }
        }

        client.close();
        LOG.info("TelegramUpdateServer was is stopped");
    }


}
