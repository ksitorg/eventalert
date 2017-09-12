package de.haw.eventalert.consumer.filtering.filter;

import de.haw.eventalert.consumer.filtering.action.Action;

/**
 * Created by Tim on 12.09.2017.
 */
public class MailMessageFilter implements Filter {

    private Type type;
    private String fieldName;
    private String condition;
    private Action action;

    public MailMessageFilter(String fieldName, Type type, String condition, Action action) {
        this.type = type;
        this.fieldName = fieldName;
        this.condition = condition;
        this.action = action;
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public String getCondition() {
        return condition;
    }

    @Override
    public Action getAction() {
        return action;
    }
}
