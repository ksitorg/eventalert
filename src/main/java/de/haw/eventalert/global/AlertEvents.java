package de.haw.eventalert.global;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.haw.eventalert.global.entity.event.AlertEvent;
import de.haw.eventalert.global.entity.event.AlertEventImpl;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by Tim on 19.08.2017.
 */
public class AlertEvents {

    private static final Logger LOG = LoggerFactory.getLogger(AlertEvents.class);

    public static AlertEvent createEvent(String eventType, Object eventObject) throws JsonProcessingException {
        return new AlertEventImpl(eventType, eventObject);
    }

    public static String toJSONString(AlertEvent alertEvent) throws JsonProcessingException {
        return getParser().writeValueAsString(alertEvent);
    }

    public static AlertEvent toAlertEvent(String jsonAlertEvent) throws IOException {
        return getParser().readValue(jsonAlertEvent, AlertEvent.class);
    }

    public static FlatMapFunction<AlertEvent, String> convertToJSONString() {
        return (alertEvent, out) -> {
            try {
                out.collect(toJSONString(alertEvent));
            } catch (JsonProcessingException e) {
                LOG.error("Could not convert alertEvent({}) to jsonAlertEvent", alertEvent, e);
            }
        };
    }

    public static FlatMapFunction<String, AlertEvent> convertToAlertEvent() {
        return (jsonAlertEvent, out) -> {
            try {
                out.collect(toAlertEvent(jsonAlertEvent));
            } catch (IOException e) {
                LOG.error("Could not convert jsonAlertEvent({}) to alertEvent", jsonAlertEvent, e);
            }
        };
    }

    private static ObjectMapper jsonParser;

    private static ObjectMapper getParser() {
        if (jsonParser == null) {
            jsonParser = new ObjectMapper();
        }
        return jsonParser;
    }

}
