package home.smart.thuans.centraldevice.artificialBot;

/**
 * Created by Sam on 4/14/2017.
 */
public class IntentLearned {
    private int id;
    private int speakIntentId;
    private boolean reply;
    private String speakIntent;
    private String sentence;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSpeakIntentId() {
        return speakIntentId;
    }

    public void setSpeakIntentId(int speakIntentId) {
        this.speakIntentId = speakIntentId;
    }

    public boolean isReply() {
        return reply;
    }

    public void setReply(boolean reply) {
        reply = reply;
    }

    public String getSpeakIntent() {
        return speakIntent;
    }

    public void setSpeakIntent(String speakIntent) {
        this.speakIntent = speakIntent;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }
}
