package glorydark.dialogue.commands;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import glorydark.dialogue.DialogueMain;
import glorydark.dialogue.DialoguePlayTask;
import glorydark.dialogue.data.DialogueData;

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
            switch (strings[0]){
                case "help":
                    break;
                case "reload":
                    if(commandSender.isPlayer() && !commandSender.isOp()){ // 普通玩家不允许reload
                        return true;
                    }
                    DialogueMain.getPlugin().loadAllDialogues();
                    commandSender.sendMessage(DialogueMain.getLanguage().translateString(null, "command_reload_success"));
                    break;
                case "play": // dialogue play BizarreDark test.yml
                    if(strings.length == 3){
                        String fileName = strings[2];
                        if(DialogueMain.getDialogues().containsKey(fileName)) {
                            String playerName = strings[1];
                            Player player = Server.getInstance().getPlayer(playerName);
                            if(player != null){
                                DialogueData data = DialogueMain.getDialogues().get(fileName);
                                DialoguePlayTask task = new DialoguePlayTask(player, data);
                                Server.getInstance().getScheduler().scheduleRepeatingTask(DialogueMain.getPlugin(), task, 1, true);
                                commandSender.sendMessage(DialogueMain.getLanguage().translateString(player, "command_play_to_player_success", playerName));
                            }else{
                                commandSender.sendMessage(DialogueMain.getLanguage().translateString(null, "command_player_not_found", playerName));
                            }
                        }else{
                            commandSender.sendMessage(DialogueMain.getLanguage().translateString(null, "command_dialogue_not_found", fileName));
                        }
                    }
                    break;
            }
        }
        return true;
    }
}
