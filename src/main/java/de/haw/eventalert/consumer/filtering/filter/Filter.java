package de.haw.eventalert.consumer.filtering.filter;

import de.haw.eventalert.consumer.filtering.action.Action;

/**
 * Created by Tim on 12.09.2017.
 */
public interface Filter {
    String getEventType();

    String getFieldName();

    Condition getCondition();

    Action getAction();
}
