package glorydark.dialogue;

import cn.nukkit.Player;
import cn.nukkit.scheduler.Task;
import glorydark.dialogue.data.DialogueData;
import glorydark.dialogue.data.DialogueLineData;
import glorydark.dialogue.event.manager.HandlerManager;
import glorydark.dialogue.event.type.DialogueEndEvent;
import glorydark.dialogue.event.type.DialogueTickEvent;
import glorydark.dialogue.utils.Utils;

/**
 * @author glorydark
 * @date {2023/6/29} {23:52}
 */
public class DialoguePlayTask extends Task {

    protected final DialogueData dialogueData; // 对话数据
    protected int currentTicks = 0; // 当前的tick数
    protected int currentLineTicks = 0; // 当前行数的tick数
    protected int currentLineIndex = 0; // 当前播放行数
    protected Player player; // 玩家

    public DialoguePlayTask(Player player, DialogueData dialogueData) {
        this.player = player;
        this.dialogueData = dialogueData;
    }

    @Override
    public void onRun(int i) {
        if (player == null || !player.isOnline()) {
            DialogueMain.playerPlayingTasks.remove(player);
            this.cancel();
        }
        currentTicks++;
        currentLineTicks++;
        DialogueLineData data = dialogueData.getDialogueLineData().get(currentLineIndex);
        if (data == null) {
            DialogueMain.getInstance().getLogger().warning("Error in running DialoguePlayTask#onRun, caused by: null line data!");
            this.cancel();
            return;
        }
        DialogueTickEvent dialogueEndEvent = new DialogueTickEvent(player, dialogueData, currentTicks);
        HandlerManager.callEvent(dialogueEndEvent);
        dialogueData.executeTickActions(player, currentTicks);
        if (currentLineTicks >= data.getExistDuration()) { // 对话播放已完成
            currentLineIndex++; // 切换到下一句
            currentLineTicks = 0;
            if (currentLineIndex >= dialogueData.getDialogueLineData().size()) { // 如果已经播放完所有的对话
                this.end();
            }
        } else {
            player.sendActionBar(getText());
        }
    }

    public void end() {
        DialogueEndEvent dialogueEndEvent = new DialogueEndEvent(player, dialogueData);
        HandlerManager.callEvent(dialogueEndEvent);
        dialogueData.executeEndActions(player);
        DialogueMain.playerPlayingTasks.remove(player);
        this.cancel();
    }

    public String getText() {
        DialogueLineData lineData = dialogueData.getDialogueLineData().get(currentLineIndex);
        // 先完成对话者的一行
        String speakerName = lineData.getSpeakerName();
        int speakerLength = Utils.getStringCharCount(speakerName);
        int lineMaxLength = DialogueMain.lineMaxLength;
        if (speakerLength < lineMaxLength && speakerLength % lineMaxLength != 0) { // 是否需要补充空格
            int remained = lineMaxLength - speakerLength;
            speakerName = speakerName + Utils.getLineBlank().substring(0, remained); // 补充空格
        }
        // 第一行完成
        String fullText = lineData.getText();
        // 文本替换部分内容
        speakerName = speakerName.replace("%player%", player.getName());
        fullText = fullText.replace("%player%", player.getName());
        // 替换完成
        int lineLength = fullText.length(); // 获取最大文本长度
        if (currentLineTicks >= lineData.getPlayDuration()) {
            return speakerName + "\n§r" + fullText;
        }
        int lineMaxCharAtIndex = (int) ((double) (currentLineTicks * lineLength / lineData.getPlayDuration())); // 获取显示字符数
        return speakerName + "\n§r" + fullText.substring(0, lineMaxCharAtIndex);
    }

}
