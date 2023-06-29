package glorydark.dialogue.data;

import cn.nukkit.Player;
import cn.nukkit.scheduler.Task;

/**
 * @author glorydark
 * @date {2023/6/29} {23:52}
 */
public class DialoguePlayTask extends Task {

    protected int currentTicks = 0; // 当前的tick数

    protected int currentLineIndex = 0; // 当前播放行数

    protected Player player; // 玩家

    protected final DialogueData dialogueData; // 对话数据

    public DialoguePlayTask(Player player, DialogueData dialogueData){
        this.player = player;
        this.dialogueData = dialogueData;
    }

    @Override
    public void onRun(int i) {
        currentTicks++;
        DialogueLineData data = dialogueData.getDialogueLineData().get(currentLineIndex);
        if(currentTicks >= data.getExistDuration()){ // 对话播放已完成
            currentLineIndex++; // 切换到下一句
            if(currentLineIndex >= dialogueData.getDialogueLineData().size()){ // 如果已经播放完所有的对话
                dialogueData.executeCommandsAndMessages(player); // 执行命令
                this.cancel();
            }
        }else{
            player.sendActionBar(getText());
        }
    }

    public String getText(){
        DialogueLineData lineData = dialogueData.getDialogueLineData().get(currentLineIndex);
        // 先完成对话者的一行
        String speakerName = lineData.getSpeakerName();
        int speakerMaxLength = Utils.getStringCharCount(speakerName);
        if(speakerMaxLength < 64 && speakerMaxLength % 64 != 0){ // 是否需要补充空格
            int remained = 64 - speakerMaxLength;
            return speakerName +Utils.getLineBlank().substring(0, remained); // 补充空格
        }
        // 第一行完成
        String fullText = lineData.getText();
        // 文本替换部分内容
        fullText = fullText.replace("%player%", player.getName());
        // 替换完成
        int lineMaxLength = Utils.getStringCharCount(fullText); // 获取最大文本长度
        int lineMaxCharAtIndex = (currentTicks / lineData.getPlayDuration()) * lineMaxLength; // 获取显示字符数
        return speakerName + "§r" + fullText.substring(0, lineMaxCharAtIndex);
    }

}
