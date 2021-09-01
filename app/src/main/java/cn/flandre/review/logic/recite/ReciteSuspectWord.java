package cn.flandre.review.logic.recite;

import cn.flandre.review.data.bean.GroupWord;
import cn.flandre.review.data.bean.ReciteData;
import cn.flandre.review.data.bean.Word;
import cn.flandre.review.data.database.SQLHelper;
import cn.flandre.review.data.database.SQLRecite;
import cn.flandre.review.logic.enumerate.ChoiceMode;

import java.util.List;

import static cn.flandre.review.data.database.SQLRecite.*;
import static cn.flandre.review.logic.recite.ReciteTimeManager.DEFAULT_RECITE_TIME;

/**
 * @author RmxhbmRyZQ 2021.9.1
 */
public class ReciteSuspectWord extends ReciteWord {
    public ReciteSuspectWord(List<GroupWord> list, ChoiceMode mode, ReciteData data, SQLRecite sqlRecite) {
        super(list, mode, data, sqlRecite);
    }

    @Override
    protected void saveData() {
        SQLHelper.updateGroupWord(groupWord, DEFAULT_RECITE_TIME, DEFAULT_RECITE_TIME, SQLRecite.getSQLRecite());
        // 把错误的单词加入错误表
        for (boolean bool : groupWord.getWrong()) {
            if (bool) {
                saveGW(WGW);
                saveGW(FHWGW);
                break;
            }
        }

        boolean d = false;
        for (boolean b : groupWord.getSuspect()) {
            if (b) {
                boolean[] wrong = groupWord.getWrong();
                groupWord.setWrong(groupWord.getSuspect());
                SQLHelper.updateGWT(SGW, groupWord, sqlRecite);
                groupWord.setWrong(wrong);
                d = true;
                break;
            }
        }

        if (!d) {
            SQLHelper.deleteGWT(SGW, groupWord, sqlRecite);
        }
    }

    private void saveGW(String tableName) {
        GroupWord groupWord = SQLHelper.queryGWT(tableName, this.groupWord, sqlRecite);
        if (groupWord != null) {
            for (int i = 0; i < groupWord.getWrong().length; i++) {
                this.groupWord.getWrong()[i] |= groupWord.getWrong()[i];
            }
            SQLHelper.updateGWT(tableName, this.groupWord, sqlRecite);
        } else {
            SQLHelper.insertGWT(tableName, this.groupWord, reciteData.getType(), sqlRecite);
        }
    }

    @Override
    public Recite getNext() {
        Recite recite;
        // 如果还有要背的单词继续背，否则进入下一套单词
        if (!SQLHelper.isEmptyGWT(SGW, reciteData.getType(), sqlRecite)) {
            recite = ReciteWordCreator.getReciteSuspectWord(this, sqlRecite);
        } else {
            recite = super.getNext();
        }
        return recite;
    }

    @Override
    protected void initTotal() {
        // 仅计算错误了的单词
        total = 0;
        for (GroupWord groupWord : list) {
            for (boolean b : groupWord.getWrong()) {
                if (b)
                    total++;
            }
        }
    }

    @Override
    public Word nextWord() {
        // 获取下一个错误了的单词
        Word word;
        while ((word = getNextWord()) != null) {
            if (groupWord.getWrong()[wordAuxiliary.get(wordAuxiliaryIndex)]) {
                break;
            }
        }
        total--;
        return word;
    }

    @Override
    public void setSuspect() {
        // 设置索引为模糊
        int pos = wordAuxiliary.get(wordAuxiliaryIndex);
        groupWord.getSuspect()[pos] = true;
        groupWord.getWrong()[pos] = false;
    }

    @Override
    public void setRight() {
        // 在错题本里面答对不应该计算正确次数
        int pos = wordAuxiliary.get(wordAuxiliaryIndex);
        groupWord.getWrong()[pos] = false;
    }

    @Override
    public String getTotalText() {
        return "还剩 " + (total + 1) + " 个模糊单词";
    }
}
