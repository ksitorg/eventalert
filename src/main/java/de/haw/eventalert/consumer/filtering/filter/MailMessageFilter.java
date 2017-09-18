package de.haw.eventalert.consumer.filtering.filter;

import de.haw.eventalert.consumer.filtering.action.Action;
import de.haw.eventalert.producer.email.MailMessage;

/**
 * Created by Tim on 12.09.2017.
 */
public class MailMessageFilter implements Filter {

    private String fieldName;
    private Condition condition;
    private Action action;

    public MailMessageFilter(String fieldName, Condition condition, Action action) {
        this.fieldName = fieldName;
        this.condition = condition;
        this.action = action;
    }

    @Override
    public String getEventType() {
        return MailMessage.EVENT_TYPE;
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }

    @Override
    public Condition getCondition() {
        return condition;
    }

    @Override
    public Action getAction() {
        return action;
    }
}
