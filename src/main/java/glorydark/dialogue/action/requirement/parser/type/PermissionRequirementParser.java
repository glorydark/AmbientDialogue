package glorydark.dialogue.action.requirement.parser.type;

import glorydark.dialogue.action.requirement.Requirement;
import glorydark.dialogue.action.requirement.PermissionRequirement;
import glorydark.dialogue.action.requirement.parser.RequirementParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author glorydark
 */
public class PermissionRequirementParser extends RequirementParser {

    @Override
    public Requirement parse(boolean comparedValue, List<String> failedMessages, Map<String, Object> map) {
        List<String> permissions = (List<String>) map.getOrDefault("permissions", new ArrayList<>());
        PermissionRequirement permissionRequirement = new PermissionRequirement(comparedValue, failedMessages, permissions);
        permissionRequirement.setEnableDefaultFailedMessage((boolean) map.getOrDefault("enable_default_failed_message", true));
        return permissionRequirement;
    }
}
