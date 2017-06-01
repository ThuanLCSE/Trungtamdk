package home.smart.thuans.centraldevice.utils;

import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import home.smart.thuans.centraldevice.artificialBot.DetectIntent;
import home.smart.thuans.centraldevice.artificialBot.DetectIntentSQLite;
import home.smart.thuans.centraldevice.artificialBot.IntentConstant;
import home.smart.thuans.centraldevice.artificialBot.IntentLearned;
import home.smart.thuans.centraldevice.artificialBot.IntentLearnedSQLite;
import home.smart.thuans.centraldevice.artificialBot.Term;
import home.smart.thuans.centraldevice.artificialBot.TermTarget;
import home.smart.thuans.centraldevice.device.ConnectedDevice;
import home.smart.thuans.centraldevice.device.DeviceSQLite;
import home.smart.thuans.centraldevice.house.HouseConfig;

/**
 * Created by Thuans on 4/28/2017.
 */

public class BotUtils {
    private static String TAG = "Bot utillss";
    private static String BOT_NAME = "<bot-name>";
    private static String BOT_ROLE = "<bot-role>";
    private static String OWNER_NAME = "<owner-name>";
    private static String OWNER_ROLE = "<owner-role>";
    private static String TARGET_OBJECT = "<target-object>";
    private static String RESULT_VALUE = "<result-value>";
    public static DetectIntent findBestDetected(List<Term> foundTerm){
        Map<Integer,Double> sumTfidfIntent = new HashMap<>();
        double maxTfidf = 0;
        int bestIntentId = -1;
        for (Term term: foundTerm){
            double currentPoint = term.getTfidfPoint();
            if (sumTfidfIntent.containsKey(term.getIntentId())) {
                currentPoint += sumTfidfIntent.get(term.getIntentId());
            }
            sumTfidfIntent.put(term.getIntentId(),currentPoint);
            if (currentPoint >maxTfidf){
                maxTfidf = currentPoint;
                bestIntentId = term.getIntentId();
            }
        }
        Log.d(TAG, bestIntentId+ " founded");
            DetectIntentSQLite detectIntentSQLite = new DetectIntentSQLite();
            return detectIntentSQLite.findById(bestIntentId);
    }
    public static ConnectedDevice findBestDeviceName(List<TermTarget> termTargets) {
        Map<String,Double> sumTfidfTarget = new HashMap<>();
        double maxTfidf = 0;
        String bestDeviceId = "";
        for (TermTarget termTarget: termTargets){
            if (termTarget.getTargetType().equals(DeviceConstant.DEVICE_TYPE)) {

                double currentPoint = termTarget.getTfidfPoint();
                if (sumTfidfTarget.containsKey(termTarget.getTargetId())) {
                    currentPoint += sumTfidfTarget.get(termTarget.getTargetId());
                }
                sumTfidfTarget.put(termTarget.getTargetId(), currentPoint);
                if (currentPoint > maxTfidf) {
                    maxTfidf = currentPoint;
                    bestDeviceId= termTarget.getTargetId();
                }
            }
        }
        Log.d(TAG,"best device "+ bestDeviceId+ " founded");
        DeviceSQLite deviceSQLite = new DeviceSQLite();
        return deviceSQLite.findById(bestDeviceId);
    }
    /**
     * Computes the edit distance between two Strings.

     * @param str1      a String
     * @param str2      a String
     * @return          an int of the edit distance between {@code str1} and {@code str2}
     */
    public static int computeEditDistance(String[] str1, String[] str2)
    {
        int str1Length = str1.length;
        int str2Length = str2.length;

        if (str1.equals(str2))       return 0;               //Strings are equal, no edits needed
        else if (str1Length == 0)    return str2.length;   //str1 is empty, edits are simply insertions of the chars of str2
        else if (str2Length == 0)    return str1.length;   //str2 is empty, edits are simply the insertions of the chars of str1
        for (int i = 1; i< str1Length; i++)
            str1[i] = str1[i].toLowerCase();
        for (int i = 1; i< str2Length; i++)
            str2[i] = str2[i].toLowerCase();

        int[] ancestorMatrixCol = new int[str2Length + 1];
        int[] previousMatrixCol = new int[str2Length + 1];
        int[] currentMatrixCol = new int[str2Length + 1];

        for (int i = 0; i < previousMatrixCol.length; i++) previousMatrixCol[i] = i;

        for (int i = 0; i < str1Length; i++)
        {
            //The first ROW of the matrix is associated with scenarios in which str2 is empty, in which case
            //the edit distance is between it and str1.substring(0, i) is simply i. The value is offset by 1
            //to take in to account the first COLUMN of the matrix which corresponds to empty str1 scenarios.
            currentMatrixCol[0] = i + 1;

            //Loop through the chars in str2, calculating the optimal edit distances between
            //the 0-based str2 substring bounded by each char and str1.substring(0, i).
            //In other words, fill from top to bottom, the matrix column indexed by i.
            for (int j = 0; j < str2Length; j++)
            {
                //Take the edit distance (contained in the neighboring upper matrix cell) calculated between the previous str2
                //substring and the current str1 substring and increment it to represent a hypothetical necessary deletion in str2
                int deletionCost = currentMatrixCol[j] + 1;

                //Take the edit distance (contained in the neighboring left matrix cell) calculated between the current str2 substring
                //and the previous str1 substring and increment it to represent a hypothetical necessary insertion to str2
                int insertionCost = previousMatrixCol[j + 1] + 1;

                //Determine the edit distance between the currently processing chars in str1 and str2
                int curCharEditDistance = (str1[i].equals(str2[j]) ? 0 : 1);

                //Take the edit distance calculated between the previous str2 and str1 substrings and
                //increment it only if their currently processing chars differ (hypothetical necessary substitution)
                int substitutionCost = previousMatrixCol[j] + curCharEditDistance;

                //Determine the smallest edit operation cost among those currently associated with a deletion, insertion and substitution
                int minEditOperationCost = Math.min(deletionCost, insertionCost);
                minEditOperationCost = Math.min(minEditOperationCost, substitutionCost);

                //If the previous and currently processing chars of both strings are transposed, determine the smallest
                //edit operation cost between minEditOperationCost and that associated with a hypothetical transposition
                if(i > 0 && j > 0 && str1[i] == str2[j - 1] && str1[i - 1] == str2[j])
                    minEditOperationCost = Math.min(minEditOperationCost, ancestorMatrixCol[j-1] + curCharEditDistance);

                currentMatrixCol[j + 1] = minEditOperationCost;
            }
            /////

            //Copy the elements of currentMatrixCol to previousMatrixCol, priming them for the next iteration
            for (int j = 0; j < previousMatrixCol.length; j++)
            {
                ancestorMatrixCol[j] = previousMatrixCol[j];
                previousMatrixCol[j] = currentMatrixCol[j];
            }

        }
        /////

        return currentMatrixCol[str2Length];
    }

//    public static ConnectedDevice findBestDeviceName(List<ConnectedDevice> deviceList, String sentence) {
//        int minDiff = 999;
//        ConnectedDevice result = null;
//        for (ConnectedDevice device : deviceList){
//            String[] a = "Đèn hai".split(" ");
//            String[] b = "Đèn hai".split(" ");
////            int diff = computeEditDistance(device.getName().split(" "),sentence.split(" "));
//            int diff = computeEditDistance(a,b);
//            Log.d(TAG,device.getName()+ " - --- - "+ diff);
//            if (diff < minDiff){
//                result = device;
//                minDiff = diff;
//            }
//        }
//        return result;
//    }

