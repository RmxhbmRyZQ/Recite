package cn.flandre.review.recite;

import cn.flandre.review.data.GroupWord;
import cn.flandre.review.data.ReciteData;
import cn.flandre.review.data.Word;
import cn.flandre.review.database.SQLHelper;
import cn.flandre.review.database.SQLRecite;
import cn.flandre.review.enumerate.ChoiceMode;

import java.util.List;

import static cn.flandre.review.database.SQLRecite.*;
import static cn.flandre.review.recite.ReciteTimeManager.DEFAULT_RECITE_TIME;

public class ReciteWrongWord extends Recite {
    public ReciteWrongWord(List<GroupWord> list, ChoiceMode mode, ReciteData data, SQLRecite sqlRecite) {
        super(list, mode, data, sqlRecite);
    }

    @Override
    protected void saveData() {
        SQLHelper.updateGroupWord(groupWord, DEFAULT_RECITE_TIME, DEFAULT_RECITE_TIME, SQLRecite.getSQLRecite());
        // 如果还有错误就修改错误的单词位置，否则就删掉
        boolean d = false;
        for (boolean b : groupWord.getWrong()) {
            if (b) {
                SQLHelper.updateGWT(WGW, groupWord, sqlRecite);
                d = true;
                break;
            }
        }

        if (!d) {
            SQLHelper.deleteGWT(WGW, groupWord, sqlRecite);
        }
    }

    @Override
    public Recite getNext() {
        Recite recite;
        // 如果还有要背的单词继续背，否则进入下一套单词
        if (!SQLHelper.isEmptyGWT(WGW, reciteData.getType(), sqlRecite)) {
            recite = ReciteWordCreator.getReciteWrongWord(this, sqlRecite);
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
    public boolean available() {
        return total > 0;
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
    public String getTotalText() {
        return "还剩 " + (total + 1) + " 个错单词";
    }
}
