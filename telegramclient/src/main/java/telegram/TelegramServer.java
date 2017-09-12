package telegram;

import com.github.badoualy.telegram.api.Kotlogram;
import com.github.badoualy.telegram.api.TelegramApp;
import com.github.badoualy.telegram.api.TelegramClient;
import com.github.badoualy.telegram.tl.api.TLUser;
import com.github.badoualy.telegram.tl.api.auth.TLAuthorization;
import com.github.badoualy.telegram.tl.api.auth.TLSentCode;
import com.github.badoualy.telegram.tl.exception.RpcErrorException;

import java.io.IOException;
import java.util.Scanner;

import static telegram.ApiConfiguration.*;

/**
 * Created by Tim on 06.09.2017.
 */
public class TelegramServer {
    public static TelegramApp application = new TelegramApp(API_ID, API_HASH, MODEL, SYSTEM_VERSION, APP_VERSION, LANG_CODE);

    public static void main(String[] args) {
        ApiStorage apiStorage = new ApiStorage();
        TelegramClient client = Kotlogram.getDefaultClient(application, apiStorage);


        // You can start making requests
        try {
            // Send code to account
            TLSentCode sentCode = client.authSendCode(false, TEST_PHONE_NUMBER, true);
            System.out.println("Authentication code: ");
            String code = new Scanner(System.in).nextLine();

            // Auth with the received code
            TLAuthorization authorization = client.authSignIn(TEST_PHONE_NUMBER, sentCode.getPhoneCodeHash(), code);
            TLUser self = authorization.getUser().getAsUser();
            System.out.println("You are now signed in as " + self.getFirstName() + " " + self.getLastName() + " @" + self.getUsername());
        } catch (RpcErrorException | IOException e) {
            e.printStackTrace();
        } finally {
            client.close(); // Important, do not forget this, or your process won't finish
        }
    }

}
