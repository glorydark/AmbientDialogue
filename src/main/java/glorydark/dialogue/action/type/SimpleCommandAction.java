package glorydark.dialogue.action.type;

import cn.nukkit.Player;
import glorydark.dialogue.action.ActionType;
import glorydark.dialogue.action.ExecuteAction;
import glorydark.dialogue.data.DialogueData;
import glorydark.dialogue.utils.Utils;

import java.util.List;

/**
 * @author glorydark
 */
public class SimpleCommandAction extends ExecuteAction {

    protected List<String> commands;

    public SimpleCommandAction(List<String> commands) {
        this.commands = commands;
    }

    @Override
    public void execute(Player player, DialogueData dialogueData) {
        this.executeCommands(player);
    }

    protected void executeCommands(Player player) {
        for (String command : commands) {
            Utils.parseAndExecuteCommand(player, command);
        }
    }

    @Override
    public ActionType getActionType() {
        return ActionType.SIMPLE_COMMAND;
    }
}
