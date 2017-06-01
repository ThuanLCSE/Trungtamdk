package home.smart.thuans.centraldevice.http.pojoDto;

/**
 * Created by Thuans on 4/25/2017.
 */
public class ReplyDTO {
    private String id;
    private String intentLearnId;
    private String sentence;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIntentLearnId() {
        return intentLearnId;
    }

    public void setIntentLearnId(String intentLearnId) {
        this.intentLearnId = intentLearnId;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }
}
