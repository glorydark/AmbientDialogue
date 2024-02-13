package glorydark.dialogue.action.requirement;

import cn.nukkit.Player;
import glorydark.dialogue.DialogueMain;
import glorydark.dialogue.data.DialogueData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author glorydark
 */
public abstract class Requirement {

    public static final String KEY_COMPARED_VALUE = "compared_value";
    protected boolean valid = true;
    protected boolean comparedValue;
    protected List<String> failedMessages;
    protected boolean enableDefaultFailedMessage = true;

    public Requirement(boolean comparedValue, List<String> failedMessages) {
        this.comparedValue = comparedValue;
        this.failedMessages = failedMessages;
    }

    public static List<Requirement> parseRequirement(List<Map<String, Object>> maps) {
        List<Requirement> requirements = new ArrayList<>();
        for (Map<String, Object> map : maps) {
            Requirement requirement = parseRequirement(map);
            if (requirement == null) {
                DialogueMain.getInstance().getLogger().warning("Failed in parsing requirement map: " + map);
            }
            requirements.add(requirement);
        }
        return requirements;
    }

    public static Requirement parseRequirement(Map<String, Object> map) {
        boolean comparedValue = (boolean) map.getOrDefault(KEY_COMPARED_VALUE, true);
        List<String> failedMessages = (List<String>) map.getOrDefault("failed_messages", new ArrayList<>());
        switch ((String) map.getOrDefault("type", "")) {
            case "over_limited_play_time":
                int limitedPlayTime = (int) map.getOrDefault("limited_times", 0);
                LimitedPlayTimeRequirement limitedPlayTimeRequirement = new LimitedPlayTimeRequirement(comparedValue, failedMessages, limitedPlayTime);
                limitedPlayTimeRequirement.enableDefaultFailedMessage = (boolean) map.getOrDefault("enable_default_failed_message", true);
                return limitedPlayTimeRequirement;
            case "is_first_time":
                FirstTimeRequirement firstTimeRequirement = new FirstTimeRequirement(comparedValue, failedMessages);
                firstTimeRequirement.enableDefaultFailedMessage = (boolean) map.getOrDefault("enable_default_failed_message", true);
                return firstTimeRequirement;
            case "is_opening_hours":
                String startTime = (String) map.getOrDefault("start_time", "");
                String endTime = (String) map.getOrDefault("end_time", "");
                OpeningHourRequirement openingHourRequirement = new OpeningHourRequirement(comparedValue, failedMessages, startTime, endTime);
                openingHourRequirement.enableDefaultFailedMessage = (boolean) map.getOrDefault("enable_default_failed_message", true);
                return openingHourRequirement;
        }
        return null;
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
