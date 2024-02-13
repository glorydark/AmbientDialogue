package glorydark.dialogue.event.type;

import cn.nukkit.Player;
import glorydark.dialogue.data.DialogueData;
import glorydark.dialogue.event.DialogueEvent;

/**
 * @author glorydark
 */
public class DialogueTickEvent extends DialogueEvent {

    private final int tick;

    public DialogueTickEvent(Player player, DialogueData dialogueData, int tick) {
        this.tick = tick;
        this.player = player;
        this.dialogueData = dialogueData;
    }

    public int getTick() {
        return tick;
    }
}
