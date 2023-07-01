package glorydark.dialogue.data;

/**
 * @author glorydark
 * @date {2023/6/29} {23:48}
 */
public class DialogueLineData {

    protected int existDuration;

    protected int playDuration;

    protected String text;

    protected String speakerName;

    public DialogueLineData(String text, String speakerName, int existDuration, int playDuration){
        this.text = text;
        // 对换行符进行处理
        this.text = this.text.replace("\\n", "\n");
        this.speakerName = speakerName;
        this.existDuration = existDuration;
        this.playDuration = playDuration;
    }

    public int getExistDuration() {
        return existDuration;
    }

    public int getPlayDuration() {
        return playDuration;
    }

    public String getText() {
        return text;
    }

    public String getSpeakerName() {
        return speakerName;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setSpeakerName(String speakerName) {
        this.speakerName = speakerName;
    }

    public void setPlayDuration(int playDuration) {
        this.playDuration = playDuration;
    }

    public void setExistDuration(int existDuration) {
        this.existDuration = existDuration;
    }
}
