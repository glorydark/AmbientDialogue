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

}
