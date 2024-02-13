package glorydark.dialogue.action.requirement.parser.base;

import glorydark.dialogue.action.requirement.Requirement;

import java.util.List;
import java.util.Map;

/**
 * @author glorydark
 */
public abstract class RequirementParser {

    public RequirementParser() {

    }

    public Requirement parse(boolean comparedValue, List<String> failedMessages, Map<String, Object> map) {
        return null;
    }
}
