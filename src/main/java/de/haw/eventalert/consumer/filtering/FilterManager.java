package de.haw.eventalert.consumer.filtering;

import de.haw.eventalert.consumer.filtering.filter.Filter;
import de.haw.eventalert.producer.email.MailMessage;
import org.apache.flink.shaded.com.google.common.collect.Lists;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tim on 12.09.2017.
 */
public class FilterManager {

    private static FilterManager instance;

    public static FilterManager getInstance() {
        if (instance == null) {
            instance = new FilterManager();
        }
        return instance;
    }

    private Map<String, List<Filter>> allFilters;

    private FilterManager() {
        allFilters = new HashMap<>();
    }

    public void addFilter(String eventType, Filter filter) {
        allFilters.computeIfAbsent(eventType, k -> Lists.newArrayList(filter));
    }

    public List<Filter> getFilters(String eventType) throws Exception {
        switch (eventType) {
            case MailMessage.EVENT_TYPE:
                return allFilters.get(eventType);
        }
        throw new Exception(String.format("No Filters for eventType %s", eventType));
    }

    public boolean hasFilters(String eventType) {
        return allFilters.get(eventType) != null;
    }
}
