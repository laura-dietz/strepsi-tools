package edu.umass.ciir.strepsi;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

public class StopWordList {

    private static final String[] STOP_WORDS = {
            "a", "about", "and", "are", "an", "also", "are", "as", "at", "be", "but", "by", "com",
            "for", "from", "he", "how", "his", "has", "have", "if", "i", "in", "into", "is", "it",
            "its", "may", "no", "not", "of", "on", "or", "s", "she", "such", "should",
            "t", "that", "the", "this", "their", "then", "there", "these",
            "they", "this", "to", "was", "were", "what", "when", "where", "who", "will", "with",
            "you", "http", "www", "org", "com", "edu", "resource", "gov"
    };

    private static Set<String> m_stopSet = null;

    public static boolean isStopWord(String word) {
        if (m_stopSet == null) {
            try {
                loadFromResources();
            } catch (Exception e) {
                System.out.println("failed to load inquery stopword list");
            }
            //m_stopSet = makeStopSet(STOP_WORDS, true);
        }
        return m_stopSet.contains(word.toLowerCase());
    }

    public static void addWord(String word) {

        if (m_stopSet == null) {
            try {
                loadFromResources();
            } catch (Exception e) {
                System.out.println("failed to load inquery stopword list");
            }
        }

        m_stopSet.add(word);
    }

    public static void removeWord(String word) {

        if (m_stopSet == null) {
            try {
                loadFromResources();
            } catch (Exception e) {
                System.out.println("failed to load inquery stopword list");
            }
        }

        m_stopSet.remove(word);
    }
    public static void removeStopWord(String word) {
        removeWord(word);
    }

    public static void addStopWord(String word) {
        addWord(word);
    }

    public static Set<String> getStopWords() {
        if (m_stopSet == null) {
            try {
                loadFromResources();
            } catch (Exception e) {
                System.out.println("failed to load inquery stopword list");
            }
        }
        return m_stopSet;
    }

    /**
     * @param stopWords
     * @param ignoreCase If true, all words are lower cased first.
     * @return a Set containing the words
     */
    private static final Set<String> makeStopSet(String[] stopWords, boolean ignoreCase) {
        HashSet<String> stopTable = new HashSet<String>(stopWords.length);
        for (int i = 0; i < stopWords.length; i++)
            stopTable.add(ignoreCase ? stopWords[i].toLowerCase() : stopWords[i]);
        return stopTable;
    }

    private static final void loadFromResources() throws Exception {
        System.out.println("Loading inquery stopword list from resources.");
        reloadStopWordFromInputStream(StopWordList.class.getResourceAsStream("/stopwords/inquery"), true);
    }

    public static final void reloadStopWordFromInputStream(InputStream inputStream, boolean ignoreCase)

            throws Exception {
        HashSet<String> stopTable = new HashSet<String>();
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        try {
            int numLoaded = 0;
            while (br.ready()) {
                String line = br.readLine();
                if (line.trim().length() > 0 && !line.startsWith("//")) {
                    stopTable.add(ignoreCase ? line.toLowerCase() : line);
                    numLoaded++;
                }
            }
            System.out.println("Loaded: " + numLoaded + " stopwords.");
        } finally {
            br.close();
        }
        m_stopSet = stopTable;
    }
}
