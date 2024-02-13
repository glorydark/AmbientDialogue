package glorydark.dialogue.event;

import cn.nukkit.Player;
import glorydark.dialogue.DialogueMain;
import glorydark.dialogue.data.DialogueData;

/**
 * @author glorydark
 */
public abstract class DialogueEvent {

    public static final int EVENT_PRE_START = 1;

    public static final int EVENT_TICK_IN_DIALOGUE = 2;

    public static final int EVENT_END = 3;

    protected DialogueData dialogueData;

    protected Player player;
    private boolean isCancelled = false;

    public DialogueEvent() {
    }

    public DialogueData getDialogueData() {
        return dialogueData;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void setCancelled(boolean cancelled) {
        if (this instanceof Cancellable) {
            this.isCancelled = cancelled;
        } else {
            DialogueMain.getInstance().getLogger().error("Cannot cancel event " + this.getClass().getName() + ": event is not cancellable");
        }
    }

    public int getId() {
        return 0;
    }
}
