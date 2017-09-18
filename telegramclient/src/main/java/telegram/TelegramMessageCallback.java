package telegram;

import com.github.badoualy.telegram.api.TelegramClient;
import com.github.badoualy.telegram.api.UpdateCallback;
import com.github.badoualy.telegram.tl.api.*;
import com.github.badoualy.telegram.tl.api.messages.TLAbsMessages;
import com.github.badoualy.telegram.tl.core.TLIntVector;
import com.github.badoualy.telegram.tl.exception.RpcErrorException;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by Tim on 17.09.2017.
 */
public class TelegramMessageCallback implements UpdateCallback {

    public static final Logger LOG = LoggerFactory.getLogger(TelegramMessageCallback.class);

    interface TelegramMessageListener {
        public void onMessage(TLMessage message, TLAbsUser user);
    }

    @Override
    public void onUpdates(@NotNull TelegramClient telegramClient, @NotNull TLUpdates tlUpdates) {
        LOG.debug("TLUpdates called");

        if (tlUpdates.getUpdates().isEmpty())
            LOG.info("Update was empty");

        tlUpdates.getUpdates().stream().forEach(this::processUpdate);
        tlUpdates.getChats().stream().forEach(tlAbsChat -> {
            if (tlAbsChat instanceof TLChat) {
                LOG.info("Chat title was {} ", ((TLChat) tlAbsChat).getTitle());
            }
        });

        tlUpdates.getUsers().stream().forEach(tlAbsUser -> {
            if (tlAbsUser instanceof TLUser) {
                LOG.info("User was username: {}, firstname: {}", ((TLUser) tlAbsUser).getUsername(), ((TLUser) tlAbsUser).getFirstName());
            }
        });

    }

    @Override
    public void onUpdatesCombined(@NotNull TelegramClient telegramClient, @NotNull TLUpdatesCombined tlUpdatesCombined) {
        LOG.debug("TLUpdatesCombined called");
    }

    @Override
    public void onUpdateShort(@NotNull TelegramClient telegramClient, @NotNull TLUpdateShort tlUpdateShort) {
        LOG.debug("TLUpdateShort called");
        processUpdate(tlUpdateShort.getUpdate());
    }

    @Override
    public void onShortChatMessage(@NotNull TelegramClient telegramClient, @NotNull TLUpdateShortChatMessage tlUpdateShortChatMessage) {
        //Wird in gruppen getriggert
        LOG.debug("TLUpdateShortChatMessage called");

        LOG.info("ShortChat Message was: {}", tlUpdateShortChatMessage.getMessage());
        LOG.info("ShortChat Message was from chat {}", tlUpdateShortChatMessage.getChatId());
    }

    @Override
    public void onShortMessage(@NotNull TelegramClient telegramClient, @NotNull TLUpdateShortMessage tlUpdateShortMessage) {
        //Wird in einzelnen chats getriggert
        LOG.debug("TLUpdateShortMessage called");
        LOG.info("ShortMessage was: {}", tlUpdateShortMessage.getMessage());

        TLIntVector messageIds = new TLIntVector();
        messageIds.add(tlUpdateShortMessage.getId());

        try {
            TLAbsMessages tlAbsMessages = telegramClient.messagesGetMessages(messageIds);
            tlAbsMessages.getMessages().forEach(tlAbsMessage -> {
                if (tlAbsMessage instanceof TLMessage) {
                    LOG.info("Full message was: {}", tlUpdateShortMessage.getMessage());
                    LOG.info("ShortMessage was from user {}", ((TLMessage) tlAbsMessage).getFromId());
                }
            });
        } catch (RpcErrorException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onShortSentMessage(@NotNull TelegramClient telegramClient, @NotNull TLUpdateShortSentMessage tlUpdateShortSentMessage) {
        LOG.debug("TLUpdateShortSentMessage called");
    }

    @Override
    public void onUpdateTooLong(@NotNull TelegramClient telegramClient) {
        LOG.debug("UpdateTooLong called");
    }

    private void processUpdate(TLAbsUpdate update) {
        if (update instanceof TLUpdateNewMessage) {
            TLAbsMessage message = ((TLUpdateNewMessage) update).getMessage();
            if (message instanceof TLMessage) {
                LOG.info("New message was {} ", ((TLMessage) message).getMessage());
            }
        }
    }
}

