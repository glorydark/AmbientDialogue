package glorydark.dialogue.event.processor;

import glorydark.dialogue.event.DialogueEvent;

/**
 * @author glorydark
 */
public abstract class EventProcessor<T extends DialogueEvent> {

    public abstract void handle(DialogueEvent dialogueEvent);

    public abstract int getEventType();
}
