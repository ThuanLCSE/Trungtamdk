package home.smart.thuans.centraldevice.artificialBot;

/**
 * Created by Sam on 4/14/2017.
 */
public class DetectIntent {
    private int id;
    private int failReplyId;
    private int successReplyId;
    private int replyId;
    private String functionName;
    private String speakIntent;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFailReplyId() {
        return failReplyId;
    }

    public void setFailReplyId(int failReplyId) {
        this.failReplyId = failReplyId;
    }

    public int getSuccessReplyId() {
        return successReplyId;
    }

    public void setSuccessReplyId(int successReplyId) {
        this.successReplyId = successReplyId;
    }

    public int getReplyId() {
        return replyId;
    }

    public void setReplyId(int replyId) {
        this.replyId = replyId;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getSpeakIntent() {
        return speakIntent;
    }

    public void setSpeakIntent(String speakIntent) {
        this.speakIntent = speakIntent;
    }
}
