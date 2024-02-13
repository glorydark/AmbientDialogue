package glorydark.dialogue.action.requirement;

import glorydark.dialogue.DialogueMain;
import glorydark.dialogue.action.requirement.parser.FirstTimeRequirementParser;
import glorydark.dialogue.action.requirement.parser.LimitedPlayTimeRequirementParser;
import glorydark.dialogue.action.requirement.parser.OpeningHourRequirementParser;
import glorydark.dialogue.action.requirement.parser.base.RequirementParser;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author glorydark
 */
public class RequirementRegistry {

    public static final String KEY_COMPARED_VALUE = "compared_value";

    protected LinkedHashMap<String, RequirementParser> requirementParsers = new LinkedHashMap<>();

    public RequirementRegistry() {
        registerDefaultParsers();
    }

    protected void registerDefaultParsers() {
        registerParser("over_limited_play_time", new LimitedPlayTimeRequirementParser());
        registerParser("is_first_time", new FirstTimeRequirementParser());
        registerParser("is_opening_hours", new OpeningHourRequirementParser());
    }

    public void registerParser(String key, RequirementParser requirementParser) {
        requirementParsers.put(key, requirementParser);
    }

    public List<Requirement> parseRequirement(List<Map<String, Object>> maps) {
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

    public Requirement parseRequirement(Map<String, Object> map) {
        boolean comparedValue = (boolean) map.getOrDefault(KEY_COMPARED_VALUE, true);
        List<String> failedMessages = (List<String>) map.getOrDefault("failed_messages", new ArrayList<>());
        String type = (String) map.getOrDefault("type", "");
        if (requirementParsers.containsKey(type)) {
            return requirementParsers.get(type).parse(comparedValue, failedMessages, map);
        }
        DialogueMain.getInstance().getLogger().warning("Failed in parsing requirement caused by unknown type:" + type);
        return null;
    }
}
