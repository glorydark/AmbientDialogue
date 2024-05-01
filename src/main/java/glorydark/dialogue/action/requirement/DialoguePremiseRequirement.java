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
public class DialoguePremiseRequirement extends Requirement {

    protected static final FirstTimeRequirementParser parser = new FirstTimeRequirementParser();

    protected List<String> dialogueIds;

    public DialoguePremiseRequirement(List<String> dialogueIds, boolean comparedValue, List<String> failedMessages) {
        super(comparedValue, failedMessages);
        this.dialogueIds = dialogueIds;
    }

    public static RequirementParser getRequirementParser() {
        return parser;
    }

    @Override
    public boolean canExecute(Player player, DialogueData dialogueData) {
        for (String dialogueId : dialogueIds) {
            if (DialogueAPI.getPlayerPlayedTimes(player, dialogueId) != 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String getDefaultFailedMessage(Player player) {
        return DialogueMain.getLanguage().translateString(player, "tip_dialogue_requirement_failed_only_play_once");
    }
}
