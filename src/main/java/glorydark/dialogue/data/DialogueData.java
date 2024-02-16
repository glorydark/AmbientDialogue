package glorydark.dialogue.data;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.Config;
import glorydark.dialogue.DialogueMain;
import glorydark.dialogue.DialoguePlayTask;
import glorydark.dialogue.action.ActionType;
import glorydark.dialogue.action.ExecuteAction;
import glorydark.dialogue.action.requirement.Requirement;
import glorydark.dialogue.event.manager.HandlerManager;
import glorydark.dialogue.event.type.DialoguePreStartEvent;
import glorydark.dialogue.event.type.DialogueSkipEvent;
import glorydark.dialogue.response.ResponseData;
import glorydark.dialogue.response.ResponseDataType;
import glorydark.dialogue.utils.Utils;

import java.util.*;

/**
 * @author glorydark
 * @date {2023/6/29} {23:47}
 */
public class DialogueData {

    public List<Requirement> openRequirements;
    protected String identifier;
    protected List<DialogueLineData> dialogueLineData;
    protected boolean playerStill;
    protected List<ExecuteAction> preStartActions = new ArrayList<>();

    protected LinkedHashMap<Integer, List<ExecuteAction>> tickActions = new LinkedHashMap<>();

    protected List<ExecuteAction> endActions = new ArrayList<>();

    protected LinkedHashMap<String, Integer> finishPlayerData = new LinkedHashMap<>();

    public DialogueData(String identifier, List<DialogueLineData> dataList, boolean playerStill, List<Map<String, Object>> openRequirements, List<Map<String, Object>> preStartActions, List<Map<String, Object>> tickActions, List<Map<String, Object>> endActions) {
        this.identifier = identifier;
        this.dialogueLineData = dataList;
        this.playerStill = playerStill;
        this.openRequirements = DialogueMain.getRequirementRegistry().parseRequirement(openRequirements);
        for (Map<String, Object> action : preStartActions) {
            this.addPreStartExecuteAction(ExecuteAction.parseExecuteActionFromMap(action));
        }
        for (Map<String, Object> action : tickActions) {
            this.addTickExecuteAction((Integer) action.get("tick"), ExecuteAction.parseExecuteActionFromMap(action));
        }
        for (Map<String, Object> action : endActions) {
            this.addEndExecuteAction(ExecuteAction.parseExecuteActionFromMap(action));
        }
        loadFinishPlayerData();
    }

    public void loadFinishPlayerData() {
        Config config = new Config(DialogueMain.getPath() + "/player_caches/" + identifier, Config.YAML);
        for (Map.Entry<String, Object> objectEntry : config.getAll().entrySet()) {
            finishPlayerData.put(objectEntry.getKey(), (Integer) objectEntry.getValue());
        }
    }

    public void updateFinishPlayerData(Player player) {
        String playerName = player.getName();
        int count = finishPlayerData.getOrDefault(playerName, 0) + 1;
        Config config = new Config(DialogueMain.getPath() + "/player_caches/" + identifier, Config.YAML);
        config.set(playerName, count);
        config.save();
        finishPlayerData.put(playerName, count);
    }

    public void play(CommandSender sender, Player player) {
        for (Requirement openRequirement : openRequirements) {
            if (!openRequirement.canExecute(player, this)) {
                if (openRequirement.isEnableDefaultFailedMessage()) {
                    Utils.sendPlayerMessage(player, openRequirement.getDefaultFailedMessage(player));
                }
                for (String failedMessage : openRequirement.getFailedMessages()) {
                    Utils.sendPlayerMessage(player, failedMessage);
                }
                return;
            }
        }
        ResponseData responseData = executePreStartActions(player);
        if (responseData.getBooleanResponse(ResponseDataType.BOOLEAN_SKIP_DIALOGUE, false)) {
            DialogueSkipEvent dialogueSkipEvent = new DialogueSkipEvent(player, this);
            HandlerManager.callEvent(dialogueSkipEvent);
            this.executeEndActions(player);
            return;
        }
        DialoguePreStartEvent dialoguePreStartEvent = new DialoguePreStartEvent(player, this);
        HandlerManager.callEvent(dialoguePreStartEvent);
        if (dialoguePreStartEvent.isCancelled()) {
            return;
        }
        if (playerStill) {
            player.setImmobile(true);
        }
        DialoguePlayTask task = new DialoguePlayTask(player, this);
        DialogueMain.getPlayerPlayingTasks().put(player, task);
        DialogueMain.getInstance().getServer().getScheduler().scheduleRepeatingTask(DialogueMain.getInstance(), task, 1);
        sender.sendMessage(DialogueMain.getLanguage().translateString(sender, "command_play_to_player_success", player.getName()));
    }

    public ResponseData executePreStartActions(Player player) {
        ResponseData responseData = new ResponseData();
        for (ExecuteAction preStartAction : preStartActions) {
            if (preStartAction.checkValid(player, this)) {
                preStartAction.execute(player, this);
                if (preStartAction.getActionType() == ActionType.SKIP_DIALOGUE) {
                    responseData.setResponse(ResponseDataType.BOOLEAN_SKIP_DIALOGUE, true);
                }
            }
        }
        return responseData;
    }

    public void executeTickActions(Player player, int tick) {
        List<ExecuteAction> actions = tickActions.getOrDefault(tick, new ArrayList<>());
        if (actions.size() == 0) {
            return;
        }
        for (ExecuteAction tickAction : actions) {
            if (tickAction.checkValid(player, this)) {
                tickAction.execute(player, this);
            }
        }
    }

    public void executeEndActions(Player player) {
        player.setImmobile(false);
        updateFinishPlayerData(player);
        for (ExecuteAction endAction : endActions) {
            if (endAction.checkValid(player, this)) {
                endAction.execute(player, this);
            }
        }
    }

    public void saveAll() {
        Config config = new Config(DialogueMain.getPath() + "/dialogues/" + identifier, Config.YAML);
        List<Map<String, Object>> lines = new ArrayList<>();
        for (DialogueLineData lineData : dialogueLineData) {
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

    public void addPreStartExecuteAction(ExecuteAction executeAction) {
        this.preStartActions.add(executeAction);
    }

    public void addTickExecuteAction(int tick, ExecuteAction executeAction) {
        if (!this.tickActions.containsKey(tick)) {
            this.tickActions.put(tick, new ArrayList<>());
        }
        this.tickActions.get(tick).add(executeAction);
    }

    public void addEndExecuteAction(ExecuteAction executeAction) {
        this.endActions.add(executeAction);
    }

    public List<DialogueLineData> getDialogueLineData() {
        return dialogueLineData;
    }

    public void setDialogueLineData(List<DialogueLineData> dialogueLineData) {
        this.dialogueLineData = dialogueLineData;
    }

    public boolean isPlayerStill() {
        return playerStill;
    }

    public void setPlayerStill(boolean playerStill) {
        this.playerStill = playerStill;
    }

    public String getIdentifier() {
        return identifier;
    }

    public LinkedHashMap<String, Integer> getFinishPlayerData() {
        return finishPlayerData;
    }
}
