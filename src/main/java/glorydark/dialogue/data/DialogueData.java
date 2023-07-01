package glorydark.dialogue.data;

import cn.nukkit.Player;
import cn.nukkit.utils.Config;
import glorydark.dialogue.DialogueMain;
import glorydark.dialogue.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author glorydark
 * @date {2023/6/29} {23:47}
 */
public class DialogueData {

    protected String identifier;

    protected List<DialogueLineData> dialogueLineData;

    protected List<String> commands;

    protected List<String> messages;

    protected boolean playerStill;

    public DialogueData(String identifier, List<DialogueLineData> dataList, List<String> commands, List<String> messages, boolean playerStill){
        this.identifier = identifier;
        this.dialogueLineData = dataList;
        this.commands = commands;
        this.messages = messages;
        this.playerStill = playerStill;
    }

    public void setDialogueLineData(List<DialogueLineData> dialogueLineData) {
        this.dialogueLineData = dialogueLineData;
    }

    public List<DialogueLineData> getDialogueLineData() {
        return dialogueLineData;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    public void setCommands(List<String> commands) {
        this.commands = commands;
    }

    public List<String> getCommands() {
        return commands;
    }

    public List<String> getMessages() {
        return messages;
    }

    public boolean isPlayerStill() {
        return playerStill;
    }

    public void setPlayerStill(boolean playerStill) {
        this.playerStill = playerStill;
    }

    public void executeCommandsAndMessages(Player player){

        for(String command: commands){
            Utils.parseAndExecuteCommand(player, command);
        }

        for(String s: messages){
            player.sendMessage(s.replace("%player%", player.getName()));
        }

    }

    public String getIdentifier() {
        return identifier;
    }

    public void saveAll(){
        Config config = new Config(DialogueMain.getPath()+"/dialogues/"+identifier, Config.YAML);
        config.set("commands", commands);
        config.set("messages", messages);
        List<Map<String, Object>> lines = new ArrayList<>();
        for(DialogueLineData lineData: dialogueLineData){
            Map<String, Object> line = new HashMap<>();
            line.put("text", lineData.getText().replace("\n", "\\n"));
            line.put("speaker_name", lineData.getSpeakerName());
            line.put("exist_ticks", lineData.getExistDuration());
            line.put("play_ticks", lineData.getPlayDuration());
            lines.add(line);
        }
        config.set("lines", lines);
        config.save();
    }
}
