package glorydark.dialogue.action.requirement.parser;

import glorydark.dialogue.action.requirement.Requirement;

import java.util.List;
import java.util.Map;

/**
 * @author glorydark
 */
public abstract class IncomparableRequirementParser extends RequirementParser {

    protected boolean setBool;

    public IncomparableRequirementParser(boolean setBool) {
        this.setBool = setBool;
    }

    public Requirement parse(List<String> failedMessages, Map<String, Object> map) {
        return super.parse(setBool, failedMessages, map);
    }
}
