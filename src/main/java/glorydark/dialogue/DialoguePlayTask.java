package glorydark.dialogue;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.scheduler.NukkitRunnable;
import cn.nukkit.scheduler.Task;
import glorydark.dialogue.data.DialogueData;
import glorydark.dialogue.data.DialogueLineData;
import glorydark.dialogue.utils.Utils;

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
        if(player == null || !player.isOnline()){
            DialogueMain.playerPlayingTasks.remove(player);
            this.cancel();
        }
        currentTicks++;
        DialogueLineData data = dialogueData.getDialogueLineData().get(currentLineIndex);
        if(data == null){
            DialogueMain.getPlugin().getLogger().warning("对话播放时出现不存在的对话！");
            this.cancel();
            return;
        }
        if(currentTicks >= data.getExistDuration()){ // 对话播放已完成
            currentLineIndex++; // 切换到下一句
            currentTicks = 0;
            if(currentLineIndex >= dialogueData.getDialogueLineData().size()){ // 如果已经播放完所有的对话
                DialogueMain.playerPlayingTasks.remove(player);
                Server.getInstance().getScheduler().scheduleDelayedTask(DialogueMain.getPlugin(), new NukkitRunnable() {
                    @Override
                    public void run() {
                        dialogueData.executeCommandsAndMessages(player); // 执行命令
                    }
                }, 5); // 防止CurrentModification的问题
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
        int speakerLength = Utils.getStringCharCount(speakerName);
        int lineMaxLength = DialogueMain.lineMaxLength;
        if(speakerLength < lineMaxLength && speakerLength % lineMaxLength != 0){ // 是否需要补充空格
            int remained = lineMaxLength - speakerLength;
            speakerName = speakerName +Utils.getLineBlank().substring(0, remained); // 补充空格
        }
        // 第一行完成
        String fullText = lineData.getText();
        // 文本替换部分内容
        speakerName = speakerName.replace("%player%", player.getName());
        fullText = fullText.replace("%player%", player.getName());
        // 替换完成
        int lineLength = fullText.length(); // 获取最大文本长度
        if(currentTicks >= lineData.getPlayDuration()){
            return speakerName + "\n§r" + fullText;
        }
        int lineMaxCharAtIndex = (int)((double) (currentTicks * lineLength / lineData.getPlayDuration())); // 获取显示字符数
        return speakerName + "\n§r" + fullText.substring(0, lineMaxCharAtIndex);
    }

}
