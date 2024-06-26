package glorydark.dialogue.action.requirement.parser.type;

import glorydark.dialogue.action.requirement.Requirement;
import glorydark.dialogue.action.requirement.DailyRequirement;
import glorydark.dialogue.action.requirement.parser.IncomparableRequirementParser;

import java.util.List;
import java.util.Map;

/**
 * @author glorydark
 */
public class DailyRequirementParser extends IncomparableRequirementParser {

    public DailyRequirementParser() {
        super(true);
    }

    @Override
    public Requirement parse(boolean comparedValue, List<String> failedMessages, Map<String, Object> map) {
        DailyRequirement dailyRequirement = new DailyRequirement(true, failedMessages);
        dailyRequirement.setEnableDefaultFailedMessage((boolean) map.getOrDefault("enable_default_failed_message", true));
        return dailyRequirement;
    }
}
