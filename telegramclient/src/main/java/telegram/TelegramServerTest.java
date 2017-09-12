package telegram;

import com.github.badoualy.telegram.api.Kotlogram;
import com.github.badoualy.telegram.api.TelegramApp;
import com.github.badoualy.telegram.api.TelegramClient;
import com.github.badoualy.telegram.tl.api.*;
import com.github.badoualy.telegram.tl.api.auth.TLAuthorization;
import com.github.badoualy.telegram.tl.api.auth.TLSentCode;
import com.github.badoualy.telegram.tl.api.messages.TLAbsDialogs;
import com.github.badoualy.telegram.tl.api.updates.*;
import com.github.badoualy.telegram.tl.core.TLVector;
import com.github.badoualy.telegram.tl.exception.RpcErrorException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import static telegram.ApiConfiguration.*;

/**
 * Created by Tim on 06.09.2017.
 */
public class TelegramServerTest {
    public static TelegramApp application = new TelegramApp(API_ID, API_HASH, MODEL, SYSTEM_VERSION, APP_VERSION, LANG_CODE);

    public static void main(String[] args) throws Exception {
        ApiStorage apiStorage = new ApiStorage();

        TelegramClient client = Kotlogram.getDefaultClient(application, apiStorage);
        try {
            boolean isUserLoggedIn = false;
            //Check if user is already logged in
            try {
                TLUserFull userFull = client.usersGetFullUser(new TLInputUserSelf());
                isUserLoggedIn = true;
            } catch (RpcErrorException e) {
                if (e.getCode() == 401) {
                    System.out.println("User not logged in!");
                    isUserLoggedIn = false;
                }
            }

            //Login user if necessary
            if (!isUserLoggedIn) {
                try {
                    // Send code to account
                    TLSentCode sentCode = client.authSendCode(false, TEST_PHONE_NUMBER, true);
                    //if (sentCode.getPhoneRegistered() == true) TODO phone registered is false?!
                    System.out.println("Authentication code: ");
                    String code = new Scanner(System.in).nextLine();

                    // Auth with the received code
                    TLAuthorization authorization = client.authSignIn(TEST_PHONE_NUMBER, sentCode.getPhoneCodeHash(), code);
                    isUserLoggedIn = true;

                    TLUser self = authorization.getUser().getAsUser();
                    System.out.println("Authorization successful!");
                    System.out.println("YOUR ID IS " + self.getId());
                    System.out.println("You are now signed in as " + self.getFirstName() + " " + self.getLastName() + " @" + self.getUsername());
                } catch (RpcErrorException e) {
                    System.err.print("Authorization failed!");
                    e.printStackTrace();
                    //throw new Exception("Cannot authorize user!");
                    isUserLoggedIn = false;
                }
            }

            if (isUserLoggedIn) {

                try {
                    //printOutNewMessages(client,30);
//                    tokenTypes:
//                    1 - APNS
//                    2 - GCM
//                    3 - MPNS
//                    4 - Simple Push
//                    5 - Ubuntu Phone
//                    6 - Blackberry
//                    TLBool deviceRegistered = client.accountRegisterDevice(4,"testToken");
//                    if(deviceRegistered.equals(TLBool.get(true))) {
//                        System.out.println("dicve registered");
//                    }

                    printOutNewMessages(client, 10);

//                    TLAbsDialogs tlAbsDialogs = client.messagesGetDialogs(false,0, 0, new TLInputPeerEmpty(), 10);
//
//                    // Map peer id to displayable string
//                    HashMap<Integer, String> nameMap = createNameMap(tlAbsDialogs);
//
//                    // Map message id to message
//                    HashMap<Integer, TLAbsMessage> messageMap = new HashMap<>();
//                    tlAbsDialogs.getMessages().forEach(message -> messageMap.put(message.getId(), message));
//
//                    tlAbsDialogs.getDialogs().forEach(dialog -> {
//                        System.out.print(nameMap.get(getId(dialog.getPeer())) + ": ");
//                        TLAbsMessage topMessage = messageMap.get(dialog.getTopMessage());
//                        if (topMessage instanceof TLMessage) {
//                            // The message could also be a file, a photo, a gif, ...
//                            System.out.println(((TLMessage) topMessage).getMessage());
//                        } else if (topMessage instanceof TLMessageService) {
//                            TLAbsMessageAction action = ((TLMessageService) topMessage).getAction();
//                            // action defined the type of message (user joined group, ...)
//                            System.out.println("Service message");
//                        }
//                    });
                } catch (RpcErrorException | IOException e) {
                    e.printStackTrace();
                } finally {
                    client.close(); // Important, do not forget this, or your process won't finish
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            client.close(); // Important, do not forget this, or your process won't finish
        }
    }

    public static void printOutNewMessages(TelegramClient client, int sleepTimeInSec) throws Exception {
        boolean isRunning = true;
        //Init message Vector
        TLVector<TLAbsMessage> tlAbsMessages = new TLVector<>(TLAbsMessage.class);
        //Try to fill message Vector
        TLState tlState = client.updatesGetState();
        TLAbsDifference tlAbsDifference;
        while (isRunning) {
            System.out.println("PTS: " + tlState.getPts() + " Number of events occured in a text box");
            System.out.println("QTS: " + tlState.getQts() + " Position in a sequence of updates in secret chats. For further detailes refer to article ");
            System.out.println("DATE: " + tlState.getDate() + " Date of condition");
            System.out.println("SEQ: " + tlState.getSeq() + " Number of sent updates");
            System.out.println("Unread Count: " + tlState.getUnreadCount() + " Number of unread messages");


            tlAbsDifference = client.updatesGetDifference(tlState.getPts(), 8000, tlState.getDate(), tlState.getQts());

            if (tlAbsDifference instanceof TLDifferenceEmpty) {
                System.out.println("difference empty");
                //Clear message Vector:
                tlAbsMessages.removeAll(tlAbsMessages);

            } else if (tlAbsDifference instanceof TLDifference) {
                System.out.println("tl difference");
                tlAbsMessages = ((TLDifference) tlAbsDifference).getNewMessages();
                //Update State
                tlState = ((TLDifference) tlAbsDifference).getState();
            } else if (tlAbsDifference instanceof TLDifferenceSlice) {
                System.out.println("tl difference slice");
                tlAbsMessages = ((TLDifferenceSlice) tlAbsDifference).getNewMessages();
                //Update State
                tlState = ((TLDifferenceSlice) tlAbsDifference).getIntermediateState();
            } else if (tlAbsDifference instanceof TLDifferenceTooLong) {
                System.out.println("to many difference!");
            } else {
                throw new Exception("Unkown updateDifferenceType");
            }

            if (!tlAbsMessages.isEmpty()) {
                tlAbsMessages.forEach(tlAbsMessage -> {
                    if (tlAbsMessage instanceof TLMessage) {
                        //TODO convert messages
                        System.out.println(((TLMessage) tlAbsMessage).getMessage());
                    }
                });
            }

            TimeUnit.SECONDS.sleep(sleepTimeInSec);
        }
    }

    /**
     * @param tlAbsDialogs result from messagesGetDialogs
     * @return a map where the key is the peerId and the value is the chat/channel title or the user's name
     */
    public static HashMap<Integer, String> createNameMap(TLAbsDialogs tlAbsDialogs) {
        // Map peer id to name
        HashMap<Integer, String> nameMap = new HashMap<>();

        tlAbsDialogs.getUsers().stream()
                .map(TLAbsUser::getAsUser)
                .forEach(user -> nameMap.put(user.getId(),
                        user.getFirstName() + " " + user.getLastName()));

        tlAbsDialogs.getChats().stream()
                .forEach(chat -> {
                    if (chat instanceof TLChannel) {
                        nameMap.put(chat.getId(), ((TLChannel) chat).getTitle());
                    } else if (chat instanceof TLChannelForbidden) {
                        nameMap.put(chat.getId(), ((TLChannelForbidden) chat).getTitle());
                    } else if (chat instanceof TLChat) {
                        nameMap.put(chat.getId(), ((TLChat) chat).getTitle());
                    } else if (chat instanceof TLChatEmpty) {
                        nameMap.put(chat.getId(), "Empty chat");
                    } else if (chat instanceof TLChatForbidden) {
                        nameMap.put(chat.getId(), ((TLChatForbidden) chat).getTitle());
                    }
                });

        return nameMap;
    }

    public static int getId(TLAbsPeer peer) {
        if (peer instanceof TLPeerUser)
            return ((TLPeerUser) peer).getUserId();
        if (peer instanceof TLPeerChat)
            return ((TLPeerChat) peer).getChatId();

        return ((TLPeerChannel) peer).getChannelId();
    }

}
