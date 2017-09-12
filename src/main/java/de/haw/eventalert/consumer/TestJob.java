package de.haw.eventalert.consumer;

import de.haw.eventalert.global.AlertEvents;
import de.haw.eventalert.global.entity.event.AlertEvent;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * Created by Tim on 08.09.2017.
 */
public class TestJob {
    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment().setParallelism(1);

        DataStream<String> testInput = env.fromElements("{falsche event}", "{eventType}", "");

        DataStream<AlertEvent> testOutput = testInput.map(AlertEvents::toAlertEvent);
        testOutput.print();

        env.execute("Test Job");
    }
}
