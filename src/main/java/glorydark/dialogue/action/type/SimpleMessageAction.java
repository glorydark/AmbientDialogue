package glorydark.dialogue.action.type;

import cn.nukkit.Player;
import glorydark.dialogue.action.ActionType;
import glorydark.dialogue.action.ExecuteAction;
import glorydark.dialogue.data.DialogueData;

import java.util.List;

/**
 * @author glorydark
 */
public class SimpleMessageAction extends ExecuteAction {

    protected List<String> messages;

    public SimpleMessageAction(List<String> messages) {
        this.messages = messages;
    }

    @Override
    public void execute(Player player, DialogueData dialogueData) {
        this.executeMessages(player);
    }

    protected void executeMessages(Player player) {
        for (String message : messages) {
            player.sendMessage(message);
        }
    }

    @Override
    public ActionType getActionType() {
        return ActionType.SIMPLE_MESSAGE;
    }
}