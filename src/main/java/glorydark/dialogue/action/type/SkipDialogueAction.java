package glorydark.dialogue.action.type;

import cn.nukkit.Player;
import glorydark.dialogue.action.ActionType;
import glorydark.dialogue.action.ExecuteAction;
import glorydark.dialogue.data.DialogueData;
import glorydark.dialogue.response.ResponseData;
import glorydark.dialogue.response.ResponseDataType;

/**
 * @author glorydark
 */
public class SkipDialogueAction extends ExecuteAction {

    public SkipDialogueAction() {
    }

    @Override
    public void execute(Player player, DialogueData dialogueData) {
        ResponseData responseData = new ResponseData();
        responseData.setResponse(ResponseDataType.BOOLEAN_SKIP_DIALOGUE, true);
    }

    @Override
    public ActionType getActionType() {
        return ActionType.SKIP_DIALOGUE;
    }
}
