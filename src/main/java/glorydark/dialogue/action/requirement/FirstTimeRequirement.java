package glorydark.dialogue.action.requirement;

import cn.nukkit.Player;
import glorydark.dialogue.DialogueMain;
import glorydark.dialogue.data.DialogueData;

import java.util.List;

/**
 * @author glorydark
 */
public class FirstTimeRequirement extends Requirement {

    public FirstTimeRequirement(boolean comparedValue, List<String> failedMessages) {
        super(comparedValue, failedMessages);
    }

    @Override
    public boolean canExecute(Player player, DialogueData dialogueData) {
        return dialogueData.getFinishPlayerData().containsKey(player.getName()) == comparedValue;
    }

    @Override
    public String getDefaultFailedMessage(Player player) {
        return DialogueMain.getLanguage().translateString(player, "tip_dialogue_requirement_failed_only_play_once");
    }
}
