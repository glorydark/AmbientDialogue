package glorydark.dialogue.event.type;

import cn.nukkit.Player;
import glorydark.dialogue.data.DialogueData;
import glorydark.dialogue.event.DialogueEvent;

/**
 * @author glorydark
 */
public class DialogueSkipEvent extends DialogueEvent {

    public DialogueSkipEvent(Player player, DialogueData dialogueData) {
        this.player = player;
        this.dialogueData = dialogueData;
    }
}
