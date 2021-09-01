package cn.flandre.review.logic.recite;

import cn.flandre.review.data.bean.GroupWord;
import cn.flandre.review.data.bean.ReciteData;
import cn.flandre.review.data.database.SQLHelper;
import cn.flandre.review.data.database.SQLRecite;
import cn.flandre.review.logic.enumerate.ChoiceMode;

import java.util.List;

import static cn.flandre.review.data.database.SQLRecite.FDWGW;
import static cn.flandre.review.data.database.SQLRecite.FHWGW;
import static cn.flandre.review.logic.recite.ReciteTimeManager.DEFAULT_RECITE_TIME;

/**
 * @author RmxhbmRyZQ 2021.8.30
 */
public class ReciteFutureDayWord extends ReciteFutureHourWord {
    public ReciteFutureDayWord(List<GroupWord> list, ChoiceMode mode, ReciteData data, SQLRecite sqlRecite) {
        super(list, mode, data, sqlRecite);
    }

    @Override
    protected void saveData() {
        SQLHelper.updateGroupWord(groupWord, DEFAULT_RECITE_TIME, DEFAULT_RECITE_TIME, SQLRecite.getSQLRecite());
        deleteGW(FDWGW);
    }

    @Override
    public Recite getNext() {
        Recite recite;
        // 如果还有要背的单词继续背，否则进入下一套单词
        if (!SQLHelper.isEmptyGWT(FDWGW, reciteData.getType(), ReciteTimeManager.turnSystemTimeToNormalTime(reciteData.getTime()), sqlRecite)){
            recite = ReciteWordCreator.getReciteFutureDayWord(this, sqlRecite);
        }else {
            recite = super.getNext();
        }
        return  recite;
    }

    @Override
    public String getTotalText() {
        return "还剩 " + (total + 1) + " 个错单词(天)";
    }
}
