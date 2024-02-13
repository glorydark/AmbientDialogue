package glorydark.dialogue.event.type;

import cn.nukkit.Player;
import glorydark.dialogue.data.DialogueData;
import glorydark.dialogue.event.DialogueEvent;

/**
 * @author glorydark
 */
public class DialogueEndEvent extends DialogueEvent {

    public DialogueEndEvent(Player player, DialogueData dialogueData) {
        this.player = player;
        this.dialogueData = dialogueData;
    }
}
