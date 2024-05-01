package glorydark.dialogue.action.requirement.parser.type;

import glorydark.dialogue.action.requirement.DialoguePremiseRequirement;
import glorydark.dialogue.action.requirement.LimitedPlayTimeRequirement;
import glorydark.dialogue.action.requirement.Requirement;
import glorydark.dialogue.action.requirement.parser.RequirementParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author glorydark
 */
public class DialoguePremiseRequirementParser extends RequirementParser {

    @Override
    public Requirement parse(boolean comparedValue, List<String> failedMessages, Map<String, Object> map) {
        List<String> dialogueIds = (List<String>) map.getOrDefault("dialogues", new ArrayList<>());
        DialoguePremiseRequirement dialoguePremiseRequirement = new DialoguePremiseRequirement(dialogueIds, comparedValue, failedMessages);
        dialoguePremiseRequirement.setEnableDefaultFailedMessage((boolean) map.getOrDefault("enable_default_failed_message", true));
        return dialoguePremiseRequirement;
    }
}
