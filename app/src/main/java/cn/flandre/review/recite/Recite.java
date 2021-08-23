package cn.flandre.review.recite;

import cn.flandre.review.data.GroupWord;
import cn.flandre.review.data.ReciteData;
import cn.flandre.review.data.Word;
import cn.flandre.review.database.SQLHelper;
import cn.flandre.review.database.SQLRecite;
import cn.flandre.review.enumerate.ChoiceMode;
import cn.flandre.review.tools.ListRandom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static cn.flandre.review.database.SQLRecite.*;
import static cn.flandre.review.recite.ReciteTimeManager.DEFAULT_RECITE_TIME;

/**
 * @author RmxhbmRyZQ
 * 2021.8.20
 */
abstract public class Recite {
    public final static int TIMES_BETWEEN_WRONG_RIGHT = 5;

    final List<GroupWord> list;
    final List<Integer> listAuxiliary;
    final List<Integer> wordAuxiliary;
    final SQLRecite sqlRecite;
    GroupWord groupWord;
    Word[] words;
    int listAuxiliaryIndex = -1;
    int wordAuxiliaryIndex = -1;

    int total;
    final ChoiceMode mode;

    protected ReciteData reciteData;

    public Recite(List<GroupWord> list, ChoiceMode mode, ReciteData data, SQLRecite sqlRecite){
        this.list = list;
        this.mode = mode;
        this.sqlRecite = sqlRecite;
        this.reciteData = data;

        wordAuxiliary = new ArrayList<>();
        listAuxiliary = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            listAuxiliary.add(i);
        }
        for (int i = 0; i < 100; i++) {
            wordAuxiliary.add(i);
        }
        Collections.shuffle(listAuxiliary);
        initTotal();
    }

    protected Word getNextWord() {
        if (words == null || wordAuxiliaryIndex == words.length - 1) {
            // 当最后一个背完时结束
            if (listAuxiliaryIndex == list.size() - 1) {
                saveData();
                return null;
            } else {
                // 第一次运行时为 -1
                if (wordAuxiliaryIndex != -1) {
                    list.set(listAuxiliary.get(listAuxiliaryIndex), null);
                    saveData();
                }
                listAuxiliaryIndex++;
                groupWord = list.get(listAuxiliary.get(listAuxiliaryIndex));
                words = groupWord.getWord();
                Collections.sort(wordAuxiliary);
                ListRandom.shuffle(wordAuxiliary, words.length);
                wordAuxiliaryIndex = -1;
            }
        }
        wordAuxiliaryIndex++;
        return words[wordAuxiliary.get(wordAuxiliaryIndex)];
    }

    public void setWrong() {
        int pos = wordAuxiliary.get(wordAuxiliaryIndex);
        int wrong = ++groupWord.getWrongTimes()[pos];
        groupWord.getWrong()[pos] = true;
        int right = groupWord.getRightTimes()[pos];
        // 当错的次数比对的次数多达一定时设为常错单词
        if (wrong - right > TIMES_BETWEEN_WRONG_RIGHT) {
            groupWord.getWrongIndex()[pos] = true;
            groupWord.getCorrectIndex()[pos] = false;
        }
    }

    public void setRight() {
        int pos = wordAuxiliary.get(wordAuxiliaryIndex);
        int right = ++groupWord.getRightTimes()[pos];
        int wrong = groupWord.getWrongTimes()[pos];
        groupWord.getWrong()[pos] = false;
        // 当对的次数比错的次数多达一定时设为常对单词
        if (right - wrong > TIMES_BETWEEN_WRONG_RIGHT) {
            groupWord.getWrongIndex()[pos] = false;
            groupWord.getCorrectIndex()[pos] = true;
        }
    }

    public Recite getNext() {
        Recite recite = null;
        switch (mode) {
            case CONTINUE:
                // 如果是继续背诵就应该拿暂时数据库的单词
                if (!SQLHelper.isEmptyGWT(TMPGW, reciteData.getType(), sqlRecite)) {
                    recite = ReciteWordCreator.getTmpReciteWord(this, sqlRecite);
                }
                break;
            case TOTAL:
            case PORTION:
            case WRONG:
                // 其他情况就直接获取今天要背的单词
                // 如果应该获取过单词了，那么背诵结束
                if (!reciteData.isGetData()) {
                    recite = ReciteWordCreator.getSQLReciteWord(this, sqlRecite);
                    recite.getReciteData().setGetData(true);
                }
                break;
        }
        if (recite == null && !SQLHelper.isEmptyGWT(WGW, reciteData.getType(), sqlRecite)){
            recite = ReciteWordCreator.getReciteWrongWord(this, sqlRecite);
        }
        if (recite == null)
            finish();
        return recite;
    }

    protected void finish(){
        long time = System.currentTimeMillis();  // 完成时间
        // 是否存在错误单词
        if (!SQLHelper.isEmptyGWT(FHWGW, reciteData.getType(), DEFAULT_RECITE_TIME, sqlRecite)){
            reciteData.setHasWrongWord(true);
            // 拿到这次错误的单词
            List<GroupWord> groupWords = SQLHelper.queryGWT(FHWGW, DEFAULT_RECITE_TIME, reciteData.getType(), sqlRecite);
            // 设置设置单词的复制日期为一小时后
            SQLHelper.updateTimeGWT(FHWGW, ReciteTimeManager.turnSystemTimeToDetailTime(
                    time + ReciteTimeManager.HOUR), sqlRecite);
            // 把错误单词放入明天背诵表
            for (GroupWord groupWord : groupWords){
                SQLHelper.insertGWT(FDWGW, groupWord, ReciteTimeManager.turnSystemTimeToNormalTime(
                        time + ReciteTimeManager.DAY), reciteData.getType(), sqlRecite);
            }
        }
    }

    /**
     * 当一组单词背完后进行的数据保存
     */
    protected abstract void saveData();

    /**
     * 计算总共要背多少个单词
     */
    protected abstract void initTotal();

    /**
     * @return 是否还有单词要背
     */
    public abstract boolean available();

    /**
     * @return 返回下一个要背的单词
     */
    public abstract Word nextWord();

    /**
     * @return 剩余单词的文本表达
     */
    public abstract String getTotalText();

    public ChoiceMode getMode() {
        return mode;
    }

    public ReciteData getReciteData() {
        return reciteData;
    }
}
