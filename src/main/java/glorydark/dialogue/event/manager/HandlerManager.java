package glorydark.dialogue.event.manager;

import glorydark.dialogue.event.DialogueEvent;
import glorydark.dialogue.event.processor.EventProcessor;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.util.ArrayList;
import java.util.List;

/**
 * @author glorydark
 */
@SuppressWarnings("rawtypes")
public class HandlerManager {

    private static final Int2ObjectOpenHashMap<List<EventProcessor>> processors = new Int2ObjectOpenHashMap<>(10);

    public static void callEvent(DialogueEvent event) {
        List<EventProcessor> processorList = processors.getOrDefault(event.getId(), new ArrayList<>());
        for (EventProcessor eventProcessor : processorList) {
            eventProcessor.handle(event);
        }
    }

    public static void registerEventProcessors(EventProcessor... eventProcessors) {
        for (EventProcessor processor : eventProcessors) {
            registerEventProcessor(processor);
        }
        processors.trim();
    }

    protected static void registerEventProcessor(EventProcessor eventProcessor) {
        final int type = eventProcessor.getEventType();
        if (processors.containsKey(eventProcessor.getEventType())) {
            processors.put(type, new ArrayList<>());
        }
        processors.get(type).add(eventProcessor);
    }
}
