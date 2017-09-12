package de.haw.eventalert.consumer.filtering.action.ledevent;

import de.haw.eventalert.consumer.filtering.action.Action;

/**
 * Created by Tim on 12.09.2017.
 */
public class LEDEventAction implements Action { //TODO make a real LEDEventAction

    private String testLEDEventString;

    public LEDEventAction(String testLEDEventString) {
        this.testLEDEventString = testLEDEventString;
    }

    @Override
    public String getType() {
        return "ledevent"; //TODO
    }

    @Override
    public String getAction() {
        return testLEDEventString;
    }

    public String getLedEvent() {
        return testLEDEventString;
    }

    public void setLedEvent(String ledEvent) {
        this.testLEDEventString = ledEvent;
    }

    public String toString() {
        return String.format("%s : %s", getType(), getAction());
    }
}
