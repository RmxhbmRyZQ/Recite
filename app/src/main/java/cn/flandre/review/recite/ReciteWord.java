package cn.flandre.review.recite;

import cn.flandre.review.data.GroupWord;
import cn.flandre.review.data.ReciteData;
import cn.flandre.review.data.Word;
import cn.flandre.review.database.SQLHelper;
import cn.flandre.review.database.SQLRecite;
import cn.flandre.review.enumerate.ChoiceMode;

import java.text.ParseException;
import java.util.List;

import static cn.flandre.review.database.SQLRecite.*;

public class ReciteWord extends Recite {

    public ReciteWord(List<GroupWord> list, ChoiceMode mode, ReciteData data, SQLRecite sqlRecite) {
        super(list, mode, data, sqlRecite);
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
        SQLHelper.updateGroupWord(groupWord, last, next, SQLRecite.getSQLRecite());
        // 从局部表删除这个组
        SQLHelper.deleteGWT(TMPGW, groupWord, sqlRecite);
        // 如果单词这组单词有单词错了，就插入错误表和一小时后复习表
        for (boolean bool : groupWord.getWrong()) {
            if (bool) {
                SQLHelper.insertGWT(WGW, groupWord, reciteData.getType(), sqlRecite);
                SQLHelper.insertGWT(FHWGW, groupWord, reciteData.getType(), sqlRecite);
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
