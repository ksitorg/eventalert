package de.haw.eventalert.producer.email.imap;

import de.haw.eventalert.global.AlertEvents;
import de.haw.eventalert.global.entity.event.AlertEvent;
import de.haw.eventalert.producer.Producer;
import de.haw.eventalert.producer.email.MailMessage;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by Tim on 18.08.2017.
 */
public class IMAPAlertEventProducerJob {
    private static final Logger LOG = LoggerFactory.getLogger(IMAPAlertEventProducerJob.class);

    public static void main(String[] args) throws Exception {
        LOG.info("========== IMAPAlertEventProducerJob started ==========");

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment().setParallelism(1);

        ImapSource imapSource = new ImapSource("default-imap.properties");
        DataStream<MailMessage> mailMessageSource = env.addSource(imapSource);

        //Convert Source to Alert event...
        DataStream<AlertEvent> alertEventDataStream = mailMessageSource.flatMap((mailMessage, out) -> {
            try {
                out.collect(AlertEvents.createEvent(MailMessage.EVENT_TYPE, mailMessage));
            } catch (Exception e) {
                LOG.error("Error creating alertEvent out of mailMessage({})", mailMessage, e);
            }
        });

        //Add stream to kafka
        Producer.provideDataStream(alertEventDataStream);

        env.execute("IMAPAlertEventProducerJob");
    }
}
