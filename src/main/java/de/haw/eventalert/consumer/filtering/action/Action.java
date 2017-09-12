package de.haw.eventalert.consumer.filtering.action;

/**
 * Created by Tim on 12.09.2017.
 */
public interface Action {
    String getType();

    String getAction();

    String toString(); //TODO ONLY FOR TEST
}
