package de.haw.eventalert.producer.email.imap;

import de.haw.eventalert.producer.email.MailMessage;
import de.haw.eventalert.producer.email.MailMessageImpl;
import de.haw.eventalert.producer.email.TransformMessage;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Tim on 18.08.2017.
 */
public class ImapSource implements SourceFunction<MailMessage> {
    private static final Logger LOG = LoggerFactory.getLogger(ImapSource.class);
    private volatile boolean isRunning = true;

    private ImapClient client;
    private Long accountId;

    public ImapSource(String imapHost, String imapLoginName, String imapLoginPassword) {
        this.client = new ImapClient(imapHost, imapLoginName, imapLoginPassword);
    }

    public ImapSource(String propertyFile) throws IOException {
        Properties properties = new Properties();
        InputStream in = getClass().getResourceAsStream("/" + propertyFile);
        properties.load(in);
        this.client = new ImapClient(properties.getProperty("host"), properties.getProperty("user"), properties.getProperty("password"));
        this.accountId = Long.valueOf(properties.getProperty("id"));
        //ParameterTool parameters = ParameterTool.fromPropertiesFile(propertyFile);
        //this.client = new ImapClient(parameters.get("host"),parameters.get("user"),parameters.get("password"));
    }

    @Override
    public void run(SourceContext<MailMessage> sourceContext) throws Exception {
        client.init();
        client.setListener((ImapClient.MessageEventListener) message -> {
            //Transform to Message to MailMessage
            //TODO a little workaround to add imapAccountId to MailMessage
            MailMessageImpl mailMessage = (MailMessageImpl) TransformMessage.javaxMessageToMailMessage.apply(message);
            mailMessage.setImapAccountId(this.accountId);

            LOG.debug("New Message received from ImapClient: {}", mailMessage);
            try {
                sourceContext.collectWithTimestamp(mailMessage, mailMessage.getReceivedTime());
            } catch (Exception e) {
                LOG.error("Error collecting messages from ImapClient", e);
            }
        });
        client.run();
    }

    @Override
    public void cancel() {
        client.stop();
    }

//    private Stream<Message> getNewMessages() throws IOException {
//        client.
//    }
//TODO message als stream?!
}
