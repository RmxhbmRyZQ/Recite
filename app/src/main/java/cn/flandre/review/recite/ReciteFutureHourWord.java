package cn.flandre.review.recite;

import cn.flandre.review.data.GroupWord;
import cn.flandre.review.data.ReciteData;
import cn.flandre.review.database.SQLHelper;
import cn.flandre.review.database.SQLRecite;
import cn.flandre.review.enumerate.ChoiceMode;

import java.util.List;

import static cn.flandre.review.database.SQLRecite.*;
import static cn.flandre.review.recite.ReciteTimeManager.DEFAULT_RECITE_TIME;

public class ReciteFutureHourWord extends ReciteWrongWord {

    public ReciteFutureHourWord(List<GroupWord> list, ChoiceMode mode, ReciteData data, SQLRecite sqlRecite) {
        super(list, mode, data, sqlRecite);
    }

    @Override
    protected void saveData() {
        SQLHelper.updateGroupWord(groupWord, DEFAULT_RECITE_TIME, DEFAULT_RECITE_TIME, SQLRecite.getSQLRecite());
        boolean d = false;
        // 如果还有错误就修改错误的单词位置，否则就删掉
        for (boolean b : groupWord.getWrong()) {
            if (b) {
                SQLHelper.updateGWT(FHWGW, groupWord, sqlRecite);
                d = true;
                break;
            }
        }

        if (!d) {
            SQLHelper.deleteGWT(FHWGW, groupWord, sqlRecite);
        }
    }

    @Override
    public Recite getNext() {
        Recite recite;
        // 如果还有要背的单词继续背，否则进入下一套单词
        if (!SQLHelper.isEmptyGWT(FHWGW, reciteData.getType(), ReciteTimeManager.turnSystemTimeToDetailTime(reciteData.getTime()), sqlRecite)) {
            recite = ReciteWordCreator.getReciteFutureHourWord(this, sqlRecite);
        } else {
            recite = super.getNext();
        }
        return recite;
    }

    @Override
    public String getTotalText() {
        return "还剩 " + (total + 1) + " 个错单词(时)";
    }
}
