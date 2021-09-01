package cn.flandre.review.logic.recite;

import cn.flandre.review.data.bean.GroupWord;
import cn.flandre.review.data.bean.ReciteData;
import cn.flandre.review.data.bean.Word;
import cn.flandre.review.data.database.SQLHelper;
import cn.flandre.review.data.database.SQLRecite;
import cn.flandre.review.logic.enumerate.ChoiceMode;

import java.text.ParseException;
import java.util.List;

import static cn.flandre.review.data.database.SQLRecite.*;

/**
 * @author RmxhbmRyZQ 2021.8.30
 */
public class ReciteWord extends Recite {

    public ReciteWord(List<GroupWord> list, ChoiceMode mode, ReciteData data, SQLRecite sqlRecite) {
        super(list, mode, data, sqlRecite);
    }

    @Override
    public void setSuspect() {
        // 设置索引为模糊
        int pos = wordAuxiliary.get(wordAuxiliaryIndex);
        groupWord.getSuspect()[pos] = true;
    }

    @Override
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

    @Override
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

    @Override
    protected void saveData() {
        // 先更新数据库信息
        long last = ReciteTimeManager.turnSystemTimeToNormalTime(reciteData.getTime());
        long next;
        try {
            next = ReciteTimeManager.calculationNextTime(groupWord, reciteData.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            next = ReciteTimeManager.turnSystemTimeToNormalTime(reciteData.getTime() + 7L * ReciteTimeManager.DAY);
        }
        groupWord.increaseReciteTimes();
        SQLHelper.updateGroupWord(groupWord, last, next, SQLRecite.getSQLRecite());
        // 从临时表删除这个组
        SQLHelper.deleteGWT(TMPGW, groupWord, sqlRecite);
        // 如果单词这组单词有单词错了，就插入错误表和一小时后复习表
        for (boolean bool : groupWord.getWrong()) {
            if (bool) {
                SQLHelper.insertGWT(WGW, groupWord, reciteData.getType(), sqlRecite);
                SQLHelper.insertGWT(FHWGW, groupWord, reciteData.getType(), sqlRecite);
                break;
            }
        }
        saveSuspectData();
    }

    protected void saveSuspectData() {
        for (boolean bool : groupWord.getSuspect()) {
            if (bool) {
                boolean[] wrong = groupWord.getWrong();
                groupWord.setWrong(groupWord.getSuspect());
                SQLHelper.insertGWT(SGW, groupWord, reciteData.getType(), sqlRecite);
                groupWord.setWrong(wrong);
                break;
            }
        }
    }

    @Override
    protected void initTotal() {
        total = 0;
        switch (mode) {
            case CONTINUE:
            case TOTAL:
                // 每一个单词都是有效的
                for (GroupWord groupWord : list) {
                    total += groupWord.getWord().length;
                }
                break;
            case WRONG:
                // 仅计算常错误的单词
                for (GroupWord groupWord : list) {
                    for (boolean b : groupWord.getWrongIndex()) {
                        if (b) total++;
                    }
                }
                break;
            case PORTION:
                // 去掉常对的单词
                for (GroupWord groupWord : list) {
                    for (boolean b : groupWord.getCorrectIndex()) {
                        if (!b) total++;
                    }
                }
                break;
        }
    }

    @Override
    public boolean available() {
        return total > 0;
    }

    @Override
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
        // 这个代码不应该放在这里，不符合设计原则
        // 可以使用一个接口链表，使用静态注册方法分散到各个子类里面，但小项目懒得搞
        if (recite == null && !SQLHelper.isEmptyGWT(SGW, reciteData.getType(), sqlRecite)){
            recite = ReciteWordCreator.getReciteSuspectWord(this, sqlRecite);
        }
        if (recite == null && !SQLHelper.isEmptyGWT(WGW, reciteData.getType(), sqlRecite)) {
            recite = ReciteWordCreator.getReciteWrongWord(this, sqlRecite);
        }
        if (recite == null)
            finish();
        return recite;
    }

    @Override
    public Word nextWord() {
        Word word = null;
        switch (mode) {
            case CONTINUE:
            case TOTAL:
                // 直接获取下一个单词
                word = getNextWord();
                break;
            case WRONG:
                // 如果不是错误单词就继续获取下一个
                while ((word = getNextWord()) != null) {
                    if (groupWord.getWrongIndex()[wordAuxiliary.get(wordAuxiliaryIndex)])
                        break;
                }
                break;
            case PORTION:
                // 如果不是非常对单词就继续获取下一个
                while ((word = getNextWord()) != null) {
                    if (!groupWord.getCorrectIndex()[wordAuxiliary.get(wordAuxiliaryIndex)])
                        break;
                }
                break;
        }
        total--;
        return word;
    }

    @Override
    public String getTotalText() {
        return "还剩 " + (total + 1) + " 个单词";
    }
}
