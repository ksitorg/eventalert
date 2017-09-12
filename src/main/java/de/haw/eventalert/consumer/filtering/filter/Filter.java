package de.haw.eventalert.consumer.filtering.filter;

import de.haw.eventalert.consumer.filtering.action.Action;

/**
 * Created by Tim on 12.09.2017.
 */
public interface Filter {
    String getFieldName();

    Type getType();

    String getCondition();

    Action getAction();

    enum Type {
        CONTAINS,
        STARTWITH,
        ENDWITH,
        REGEX
    }
}
