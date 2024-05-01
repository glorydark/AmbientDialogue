package glorydark.dialogue.action.requirement.parser.type;

import glorydark.dialogue.action.requirement.Requirement;
import glorydark.dialogue.action.requirement.parser.CoolDownRequirement;
import glorydark.dialogue.action.requirement.parser.IncomparableRequirementParser;

import java.util.List;
import java.util.Map;

/**
 * @author glorydark
 */
public class CoolDownRequirementParser extends IncomparableRequirementParser {

    public CoolDownRequirementParser() {
        super(true);
    }

    @Override
    public Requirement parse(boolean comparedValue, List<String> failedMessages, Map<String, Object> map) {
        long coolDownMillis = (long) map.getOrDefault("cool_down_millis", 0);
        CoolDownRequirement coolDownRequirement = new CoolDownRequirement(coolDownMillis, true, failedMessages);
        coolDownRequirement.setEnableDefaultFailedMessage((boolean) map.getOrDefault("enable_default_failed_message", true));
        return coolDownRequirement;
    }
}
