package glorydark.dialogue.data;

import cn.nukkit.Player;
import cn.nukkit.Server;
import glorydark.dialogue.utils.Utils;

import java.util.List;

/**
 * @author glorydark
 * @date {2023/6/29} {23:47}
 */
public class DialogueData {

    protected String identifier;

    protected List<DialogueLineData> dialogueLineData;

    protected List<String> commands;

    protected List<String> messages;

    public DialogueData(String identifier, List<DialogueLineData> dataList, List<String> commands, List<String> messages){
        this.identifier = identifier;
        this.dialogueLineData = dataList;
        this.commands = commands;
        this.messages = messages;
    }

    public List<DialogueLineData> getDialogueLineData() {
        return dialogueLineData;
    }

    public void executeCommandsAndMessages(Player player){

        for(String command: commands){
            Utils.parseAndExecuteCommand(player, command);
        }

        for(String s: messages){
            Server.getInstance().dispatchCommand(player, s.replace("%player%", player.getName()));
        }

    }

    public String getIdentifier() {
        return identifier;
    }
}
