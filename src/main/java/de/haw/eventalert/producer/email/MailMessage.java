package de.haw.eventalert.producer.email;

import de.haw.eventalert.global.entity.event.FilterParamType;
import org.apache.flink.hadoop.shaded.com.google.common.collect.ImmutableMap;

import java.util.Map;

/**
 * Created by Tim on 18.08.2017.
 */
public interface MailMessage {
    String EVENT_TYPE = "email";
    Map<String, FilterParamType> FILTER_PARAM_LIST = ImmutableMap.<String, FilterParamType>builder()
            .put("imapAccountId", FilterParamType.NUMBER)
            .put("from", FilterParamType.TEXT)
            .put("to", FilterParamType.TEXT)
            .put("replyTo", FilterParamType.TEXT)
            .put("cc", FilterParamType.TEXT)
            .put("bcc", FilterParamType.TEXT)
            .put("sendTime", FilterParamType.TIMESTAMP)
            .put("receivedTime", FilterParamType.TIMESTAMP)
            .put("subject", FilterParamType.TEXT)
            .put("content", FilterParamType.TEXT)
            .build();

    Long getImapAccountId();

    String getFrom();

    String getTo();

    String getReplyTo();

    String getCc();

    String getBcc();

    Long getSendTime();

    Long getReceivedTime();

    String getSubject();

    String getContent();
}
