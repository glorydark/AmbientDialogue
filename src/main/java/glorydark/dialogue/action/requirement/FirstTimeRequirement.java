package glorydark.dialogue.action.requirement;

import cn.nukkit.Player;
import glorydark.dialogue.DialogueMain;
import glorydark.dialogue.action.requirement.parser.RequirementParser;
import glorydark.dialogue.action.requirement.parser.type.FirstTimeRequirementParser;
import glorydark.dialogue.api.DialogueAPI;
import glorydark.dialogue.data.DialogueData;

import java.util.List;

/**
 * @author glorydark
 */
public class FirstTimeRequirement extends Requirement {

    protected static final FirstTimeRequirementParser parser = new FirstTimeRequirementParser();

    public FirstTimeRequirement(boolean comparedValue, List<String> failedMessages) {
        super(comparedValue, failedMessages);
    }

    public static RequirementParser getRequirementParser() {
        return parser;
    }

    @Override
    public boolean canExecute(Player player, DialogueData dialogueData) {
        return (DialogueAPI.getPlayerPlayedTimes(player, dialogueData.getIdentifier()) > 0) != comparedValue;
    }

    @Override
    public String getDefaultFailedMessage(Player player) {
        return DialogueMain.getLanguage().translateString(player, "tip_dialogue_requirement_failed_only_play_once");
    }
}
