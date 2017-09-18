package de.haw.eventalert.consumer;

import de.haw.eventalert.consumer.filtering.FilterManager;
import de.haw.eventalert.consumer.filtering.action.Action;
import de.haw.eventalert.consumer.filtering.action.Actions;
import de.haw.eventalert.consumer.filtering.filter.Condition;
import de.haw.eventalert.consumer.filtering.filter.MailMessageFilter;
import de.haw.eventalert.global.AlertEvents;
import de.haw.eventalert.global.entity.event.AlertEvent;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static de.haw.eventalert.consumer.filtering.filter.Condition.Type.CONTAINS;
import static de.haw.eventalert.consumer.filtering.filter.Condition.Type.STARTWITH;

/**
 * Created by Tim on 19.08.2017.
 */
public class AlertEventConsumerJob {
    private static final Logger LOG = LoggerFactory.getLogger(AlertEventConsumerJob.class);

    public static void main(String[] args) throws Exception {
        LOG.info("========== AlertEventConsumerJob started ==========");
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment().setParallelism(1);
        //Get all alert-events
        DataStream<String> jsonAlertEventStream = env.addSource(Consumer.createConsumer());
        //convert jsonAlertEvents to AlertEvent Objects
        DataStream<AlertEvent> alertEventStream = jsonAlertEventStream
                .flatMap(
                        AlertEvents.convertToAlertEvent()
                );

        //Filter Events

        //Tuple4<FilterParamName, contains/startWith/endWith/regex/greaterThan/lowerThan/equals, filter, LEDEffect>
        //Init filters
        FilterManager filterManager = FilterManager.getInstance();
        filterManager.addFilter(new MailMessageFilter("from", new Condition(CONTAINS, "tim@ksit.org"), Actions.createLEDEventAction("LED Leuchted blau")));
        filterManager.addFilter(new MailMessageFilter("to", new Condition(STARTWITH, "wittler"), Actions.createLEDEventAction("LED Leuchtet rot")));

        DataStream<Action> filteredAlertEvents = alertEventStream.flatMap((alertEvent, out) -> {

            //Check if the filterManger has filters for this eventType
            //Version 3: filterung anhand der alertEvent Felder
            if (filterManager.hasFilters(alertEvent.getEventType())) {
                alertEvent.getEventData().fieldNames().forEachRemaining(fieldName -> {
                    try {
                        filterManager.getFilters(alertEvent.getEventType(), fieldName).forEach(filter -> {
                            String fieldValue = alertEvent.getEventData().get(filter.getFieldName()).asText(); //TODO here we get all fields as text, no matter if its a date or another field
                            //check if the filter match with this field
                            switch (filter.getCondition().getType()) {
                                case CONTAINS:
                                    if (fieldValue.contains(filter.getCondition().getPattern()))
                                        out.collect(filter.getAction());
                                    return;
                                case STARTWITH:
                                    if (fieldValue.startsWith(filter.getCondition().getPattern()))
                                        out.collect(filter.getAction());
                                    return;
                                case ENDWITH:
                                    if (fieldValue.endsWith(filter.getCondition().getPattern()))
                                        out.collect(filter.getAction());
                                    return;
                                case REGEX: //TODO not supported
                                    break;
                            }
                        });
                    } catch (Exception e) {
                        LOG.error("error filtering alertEvent", e);
                    }
                });

            }
            //Version 2 filterung anhand der vorhandenen filter
//            if (filterManager.hasFilters(alertEvent.getEventType())) {
//                filterManager.getAllFiltersForEventType(alertEvent.getEventType()).stream()//TODO Test parralel.parallel()
//                        .filter(x -> alertEvent.getEventData().has(x.getFieldName()))
//                        .forEach(x -> {
//                            String fieldValue = alertEvent.getEventData().get(x.getFieldName()).asText(); //TODO here we get all fields as text, no matter if its a date or another field
//                            //check if the filter match with this field
//                            switch (x.getCondition().getType()) {
//                                case CONTAINS:
//                                    if (fieldValue.contains(x.getCondition().getPattern())) out.collect(x.getAction());
//                                    return;
//                                case STARTWITH:
//                                    if (fieldValue.startsWith(x.getCondition().getPattern())) out.collect(x.getAction());
//                                    return;
//                                case ENDWITH:
//                                    if (fieldValue.endsWith(x.getCondition().getPattern())) out.collect(x.getAction());
//                                    return;
//                                case REGEX: //TODO not supported
//                                    break;
//                            }
//                });

            //Version 1
//                for (Filter filter : filterManager.getAllFiltersForEventType(alertEvent.getEventType())) {
//                    //Check if the event has the fieldName
//                    if (alertEvent.getEventData().has(filter.getFieldName())) {
//                        //Get the value from this field as text
//                        String fieldValue = alertEvent.getEventData().get(filter.getFieldName()).asText(); //TODO here we get all fields as text, no matter if its a date or another field
//                        //check if the filter match with this field
//                        switch (filter.getType()) {
//                            case CONTAINS:
//                                if (fieldValue.contains(filter.getCondition())) out.collect(filter.getAction());
//                                return;
//                            case STARTWITH:
//                                if (fieldValue.startsWith(filter.getCondition())) out.collect(filter.getAction());
//                                return;
//                            case ENDWITH:
//                                if (fieldValue.endsWith(filter.getCondition())) out.collect(filter.getAction());
//                                return;
//                            case REGEX: //TODO not supported
//                                break;
//                        }
//                    }
//                }
//            }
        });
        filteredAlertEvents.print();
        env.execute();
    }
}
