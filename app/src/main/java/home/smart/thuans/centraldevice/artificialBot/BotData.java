package home.smart.thuans.centraldevice.artificialBot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Thuans on 4/25/2017.
 */
public class BotData {
    List<Term> listTerm;
    List<DetectIntent> detectIntentList;
    List<IntentLearned> learnedReplyList;

    public List<IntentLearned> getLearnedReplyList() {
        return learnedReplyList;
    }

    public void setLearnedReplyList(List<IntentLearned> learnedReplyList) {
        this.learnedReplyList = learnedReplyList;
    }

    public List<Term> getListTerm() {
        return listTerm;
    }

    public void setListTerm(List<Term> listTerm) {
        this.listTerm = listTerm;
    }

    public List<DetectIntent> getDetectIntentList() {
        return detectIntentList;
    }

    public void setDetectIntentList(List<DetectIntent> detectIntentList) {
        this.detectIntentList = detectIntentList;
    }

}
