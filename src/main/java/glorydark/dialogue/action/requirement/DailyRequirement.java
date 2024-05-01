package glorydark.dialogue.action.requirement;

import cn.nukkit.Player;
import glorydark.dialogue.DialogueMain;
import glorydark.dialogue.action.requirement.Requirement;
import glorydark.dialogue.action.requirement.parser.RequirementParser;
import glorydark.dialogue.action.requirement.parser.type.CoolDownRequirementParser;
import glorydark.dialogue.action.requirement.parser.type.DailyRequirementParser;
import glorydark.dialogue.api.DialogueAPI;
import glorydark.dialogue.data.DialogueData;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author glorydark
 */
public class DailyRequirement extends Requirement {

    protected static final DailyRequirementParser parser = new DailyRequirementParser();

    public DailyRequirement(boolean comparedValue, List<String> failedMessages) {
        super(comparedValue, failedMessages);
    }

    public static RequirementParser getRequirementParser() {
        return parser;
    }

    @Override
    public boolean canExecute(Player player, DialogueData dialogueData) {
        long lastMillis = DialogueAPI.getPlayerLastPlayedMillis(player, dialogueData.getIdentifier());
        boolean b1 =  Calendar.getInstance().get(Calendar.DAY_OF_MONTH) == new Calendar.Builder().setInstant(new Date(lastMillis)).build().get(Calendar.DAY_OF_MONTH);
        boolean b2 = System.currentTimeMillis() - lastMillis <= 86400000;
        return !b2 || !b1;
    }

    @Override
    public String getDefaultFailedMessage(Player player) {
        return DialogueMain.getLanguage().translateString(player, "tip_dialogue_requirement_failed_in_cool_down");
    }
}
