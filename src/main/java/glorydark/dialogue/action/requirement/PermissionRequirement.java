package glorydark.dialogue.action.requirement;

import cn.nukkit.Player;
import cn.nukkit.utils.Config;
import glorydark.dialogue.DialogueMain;
import glorydark.dialogue.action.requirement.Requirement;
import glorydark.dialogue.action.requirement.parser.RequirementParser;
import glorydark.dialogue.action.requirement.parser.type.PermissionRequirementParser;
import glorydark.dialogue.data.DialogueData;

import java.util.ArrayList;
import java.util.List;

/**
 * @author glorydark
 */
public class PermissionRequirement extends Requirement {

    protected static final PermissionRequirementParser parser = new PermissionRequirementParser();
    private final List<String> permissions;

    public PermissionRequirement(boolean comparedValue, List<String> failedMessages, List<String> permissions) {
        super(comparedValue, failedMessages);
        this.permissions = permissions;
    }

    public static RequirementParser getRequirementParser() {
        return parser;
    }

    @Override
    public boolean canExecute(Player player, DialogueData dialogueData) {
        Config config = new Config(DialogueMain.getPath() + "/permission_groups.yml", Config.YAML);
        for (String permission : permissions) {
            if (new ArrayList<>(config.getStringList(permission)).contains(player.getName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getDefaultFailedMessage(Player player) {
        return DialogueMain.getLanguage().translateString(player, "tip_dialogue_requirement_failed_no_permission");
    }
}
