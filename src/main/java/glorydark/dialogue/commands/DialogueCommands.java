package glorydark.dialogue.commands;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.utils.Config;
import glorydark.dialogue.DialogueMain;
import glorydark.dialogue.data.DialogueData;
import glorydark.dialogue.form.FormHelper;
import glorydark.dialogue.utils.Language;

import java.io.File;

/**
 * @author glorydark
 * @date {2023/6/30} {0:36}
 */
public class DialogueCommands extends Command {

    public DialogueCommands(String name) {
        super(name);
        this.commandParameters.clear();
        this.commandParameters.put("create", new CommandParameter[]{
                CommandParameter.newEnum("create", new String[]{"create"}),
                CommandParameter.newType(DialogueMain.getLanguage().translateString("command_parameter_new_dialogue_name"), CommandParamType.TEXT)
        });
        this.commandParameters.put("edit", new CommandParameter[]{
                CommandParameter.newEnum("edit", new String[]{"edit"})
        });
        this.commandParameters.put("help", new CommandParameter[]{
                CommandParameter.newEnum("help", new String[]{"help"})
        });
        this.commandParameters.put("play", new CommandParameter[]{
                CommandParameter.newEnum("play", new String[]{"play"}),
                CommandParameter.newEnum(DialogueMain.getLanguage().translateString("command_parameter_play_player_name"), CommandParameter.ARG_TYPE_TARGET),
                CommandParameter.newEnum(DialogueMain.getLanguage().translateString("command_parameter_play_dialogue_name"), DialogueMain.getDialogues().keySet().toArray(new String[0]))
        });
        this.commandParameters.put("reload", new CommandParameter[]{
                CommandParameter.newEnum("reload", new String[]{"reload"})
        });
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if (strings.length > 0) {
            switch (strings[0]) {
                case "create":
                    if (commandSender.isPlayer() && !commandSender.isOp()) { // 普通玩家不允许reload
                        return true;
                    }
                    if (strings.length != 2) {
                        Server.getInstance().dispatchCommand(commandSender, "dialogue help");
                    } else {
                        String fileName = strings[1] + ".yml"; // 保存文件名
                        if (DialogueMain.getDialogues().containsKey(fileName)) { // 如果已经存在，提示错误
                            commandSender.sendMessage(DialogueMain.getLanguage().translateString("command_dialogue_existed", strings[1]));
                        } else { // 未存在，将resources里面的default.yml保存为新的文件
                            File file = new File(DialogueMain.getPath() + "/dialogues/" + fileName);
                            if (DialogueMain.getInstance().saveResource("default_dialogue_zh_CN.yml", "dialogues/" + file.getName(), false)) {
                                DialogueMain.getInstance().loadDialogue(file);
                            }
                            commandSender.sendMessage(DialogueMain.getLanguage().translateString("command_create_success", strings[1]));
                        }
                    }
                    break;
                case "help":
                    commandSender.sendMessage(DialogueMain.getLanguage().translateString("command_help_create"));
                    commandSender.sendMessage(DialogueMain.getLanguage().translateString("command_help_play"));
                    commandSender.sendMessage(DialogueMain.getLanguage().translateString("command_help_reload"));
                    commandSender.sendMessage(DialogueMain.getLanguage().translateString("command_help_edit"));
                    break;
                case "reload":
                    if (commandSender.isPlayer() && !commandSender.isOp()) { // 普通玩家不允许reload
                        return true;
                    }
                    Config config = new Config(DialogueMain.getPath() + "/config.yml", Config.YAML);
                    DialogueMain.lineMaxLength = config.getInt("line_max_length", 64);
                    DialogueMain.language = new Language(DialogueMain.getPath() + "/languages/");
                    DialogueMain.invincibleInDialogue = config.getBoolean("invincible_in_dialogue", false);
                    DialogueMain.getInstance().loadAllDialogues();
                    commandSender.sendMessage(DialogueMain.getLanguage().translateString("command_reload_success"));
                    break;
                case "play": // dialogue play BizarreDark test.yml
                    if (commandSender.isPlayer() && !commandSender.isOp()) {
                        commandSender.sendMessage(DialogueMain.getLanguage().translateString((Player) commandSender, "command_not_authorized"));
                        return true;
                    }
                    if (strings.length == 3) {
                        String fileName = strings[2] + ".yml";
                        if (DialogueMain.getDialogues().containsKey(fileName)) {
                            String playerName = strings[1];
                            Player player = Server.getInstance().getPlayer(playerName);
                            if (player != null) {
                                if (DialogueMain.getPlayerPlayingTasks().containsKey(player)) { // 避免玩家已经播放对话
                                    commandSender.sendMessage(DialogueMain.getLanguage().translateString((Player) commandSender, "command_player_in_dialogue", playerName));
                                    return true;
                                }
                                DialogueData data = DialogueMain.getDialogues().get(fileName);
                                data.play(commandSender, player);
                            } else {
                                commandSender.sendMessage(DialogueMain.getLanguage().translateString((Player) commandSender, "command_player_not_found", playerName));
                            }
                        } else {
                            commandSender.sendMessage(DialogueMain.getLanguage().translateString((Player) commandSender, "command_dialogue_not_found", fileName));
                        }
                    } else {
                        Server.getInstance().dispatchCommand(commandSender, "dialogue help");
                    }
                    break;
                case "edit":
                    if (commandSender.isOp()) {
                        FormHelper.showDialogueSelect((Player) commandSender);
                    } else if (!commandSender.isPlayer()) {
                        commandSender.sendMessage(DialogueMain.getLanguage().translateString("command_use_in_game"));
                    }
                    break;
            }
        }
        return true;
    }
}
