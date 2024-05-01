package glorydark.dialogue.action.requirement.parser;

import cn.nukkit.Player;
import glorydark.dialogue.DialogueMain;
import glorydark.dialogue.action.requirement.Requirement;
import glorydark.dialogue.action.requirement.parser.type.CoolDownRequirementParser;
import glorydark.dialogue.api.DialogueAPI;
import glorydark.dialogue.data.DialogueData;

import java.util.List;

/**
 * @author glorydark
 */
public class CoolDownRequirement extends Requirement {

    protected static final CoolDownRequirementParser parser = new CoolDownRequirementParser();

    public long coolDownMillis;

    public CoolDownRequirement(long coolDownMillis, boolean comparedValue, List<String> failedMessages) {
        super(comparedValue, failedMessages);
        this.coolDownMillis = coolDownMillis;
    }

    public static RequirementParser getRequirementParser() {
        return parser;
    }

    @Override
    public boolean canExecute(Player player, DialogueData dialogueData) {
        if (this.coolDownMillis < 0) {
            return false;
        } else if (this.coolDownMillis == 0){
            return true;
        }
        long lastMillis = DialogueAPI.getPlayerLastPlayedMillis(player, dialogueData.getIdentifier());
        return System.currentTimeMillis() - lastMillis >= this.coolDownMillis;
    }

    @Override
    public String getDefaultFailedMessage(Player player) {
        return DialogueMain.getLanguage().translateString(player, "tip_dialogue_requirement_failed_in_cool_down");
    }
}
