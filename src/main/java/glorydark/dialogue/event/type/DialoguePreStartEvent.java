package glorydark.dialogue.event.type;

import cn.nukkit.Player;
import glorydark.dialogue.data.DialogueData;
import glorydark.dialogue.event.Cancellable;
import glorydark.dialogue.event.DialogueEvent;

/**
 * @author glorydark
 */
public class DialoguePreStartEvent extends DialogueEvent implements Cancellable {

    public DialoguePreStartEvent(Player player, DialogueData dialogueData) {
        this.player = player;
        this.dialogueData = dialogueData;
    }
}
