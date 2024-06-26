package glorydark.dialogue.action.requirement;

import cn.nukkit.Player;
import glorydark.dialogue.DialogueMain;
import glorydark.dialogue.action.requirement.parser.RequirementParser;
import glorydark.dialogue.action.requirement.parser.type.LimitedPlayTimeRequirementParser;
import glorydark.dialogue.api.DialogueAPI;
import glorydark.dialogue.data.DialogueData;

import java.util.List;

/**
 * @author glorydark
 */
public class LimitedPlayTimeRequirement extends Requirement {

    protected static final LimitedPlayTimeRequirementParser parser = new LimitedPlayTimeRequirementParser();

    protected int limitedPlayTime;

    public LimitedPlayTimeRequirement(boolean comparedValue, List<String> failedMessages, int limitedPlayTime) {
        super(comparedValue, failedMessages);
        this.limitedPlayTime = limitedPlayTime;
    }

    public static RequirementParser getRequirementParser() {
        return parser;
    }

    @Override
    public boolean canExecute(Player player, DialogueData dialogueData) {
        return (DialogueAPI.getPlayerPlayedTimes(player, dialogueData.getIdentifier()) <= limitedPlayTime) == comparedValue;
    }

    @Override
    public String getDefaultFailedMessage(Player player) {
        return DialogueMain.getLanguage().translateString(player, "tip_dialogue_requirement_failed_over_max_play_times");
    }
}
