package de.haw.eventalert.consumer.filtering.action;

import de.haw.eventalert.consumer.filtering.action.ledevent.LEDEventAction;

/**
 * Created by Tim on 12.09.2017.
 */
public class Actions {
    public static Action createLEDEventAction(String ledEvent) {
        return new LEDEventAction(ledEvent);
    }
}
