package glorydark.dialogue.action.requirement;

import cn.nukkit.Player;
import glorydark.dialogue.data.DialogueData;

import java.util.List;

/**
 * @author glorydark
 */
public abstract class Requirement {

    protected boolean valid = true;
    protected boolean comparedValue;
    protected List<String> failedMessages;
    protected boolean enableDefaultFailedMessage = true;

    public Requirement(boolean comparedValue, List<String> failedMessages) {
        this.comparedValue = comparedValue;
        this.failedMessages = failedMessages;
    }

    public boolean canExecute(Player player, DialogueData dialogueData) {
        return true;
    }

    public String getDefaultFailedMessage(Player player) {
        return "";
    }

    public boolean isValid() {
        return true;
    }

    public boolean isEnableDefaultFailedMessage() {
        return enableDefaultFailedMessage;
    }

    public void setEnableDefaultFailedMessage(boolean enableDefaultFailedMessage) {
        this.enableDefaultFailedMessage = enableDefaultFailedMessage;
    }

    public List<String> getFailedMessages() {
        return failedMessages;
    }
}
