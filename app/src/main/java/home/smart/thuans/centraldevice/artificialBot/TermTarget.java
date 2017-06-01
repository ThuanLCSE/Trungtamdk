package home.smart.thuans.centraldevice.artificialBot;

/**
 * Created by Thuans on 4/28/2017.
 */

public class TermTarget {
    private String content;
    private String targetId;
    private double tfidfPoint;
    private String targetType;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public double getTfidfPoint() {
        return tfidfPoint;
    }

    public void setTfidfPoint(double tfidfPoint) {
        this.tfidfPoint = tfidfPoint;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }
}
