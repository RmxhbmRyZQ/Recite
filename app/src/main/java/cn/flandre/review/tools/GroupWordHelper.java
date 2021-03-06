package cn.flandre.review.tools;

import cn.flandre.review.data.bean.GroupWord;
import cn.flandre.review.data.bean.Word;
import cn.flandre.review.data.database.SQLHelper;
import cn.flandre.review.data.database.SQLRecite;

/**
 * @author RmxhbmRyZQ 2021.8.30
 */
public class GroupWordHelper {
    public static final String SEPARATOR = ",";

    public static void setWordId(GroupWord groupWord, String id) {
        String[] strings = id.split(SEPARATOR);
        int[] wordId = new int[strings.length];
        for (int i = 0; i < strings.length; i++) {
            wordId[i] = Integer.parseInt(strings[i]);
        }
        groupWord.setWordId(wordId);
    }

    public static String getWordId(GroupWord groupWord) {
        return StringUtils.join(groupWord.getWordId(), SEPARATOR);
    }

    public static void setWrongIndex(GroupWord groupWord, String index) {
        String[] strings = index.split(SEPARATOR);
        boolean[] wordId = new boolean[strings.length];
        for (int i = 0; i < strings.length; i++) {
            wordId[i] = Boolean.parseBoolean(strings[i]);
        }
        groupWord.setWrongIndex(wordId);
    }

    public static String getWrongIndex(GroupWord groupWord) {
        return StringUtils.join(groupWord.getWrongIndex(), SEPARATOR);
    }

    public static void setCorrectIndex(GroupWord groupWord, String index) {
        String[] strings = index.split(SEPARATOR);
        boolean[] wordId = new boolean[strings.length];
        for (int i = 0; i < strings.length; i++) {
            wordId[i] = Boolean.parseBoolean(strings[i]);
        }
        groupWord.setCorrectIndex(wordId);
    }

    public static String getCorrectIndex(GroupWord groupWord) {
        return StringUtils.join(groupWord.getCorrectIndex(), SEPARATOR);
    }

    public static void setWrongTimes(GroupWord groupWord, String times) {
        String[] strings = times.split(SEPARATOR);
        int[] wordId = new int[strings.length];
        for (int i = 0; i < strings.length; i++) {
            wordId[i] = Integer.parseInt(strings[i]);
        }
        groupWord.setWrongTimes(wordId);
    }

    public static String getWrongTimes(GroupWord groupWord) {
        return StringUtils.join(groupWord.getWrongTimes(), SEPARATOR);
    }

    public static void setRightTimes(GroupWord groupWord, String times) {
        String[] strings = times.split(SEPARATOR);
        int[] wordId = new int[strings.length];
        for (int i = 0; i < strings.length; i++) {
            wordId[i] = Integer.parseInt(strings[i]);
        }
        groupWord.setRightTimes(wordId);
    }

    public static String getRightTimes(GroupWord groupWord) {
        return StringUtils.join(groupWord.getRightTimes(), SEPARATOR);
    }

    public static void setWrong(GroupWord groupWord, String wrong) {
        String[] strings = wrong.split(SEPARATOR);
        boolean[] wrongs = new boolean[strings.length];
        for (int i = 0; i < strings.length; i++) {
            wrongs[i] = Boolean.parseBoolean(strings[i]);
        }
        groupWord.setWrong(wrongs);
    }

    public static String getWrong(GroupWord groupWord) {
        return StringUtils.join(groupWord.getWrong(), SEPARATOR);
    }

    public static void setWords(GroupWord groupWord, SQLRecite sqlRecite) {
        int[] wordId = groupWord.getWordId();
        Word[] words = new Word[wordId.length];
        for (int i = 0; i < wordId.length; i++) {
            words[i] = SQLHelper.getWord(wordId[i], sqlRecite);
        }
        groupWord.setWord(words);
    }
}
