package glorydark.dialogue.commands;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;
import cn.nukkit.utils.TextFormat;
import glorydark.dialogue.DialogueMain;
import glorydark.dialogue.api.DialogueAPI;
import glorydark.dialogue.data.DialogueData;
import glorydark.dialogue.form.FormHelper;
import glorydark.dialogue.utils.Language;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

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
                CommandParameter.newType("dialogue_name", CommandParamType.TEXT)
        });
        this.commandParameters.put("edit", new CommandParameter[]{
                CommandParameter.newEnum("edit", new String[]{"edit"})
        });
        this.commandParameters.put("help", new CommandParameter[]{
                CommandParameter.newEnum("help", new String[]{"help"})
        });
        this.commandParameters.put("play", new CommandParameter[]{
                CommandParameter.newEnum("play", new String[]{"play"}),
                CommandParameter.newType("player", CommandParamType.TEXT),
                CommandParameter.newType("dialogue_name", CommandParamType.TEXT)
        });
        this.commandParameters.put("permission", new CommandParameter[]{
                CommandParameter.newEnum("permission", new String[]{"permission"}),
                CommandParameter.newEnum("type", new String[]{"add", "remove"}),
                CommandParameter.newType("player", CommandParamType.TEXT),
                CommandParameter.newType("permission_name", CommandParamType.TEXT)
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
                            commandSender.sendMessage(DialogueMain.getLanguage().translateString(commandSender, "command_dialogue_existed", strings[1]));
                        } else { // 未存在，将resources里面的default.yml保存为新的文件
                            File file = new File(DialogueMain.getPath() + "/dialogues/" + fileName);
                            Config config = new Config(file, 2, new ConfigSection() {
                                {
                                    this.put("player_still", true);
                                    this.put("lines", new ArrayList<LinkedHashMap<String, Object>>() {
                                        {
                                            this.add(new LinkedHashMap<String, Object>() {
                                                {
                                                    this.put("text", "Men always remember love because of romance only\\n233");
                                                    this.put("speaker_name", "test1");
                                                    this.put("exist_ticks", 50);
                                                    this.put("play_ticks", 40);
                                                }
                                            });
                                        }
                                    });
                                    this.put("open_requirements", new ArrayList<>());
                                    this.put("prestart_actions", new ArrayList<>());
                                    this.put("tick_actions", new ArrayList<>());
                                    this.put("end_actions", new ArrayList<>());
                                }
                            });
                            if (DialogueMain.getInstance().saveResource("default_dialogue_zh_CN.yml", "dialogues/" + file.getName(), false)) {
                                DialogueMain.getInstance().loadDialogue(file.getName(), config);
                            }
                            commandSender.sendMessage(DialogueMain.getLanguage().translateString(commandSender, "command_create_success", strings[1]));
                        }
                    }
                    break;
                case "help":
                    commandSender.sendMessage(DialogueMain.getLanguage().translateString(commandSender, "command_help_create"));
                    commandSender.sendMessage(DialogueMain.getLanguage().translateString(commandSender, "command_help_play"));
                    commandSender.sendMessage(DialogueMain.getLanguage().translateString(commandSender, "command_help_reload"));
                    commandSender.sendMessage(DialogueMain.getLanguage().translateString(commandSender, "command_help_edit"));
                    break;
                case "reload":
                    if (commandSender.isPlayer() && !commandSender.isOp()) { // 普通玩家不允许reload
                        return true;
                    }
                    Config config = new Config(DialogueMain.getPath() + "/config.yml", Config.YAML);
                    DialogueMain.lineMaxLength = config.getInt("line_max_length", 64);
                    DialogueMain.language = new Language(DialogueMain.getPath() + "/languages/");
                    DialogueMain.invincibleInDialogue = config.getBoolean("invincible_in_dialogue", true);
                    DialogueMain.getInstance().loadAllDialogues();
                    commandSender.sendMessage(DialogueMain.getLanguage().translateString(commandSender, "command_reload_success"));
                    break;
                case "play": // dialogue play BizarreDark test.yml
                    if (commandSender.isPlayer() && !commandSender.isOp()) {
                        commandSender.sendMessage(DialogueMain.getLanguage().translateString(commandSender, "command_not_authorized"));
                        return true;
                    }
                    if (strings.length == 3) {
                        String fileName = strings[2];
                        if (DialogueMain.getDialogues().containsKey(fileName)) {
                            String playerName = strings[1];
                            Player player = Server.getInstance().getPlayer(playerName);
                            if (player != null) {
                                DialogueAPI.playDialogue(commandSender, player, fileName, false);
                            } else {
                                commandSender.sendMessage(DialogueMain.getLanguage().translateString(commandSender, "command_player_not_found", playerName));
                            }
                        } else {
                            commandSender.sendMessage(DialogueMain.getLanguage().translateString(commandSender, "command_dialogue_not_found", fileName));
                        }
                    } else {
                        Server.getInstance().dispatchCommand(commandSender, "dialogue help");
                    }
                    break;
                case "edit":
                    if (commandSender.isOp()) {
                        FormHelper.showDialogueSelect((Player) commandSender);
                    } else if (!commandSender.isPlayer()) {
                        commandSender.sendMessage(DialogueMain.getLanguage().translateString(commandSender, "command_use_in_game"));
                    }
                    break;
                case "permission": //dialogue permission add/del player test.permission
                    if (commandSender.isPlayer() && !commandSender.isOp()) {
                        commandSender.sendMessage(DialogueMain.getLanguage().translateString(commandSender, "command_not_authorized"));
                        return true;
                    }
                    if (strings.length != 4) {
                        return false;
                    }
                    String playerName = strings[2];
                    String permissionName = strings[3];
                    if (Server.getInstance().lookupName(playerName).isPresent()) {
                        switch (strings[1]) {
                            case "add":
                                Config permissionConfig = new Config(DialogueMain.getPath() + "/permission_groups.yml", Config.YAML);
                                List<String> players = new ArrayList<>(permissionConfig.getStringList(permissionName));
                                players.add(playerName);
                                permissionConfig.set(permissionName, players);
                                permissionConfig.save();
                                commandSender.sendMessage(DialogueMain.getLanguage().translateString(commandSender, "command_permission_add_success"));
                                break;
                            case "remove":
                                permissionConfig = new Config(DialogueMain.getPath() + "/permission_groups.yml", Config.YAML);
                                players = new ArrayList<>(permissionConfig.getStringList(permissionName));
                                players.remove(playerName);
                                permissionConfig.set(permissionName, players);
                                permissionConfig.save();
                                commandSender.sendMessage(DialogueMain.getLanguage().translateString(commandSender, "command_permission_remove_success"));
                                break;
                        }
                    } else {
                        commandSender.sendMessage(DialogueMain.getLanguage().translateString(commandSender, "command_player_not_found", playerName));
                    }
                    break;
                case "cleard": // Lazying ...
                    if (strings.length == 2) {
                        try {
                            int i = 0;
                            File file = new File(DialogueMain.getPath() + "/players/");
                            for (File listFile : file.listFiles()) {
                                Config conf = new Config(listFile, Config.YAML);
                                ConfigSection section = conf.getSection(DialogueAPI.KEY_PLAYED_TIMES);
                                section.remove(strings[1]);
                                conf.set(DialogueAPI.KEY_PLAYED_TIMES, section);

                                ConfigSection section1 = conf.getSection(DialogueAPI.KEY_LAST_PLAYED_MILLIS);
                                section1.remove(strings[1]);
                                conf.set(DialogueAPI.KEY_PLAYED_TIMES, section1);
                                conf.save();
                                i++;
                            }
                            commandSender.sendMessage(TextFormat.GREEN + "成功清理 " +
                                    TextFormat.YELLOW + i + TextFormat.GREEN + " 个对话" +
                                    TextFormat.YELLOW + strings[1] + TextFormat.GREEN + " 数据");
                        } catch (Exception ignored) {
                            commandSender.sendMessage(TextFormat.RED + "Error whilst clearing data!");
                        }
                    }
                    break;
            }
        }
        return true;
    }
}
