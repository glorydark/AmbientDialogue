package glorydark.dialogue.commands;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import glorydark.dialogue.DialogueMain;
import glorydark.dialogue.DialoguePlayTask;
import glorydark.dialogue.data.DialogueData;
import glorydark.dialogue.form.FormHelper;

import java.io.File;

/**
 * @author glorydark
 * @date {2023/6/30} {0:36}
 */
public class DialogueCommands extends Command {

    public DialogueCommands(String name) {
        super(name);
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if(strings.length > 0){
            switch (strings[0]) {
                case "create":
                    if (commandSender.isPlayer() && !commandSender.isOp()) { // 普通玩家不允许reload
                        return true;
                    }
                    if(strings.length != 2){
                        Server.getInstance().dispatchCommand(commandSender, "dialogue help");
                    }else{
                        String fileName = strings[1] + ".yml"; // 保存文件名
                        if(DialogueMain.dialogues.containsKey(fileName)){ // 如果已经存在，提示错误
                            commandSender.sendMessage(DialogueMain.getLanguage().translateString(null, "command_dialogue_existed"));
                        }else{ // 未存在，将resources里面的default.yml保存为新的文件
                            File file = new File(DialogueMain.getPath()+"/dialogues/"+fileName);
                            if(DialogueMain.getPlugin().saveResource("default.yml", "dialogues/"+file.getName(), false)) {
                                DialogueMain.getPlugin().loadDialogue(file);
                            }
                        }
                    }
                    break;
                case "help":
                    commandSender.sendMessage(DialogueMain.getLanguage().translateString(null, "command_help_create"));
                    commandSender.sendMessage(DialogueMain.getLanguage().translateString(null, "command_help_play"));
                    commandSender.sendMessage(DialogueMain.getLanguage().translateString(null, "command_help_reload"));
                    commandSender.sendMessage(DialogueMain.getLanguage().translateString(null, "command_help_edit"));
                    break;
                case "reload":
                    if (commandSender.isPlayer() && !commandSender.isOp()) { // 普通玩家不允许reload
                        return true;
                    }
                    DialogueMain.getPlugin().loadAllDialogues();
                    commandSender.sendMessage(DialogueMain.getLanguage().translateString(null, "command_reload_success"));
                    break;
                case "play": // dialogue play BizarreDark test.yml
                    if (commandSender.isPlayer() && !commandSender.isOp()) {
                        commandSender.sendMessage(DialogueMain.getLanguage().translateString((Player) commandSender, "command_not_authorized"));
                        return true;
                    }
                    if (strings.length == 3) {
                        String fileName = strings[2] + ".yml";
                        boolean isPlayer = commandSender.isPlayer();
                        if (DialogueMain.getDialogues().containsKey(fileName)) {
                            String playerName = strings[1];
                            Player player = Server.getInstance().getPlayer(playerName);
                            if (player != null) {
                                if (DialogueMain.playerPlayingTasks.containsKey(player)) { // 避免玩家已经播放对话
                                    if (isPlayer) {
                                        commandSender.sendMessage(DialogueMain.getLanguage().translateString((Player) commandSender, "command_player_in_dialogue", playerName));
                                    }
                                    return true;
                                }
                                DialogueData data = DialogueMain.getDialogues().get(fileName);
                                DialoguePlayTask task = new DialoguePlayTask(player, data);
                                DialogueMain.getPlugin().getServer().getScheduler().scheduleRepeatingTask(DialogueMain.getPlugin(), task, 1, true);
                                if (isPlayer) {
                                    commandSender.sendMessage(DialogueMain.getLanguage().translateString((Player) commandSender, "command_play_to_player_success", playerName));
                                }
                            } else {
                                if (isPlayer) {
                                    commandSender.sendMessage(DialogueMain.getLanguage().translateString((Player) commandSender, "command_player_not_found", playerName));
                                }
                            }
                        } else {
                            if (isPlayer) {
                                commandSender.sendMessage(DialogueMain.getLanguage().translateString((Player) commandSender, "command_dialogue_not_found", fileName));
                            }
                        }
                    }else{
                        Server.getInstance().dispatchCommand(commandSender, "dialogue help");
                    }
                    break;
                case "edit":
                    if(commandSender.isOp()){
                        FormHelper.showDialogueSelect((Player) commandSender);
                    }else if(!commandSender.isPlayer()){
                        commandSender.sendMessage(DialogueMain.getLanguage().translateString(null, "command_use_in_game"));
                    }
                    break;
            }
        }
        return true;
    }
}
