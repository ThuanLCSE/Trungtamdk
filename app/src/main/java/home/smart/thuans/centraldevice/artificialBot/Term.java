package home.smart.thuans.centraldevice.artificialBot;

/**
 * Created by Thuans on 4/25/2017.
 */
public class Term {
    private String content;
    private int intentId;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getIntentId() {
        return intentId;
    }

    public void setIntentId(int intentId) {
        this.intentId = intentId;
    }

    public double getTfidfPoint() {
        return tfidfPoint;
    }

    public void setTfidfPoint(double tfidfPoint) {
        this.tfidfPoint = tfidfPoint;
    }

    private double tfidfPoint;
}
