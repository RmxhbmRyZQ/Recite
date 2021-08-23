package cn.flandre.review.recite;

import cn.flandre.review.data.GroupWord;
import cn.flandre.review.data.ReciteData;
import cn.flandre.review.database.SQLHelper;
import cn.flandre.review.database.SQLRecite;
import cn.flandre.review.enumerate.ChoiceMode;

import java.util.List;

import static cn.flandre.review.database.SQLRecite.FDWGW;
import static cn.flandre.review.database.SQLRecite.FHWGW;
import static cn.flandre.review.recite.ReciteTimeManager.DEFAULT_RECITE_TIME;

public class ReciteFutureDayWord extends ReciteFutureHourWord {
    public ReciteFutureDayWord(List<GroupWord> list, ChoiceMode mode, ReciteData data, SQLRecite sqlRecite) {
        super(list, mode, data, sqlRecite);
    }

    @Override
    protected void saveData() {
        SQLHelper.updateGroupWord(groupWord, DEFAULT_RECITE_TIME, DEFAULT_RECITE_TIME, SQLRecite.getSQLRecite());
        boolean d = false;
        // 当该组有错误单词时，更新日错误数据库，并把错误加到一小时后背诵里
        // 背完后一小时背诵又会加到明天背诵
        for (boolean b : groupWord.getWrong()) {
            if (b) {
                SQLHelper.updateGWT(FDWGW, groupWord, sqlRecite);
                SQLHelper.insertIfNotExists(FHWGW, groupWord, DEFAULT_RECITE_TIME, reciteData.getType(), sqlRecite);
                d = true;
                break;
            }
        }
        // 没有错误单词就删除改组
        if (!d) {
            SQLHelper.deleteGWT(FDWGW, groupWord, sqlRecite);
        }
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
