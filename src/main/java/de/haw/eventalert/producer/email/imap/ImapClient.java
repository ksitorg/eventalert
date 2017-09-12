package de.haw.eventalert.producer.email.imap;

/**
 * Created by Tim on 18.08.2017.
 */

import com.sun.mail.imap.IMAPFolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.event.MessageCountAdapter;
import javax.mail.event.MessageCountEvent;
import java.io.Serializable;
import java.util.Properties;

/**
 * Created by Tim on 19.04.2017.
 * <p>
 * vorlage: http://www.programcreek.com/java-api-examples/index.php?source_dir=tradeframework-master/event-trader/src/main/java/com/jgoetsch/eventtrader/source/IMAPMsgSource.java
 */
public class ImapClient implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(ImapClient.class);
    private volatile boolean isRunning;

    private String host;
    private int port = 993;

    private String user;
    private String password;

    private String folderName = "INBOX";

    private transient Session session; //TODO get rid of tansient (?)
    private transient Store store;
    private transient IMAPFolder folder;

    private MessageEventListener listener;

    public ImapClient(String host, String user, String password) {
        this.host = host;
        this.user = user;
        this.password = password;
    }

    public void init() {
        try {
            reConnect();
        } catch (MessagingException e) {
            LOG.error("Error connecting to imap server!", e);
        }
    }

    public void run() {
        if (!(store != null && folder != null && listener != null)) throw new AssertionError();
        isRunning = true;
        try {
            while (isRunning) { //TODO stable connection that dont close automaticcly when idle
                if (!store.isConnected())
                    reConnect();
                if (!folder.isOpen())
                    reOpenFolder();
                folder.idle();
            }

        } catch (MessagingException e) {
            LOG.error("Error waiting for messages!", e);
        }
        isRunning = false;
    }

    public void stop() {
        this.isRunning = false;
        try {
            disconnect();
            LOG.info("ImapClient disconnects");
        } catch (MessagingException e) {
            LOG.error("Error disconnecting!", e);
        }
    }

    public void setListener(MessageEventListener listener) {
        this.listener = listener;
    }

    private void reConnect() throws MessagingException {
        LOG.info("ImapClient (re)connects");
        if (session == null)
            createSession();
        if (store == null)
            store = session.getStore();
        if (!store.isConnected())
            store.connect(host, port, user, password);
        reOpenFolder();
    }

    private void reOpenFolder() throws MessagingException {
        assert (store != null && store.isConnected());
        if (folder == null)
            folder = (IMAPFolder) store.getFolder(folderName);
        if (!folder.isOpen())
            folder.open(Folder.READ_ONLY);
        folder.addMessageCountListener(getMessageCountAdapter());
    }

    private void createSession() {
        assert (session == null);
        final Properties props = new Properties();
        props.setProperty("mail.store.protocol", "imaps");
        session = Session.getDefaultInstance(props);
    }

    private void disconnect() throws MessagingException {
        if (folder != null)
            folder.close(false); //TODO was ist expunge?
        if (store != null)
            store.close();
    }

    private MessageCountAdapter getMessageCountAdapter() {
        return new MessageCountAdapter() {
            @Override
            public void messagesAdded(MessageCountEvent e) {
                assert (listener != null);
                Message[] messages = e.getMessages();
                for (Message message : messages) {
                    //trigger listener
                    listener.onMessage(message);
                }
            }
        };
    }

    public interface MessageEventListener extends Serializable {
        void onMessage(Message msg);
    }
}