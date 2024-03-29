package glorydark.dialogue.action.requirement;

import cn.nukkit.Player;
import glorydark.dialogue.DialogueMain;
import glorydark.dialogue.action.requirement.parser.RequirementParser;
import glorydark.dialogue.action.requirement.parser.type.OpeningHourRequirementParser;
import glorydark.dialogue.data.DialogueData;
import glorydark.dialogue.utils.Utils;

import java.util.List;

/**
 * @author glorydark
 */
public class OpeningHourRequirement extends Requirement {

    protected static final OpeningHourRequirementParser parser = new OpeningHourRequirementParser();

    protected long startTime;

    protected long endTime;

    public OpeningHourRequirement(boolean comparedValue, List<String> failedMessages, String startTime, String endTime) {
        super(comparedValue, failedMessages);
        try {
            this.startTime = Utils.stringToDate(startTime).getTime();
            this.endTime = Utils.stringToDate(endTime).getTime();
        } catch (Exception e) {
            this.valid = false;
        }
    }

    public static RequirementParser getRequirementParser() {
        return parser;
    }

    public boolean isValid() {
        return this.valid;
    }

    @Override
    public boolean canExecute(Player player, DialogueData dialogueData) {
        long current = System.currentTimeMillis();
        boolean startTimeCheck = current <= 0 || current >= startTime;
        boolean endTimeCheck = current <= 0 || current <= endTime;
        return (startTimeCheck && endTimeCheck) == comparedValue;
    }

    @Override
    public String getDefaultFailedMessage(Player player) {
        return DialogueMain.getLanguage().translateString(player, "tip_dialogue_requirement_failed_not_in_opening_hours");
    }
}
