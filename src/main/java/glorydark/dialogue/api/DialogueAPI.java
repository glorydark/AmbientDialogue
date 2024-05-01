package glorydark.dialogue.api;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;
import glorydark.dialogue.DialogueMain;
import glorydark.dialogue.data.DialogueData;

import java.io.File;

/**
 * @author glorydark
 */
public class DialogueAPI {

    public static final String KEY_PLAYED_TIMES = "played_times";

    public static final String KEY_LAST_PLAYED_MILLIS = "last_played_millis";

    public static ConfigSection getPlayerConfigCache(Player player) {
        if (DialogueMain.getPlayerDataCaches().containsKey(player)) {
            return DialogueMain.getPlayerDataCaches().get(player);
        } else {
            File file = new File(DialogueMain.getPath() + "/players/" + player.getName() + ".yml");
            if (file.exists()) {
                ConfigSection section = new Config(file, Config.YAML).getRootSection();
                DialogueMain.getPlayerDataCaches().put(player, section);
                return section;
            } else {
                return new ConfigSection();
            }
        }
    }

    protected static Config getPlayerConfig(Player player) {
        return getPlayerConfig(player.getName());
    }

    // Marked protected to avoid some misuse by other developers to create more empty files
    protected static Config getPlayerConfig(String playerName) {
        return new Config(DialogueMain.getPath() + "/players/" + playerName + ".yml", Config.YAML);
    }

    public static void setPlayerPlayedTimes(Player player, String dialogueId, int times) {
        Config config = getPlayerConfig(player);
        ConfigSection dialoguePlayedTimes = config.getSection(KEY_PLAYED_TIMES);
        dialoguePlayedTimes.set(dialogueId, times);
        config.set(KEY_PLAYED_TIMES, dialoguePlayedTimes);
        config.save();
        DialogueMain.getPlayerDataCaches().put(player, config.getRootSection()); // 更新缓存，一定要更新
    }

    public static void addPlayerPlayedTimes(Player player, String dialogueId, int times) {
        Config config = getPlayerConfig(player);
        ConfigSection dialoguePlayedTimes = config.getSection(KEY_PLAYED_TIMES);
        dialoguePlayedTimes.set(dialogueId, dialoguePlayedTimes.getInt(dialogueId) + times);
        config.set(KEY_PLAYED_TIMES, dialoguePlayedTimes);
        config.save();
        DialogueMain.getPlayerDataCaches().put(player, config.getRootSection()); // 更新缓存，一定要更新
    }

    public static void clearPlayerPlayedTimes(Player player, String dialogueId) {
        Config config = getPlayerConfig(player);
        ConfigSection dialoguePlayedTimes = config.getSection(KEY_PLAYED_TIMES);
        dialoguePlayedTimes.set(dialogueId, 0);
        config.set(KEY_PLAYED_TIMES, dialoguePlayedTimes);
        config.save();
        DialogueMain.getPlayerDataCaches().put(player, config.getRootSection()); // 更新缓存，一定要更新
    }

    public static int getPlayerPlayedTimes(Player player, String dialogueId) {
        ConfigSection section = getPlayerConfigCache(player);
        ConfigSection dialoguePlayedTimes = section.getSection(KEY_PLAYED_TIMES);
        return dialoguePlayedTimes.getInt(dialogueId);
    }

    public static void setPlayerLastPlayedMillis(Player player, String dialogueId, long millis) {
        Config config = getPlayerConfig(player);
        ConfigSection dialoguePlayedTimes = config.getSection(KEY_LAST_PLAYED_MILLIS);
        dialoguePlayedTimes.set(dialogueId, millis);
        config.set(KEY_LAST_PLAYED_MILLIS, dialoguePlayedTimes);
        config.save();
        DialogueMain.getPlayerDataCaches().put(player, config.getRootSection()); // 更新缓存，一定要更新
    }

    public static long getPlayerLastPlayedMillis(Player player, String dialogueId) {
        ConfigSection section = getPlayerConfigCache(player);
        ConfigSection dialoguePlayedTimes = section.getSection(KEY_LAST_PLAYED_MILLIS);
        return Long.parseLong(dialoguePlayedTimes.getString(dialogueId));
    }

    public static void playDialogue(CommandSender commandSender, Player player, String dialogueFileName, boolean bypassCheck) {
        String playerName = player.getName();
        if (DialogueMain.getDialogues().containsKey(dialogueFileName)) {
            if (DialogueMain.getPlayerPlayingTasks().containsKey(player)) { // 避免玩家已经播放对话
                commandSender.sendMessage(DialogueMain.getLanguage().translateString(commandSender, "command_player_in_dialogue", playerName));
                return;
            }
            DialogueData data = DialogueMain.getDialogues().get(dialogueFileName);
            data.play(commandSender, player, bypassCheck);
        } else {
            commandSender.sendMessage(DialogueMain.getLanguage().translateString(commandSender, "command_dialogue_not_found", dialogueFileName));
        }
    }
}
