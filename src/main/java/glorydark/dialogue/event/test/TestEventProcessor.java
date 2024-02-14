package glorydark.dialogue.event.test;

import cn.nukkit.Player;
import cn.nukkit.level.particle.ExplodeParticle;
import glorydark.dialogue.event.DialogueEvent;
import glorydark.dialogue.event.processor.EventProcessor;
import glorydark.dialogue.event.type.DialogueEndEvent;

/**
 * @author glorydark
 */
public class TestEventProcessor extends EventProcessor<DialogueEndEvent> {
    @Override
    public void handle(DialogueEvent dialogueEvent) {
        Player player = dialogueEvent.getPlayer();
        player.getLevel().addParticle(new ExplodeParticle(player));
    }

    @Override
    public int getEventType() {
        return DialogueEvent.EVENT_END;
    }
}
