package home.smart.thuans.centraldevice.utils;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by Thuans on 4/17/2017.
 */
public class TFIDF {
    public static float tf(Map<String, Integer> wordCount, String term) {
        float termFrequency = 0;
        float maxFrequency = 0;
        Iterator it = wordCount.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry item = (Map.Entry)it.next();
            int count = (Integer) item.getValue();
            String word = (String) item.getKey();
            if (count > maxFrequency){
                maxFrequency = count;
            }
            if (word.equalsIgnoreCase(term)){
                termFrequency = count;
            }
        }
//        System.out.println("max: "+maxFrequency);
//        System.out.println("term: "+termFrequency);

        return termFrequency / maxFrequency;
    }

    public static float idf(Map<String,Map<String,Integer>> targetSet, String term) {
        double functionContainTerm = 0;
        double numberOfFunction = targetSet.entrySet().size();

        Iterator it = targetSet.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Map<String, Integer> frequence = (Map<String, Integer>) pair.getValue();
            if (frequence.get(term) != null){
                functionContainTerm++;
            }
        }
        return (float) (Math.log(numberOfFunction / functionContainTerm)/Math.log(2));
    }

    public static float createTfIdf(Map<String, Map<String, Integer>> targetSet, String term, String targetName) {

        Map<String,Integer> wordCout = targetSet.get(targetName);
//        System.out.println("tf: "+tf(wordCout, term));
//        System.out.println("idf: "+idf(functionSet, term));
        return tf(wordCout, term) * idf(targetSet, term);
    }
}
