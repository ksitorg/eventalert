package de.haw.eventalert.global.entity.event;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.Serializable;

/**
 * Created by Tim on 19.08.2017.
 */
@JsonDeserialize(as = AlertEventImpl.class)
public interface AlertEvent extends Serializable {
    String getEventType(); //TODO vielleicht zu getType() umbenennen

    JsonNode getEventData(); //TODO vielleicht zu getData() oder getJSON() umbennnen
}
