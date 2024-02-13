package glorydark.dialogue.action.requirement.parser;

import glorydark.dialogue.action.requirement.OpeningHourRequirement;
import glorydark.dialogue.action.requirement.Requirement;
import glorydark.dialogue.action.requirement.parser.base.RequirementParser;

import java.util.List;
import java.util.Map;

/**
 * @author glorydark
 */
public class OpeningHourRequirementParser extends RequirementParser {

    @Override
    public Requirement parse(boolean comparedValue, List<String> failedMessages, Map<String, Object> map) {
        String startTime = (String) map.getOrDefault("start_time", "");
        String endTime = (String) map.getOrDefault("end_time", "");
        OpeningHourRequirement openingHourRequirement = new OpeningHourRequirement(comparedValue, failedMessages, startTime, endTime);
        openingHourRequirement.setEnableDefaultFailedMessage((boolean) map.getOrDefault("enable_default_failed_message", true));
        return openingHourRequirement;
    }
}
