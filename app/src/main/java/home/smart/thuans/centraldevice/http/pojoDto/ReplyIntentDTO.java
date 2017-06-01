package home.smart.thuans.centraldevice.http.pojoDto;

/**
 * Created by Thuans on 4/24/2017.
 */
public class ReplyIntentDTO {
    private  int functionId;
    private String resultType;
    private String botTypeId;
    private String sentence;
    private String intentName;
    private int intentSpeakId;

    public int getIntentSpeakId() {
        return intentSpeakId;
    }

    public void setIntentSpeakId(int intentSpeakId) {
        this.intentSpeakId = intentSpeakId;
    }

    public int getFunctionId() {
        return functionId;
    }

    public void setFunctionId(int functionId) {
        this.functionId = functionId;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getBotTypeId() {
        return botTypeId;
    }

    public void setBotTypeId(String botTypeId) {
        this.botTypeId = botTypeId;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public String getIntentName() {
        return intentName;
    }

    public void setIntentName(String intentName) {
        this.intentName = intentName;
    }
}
