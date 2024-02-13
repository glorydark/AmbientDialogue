package glorydark.dialogue.action.requirement.parser.type;

import glorydark.dialogue.action.requirement.LimitedPlayTimeRequirement;
import glorydark.dialogue.action.requirement.Requirement;
import glorydark.dialogue.action.requirement.parser.RequirementParser;

import java.util.List;
import java.util.Map;

/**
 * @author glorydark
 */
public class LimitedPlayTimeRequirementParser extends RequirementParser {

    @Override
    public Requirement parse(boolean comparedValue, List<String> failedMessages, Map<String, Object> map) {
        int limitedPlayTime = (int) map.getOrDefault("limited_times", 0);
        LimitedPlayTimeRequirement limitedPlayTimeRequirement = new LimitedPlayTimeRequirement(comparedValue, failedMessages, limitedPlayTime);
        limitedPlayTimeRequirement.setEnableDefaultFailedMessage((boolean) map.getOrDefault("enable_default_failed_message", true));
        return limitedPlayTimeRequirement;
    }
}