    public static Map<String,Map<String,Integer>> readDeviceNametoHashMap(List<ConnectedDevice> devices) {
        Map<String,Map<String,Integer>> targetNameWordCount =  new HashMap<>();
        for (ConnectedDevice device: devices) {
            String deviceId = device.getId();
            Map<String, Integer> deviceWordCount = new HashMap<>();
            targetNameWordCount.put(deviceId, deviceWordCount);
            for (String secondName : device.getSecondaryName()) {
                String sentence = secondName;
                System.out.println("[][] "+device.getName()+ "  "+sentence);
                String words[] = sentence.split(" ");
                for (int i = 0; i < words.length; i++){
                    words[i] = words[i].toLowerCase();
                }
                Map<String, Integer> wordCount =  targetNameWordCount.get(deviceId);
                wordCount = updateWordCount(wordCount, words);
                targetNameWordCount.put(deviceId, wordCount);
            }
        }
        return targetNameWordCount;
    }
    private static Map<String, Integer> updateWordCount(Map<String, Integer> wordCount, String[] words) {
        Map<String, Integer> tmp = wordCount;
        for (int i = 0; i<words.length ; i++){
            int count = 1;
            if (tmp.get(words[i]) != null){
                count =  tmp.get(words[i]);
                count++;
            }
            tmp.put(words[i], count);
        }
        return tmp;
    }

    public static String completeSentence(String sentence, String resultValue, String targetObject) {
        HouseConfig house = HouseConfig.getInstance();
        String result = sentence.replaceAll(BOT_NAME, house.getBotName());
        result = result.replaceAll(BOT_ROLE, house.getBotRole());
        result = result.replaceAll(OWNER_NAME, house.getOwnerName());
        result = result.replaceAll(OWNER_ROLE, house.getOwnerRole());
        result = result.replaceAll(RESULT_VALUE, resultValue);
        result = result.replaceAll(TARGET_OBJECT, targetObject);

        Log.d(TAG,result);
        return result;
    }
    public static IntentLearned getIntentByName(String name){
        IntentLearnedSQLite intentLearnedSQLite = new IntentLearnedSQLite();
        IntentLearned reply = intentLearnedSQLite.findSpeakByName(name);
        if (reply == null){
            Log.e(TAG,"Không tìm thấy intent để speakkkkkk " +name);
        }
        return reply;
    }
    public static IntentLearned getIntentById(int id){
        IntentLearnedSQLite intentLearnedSQLite = new IntentLearnedSQLite();
        IntentLearned reply = intentLearnedSQLite.findReplyById(id);
        if (reply == null){
            Log.e(TAG,"Không tìm thấy intent để speakkkkkk " + id);
        }
        return reply;
    }

    public static DetectIntent findDetectByFunction(String functionName) {
        DetectIntentSQLite detectIntentSQLite = new DetectIntentSQLite();
        return detectIntentSQLite.findByName(functionName);
    }
}
