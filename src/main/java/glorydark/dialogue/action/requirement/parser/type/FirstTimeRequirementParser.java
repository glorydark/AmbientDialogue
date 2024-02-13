package glorydark.dialogue.action.requirement.parser.type;

import glorydark.dialogue.action.requirement.FirstTimeRequirement;
import glorydark.dialogue.action.requirement.Requirement;
import glorydark.dialogue.action.requirement.parser.RequirementParser;

import java.util.List;
import java.util.Map;

/**
 * @author glorydark
 */
public class FirstTimeRequirementParser extends RequirementParser {

    @Override
    public Requirement parse(boolean comparedValue, List<String> failedMessages, Map<String, Object> map) {
        FirstTimeRequirement firstTimeRequirement = new FirstTimeRequirement(comparedValue, failedMessages);
        firstTimeRequirement.setEnableDefaultFailedMessage((boolean) map.getOrDefault("enable_default_failed_message", true));
        return firstTimeRequirement;
    }
}
