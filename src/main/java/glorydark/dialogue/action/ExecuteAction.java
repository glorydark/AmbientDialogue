package glorydark.dialogue.action;

import cn.nukkit.Player;
import glorydark.dialogue.action.requirement.Requirement;
import glorydark.dialogue.action.type.SimpleCommandAction;
import glorydark.dialogue.action.type.SimpleMessageAction;
import glorydark.dialogue.action.type.SkipDialogueAction;
import glorydark.dialogue.data.DialogueData;
import glorydark.dialogue.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author glorydark
 */
public abstract class ExecuteAction {

    public List<Requirement> requirements = new ArrayList<>();

    public ExecuteAction() {

    }

    public static ExecuteAction parseExecuteActionFromMap(Map<String, Object> map) {
        switch ((String) map.getOrDefault("type", "")) {
            case "command":
                SimpleCommandAction simpleCommandAction = new SimpleCommandAction((List<String>) map.getOrDefault("commands", new ArrayList<>()));
                simpleCommandAction.setRequirements((List<Map<String, Object>>) map.getOrDefault("requirements", new ArrayList<>()));
                return simpleCommandAction;
            case "message":
                SimpleMessageAction simpleMessageAction = new SimpleMessageAction((List<String>) map.getOrDefault("messages", new ArrayList<>()));
                simpleMessageAction.setRequirements((List<Map<String, Object>>) map.getOrDefault("requirements", new ArrayList<>()));
                return simpleMessageAction;
            case "skip_dialogue":
                SkipDialogueAction skipDialogueAction = new SkipDialogueAction();
                skipDialogueAction.setRequirements((List<Map<String, Object>>) map.getOrDefault("requirements", new ArrayList<>()));
                return skipDialogueAction;
        }
        return null;
    }

    public boolean checkValid(Player player, DialogueData dialogueData) {
        boolean valid = true;
        for (Requirement requirement : requirements) {
            if (!requirement.canExecute(player, dialogueData)) {
                valid = false;
                if (requirement.isEnableDefaultFailedMessage()) {
                    Utils.sendPlayerMessage(player, requirement.getDefaultFailedMessage(player));
                }
                for (String failedMessage : requirement.getFailedMessages()) {
                    Utils.sendPlayerMessage(player, failedMessage);
                }
                break;
            }
        }
        return valid;
    }

    public void execute(Player player, DialogueData dialogueData) {

    }

    public void setRequirements(List<Map<String, Object>> requirements) {
        this.requirements.addAll(Requirement.parseRequirement(requirements));
    }

    public ActionType getActionType() {
        return ActionType.DEFAULT;
    }
}
