package de.haw.eventalert.global.entity.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by Tim on 19.08.2017.
 */
public class AlertEventImpl implements AlertEvent {
    private String eventType;
    private JsonNode eventData;

    //Jackson needs a default constructor
    public AlertEventImpl() {
    }

    public AlertEventImpl(String eventType, Object eventObj) throws JsonProcessingException {
        this.eventType = eventType;
        ObjectMapper jsonParser = new ObjectMapper();
        this.eventData = jsonParser.valueToTree(eventObj);
    }

    @Override
    public String getEventType() {
        return eventType;
    }

    @Override
    public JsonNode getEventData() {
        return eventData;
    }

    @Override
    public String toString() {
        return "AlertEventImpl{" +
                "eventType='" + eventType + '\'' +
                ", eventData=" + eventData +
                '}';
    }
}
