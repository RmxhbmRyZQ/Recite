package cn.flandre.review.recite;

import cn.flandre.review.controller.ReciteWordController;
import cn.flandre.review.data.GroupWord;
import cn.flandre.review.data.ReciteData;
import cn.flandre.review.database.SQLHelper;
import cn.flandre.review.database.SQLRecite;
import cn.flandre.review.enumerate.ChoiceMode;

import java.util.ArrayList;
import java.util.List;

import static cn.flandre.review.database.SQLRecite.*;

public class ReciteWordCreator {
    public static Recite getReciteWord(ChoiceMode mode, int type, SQLRecite sqlRecite) {
        Recite recite = new ReciteFutureDayWord(new ArrayList<>(), mode, new ReciteData(false, type), sqlRecite);;
        if (!SQLHelper.isEmptyGWT(FDWGW, type, ReciteTimeManager.turnSystemTimeToNormalTime(recite.getReciteData().getTime()), sqlRecite)) {
            recite = getReciteFutureDayWord(recite, sqlRecite);
        }
        return recite;
    }

    public static Recite getReciteWord(ChoiceMode mode, boolean hasWrongWord, int type, SQLRecite sqlRecite) {
        Recite recite = getReciteWord(mode, type, sqlRecite);
        recite.getReciteData().setHasWrongWord(hasWrongWord);
        return recite;
    }

    public static Recite getReciteFutureDayWord(Recite recite, SQLRecite sqlRecite) {
        List<GroupWord> list = SQLHelper.queryTimeGWT(FDWGW, ReciteTimeManager.turnSystemTimeToNormalTime(
                recite.getReciteData().getTime()), recite.getReciteData().getType(), sqlRecite);
        return new ReciteFutureDayWord(list, recite.getMode(), recite.getReciteData(), sqlRecite);
    }

    public static Recite getReciteFutureHourWord(Recite recite, SQLRecite sqlRecite) {
        List<GroupWord> list = SQLHelper.queryTimeGWT(FHWGW, ReciteTimeManager.turnSystemTimeToDetailTime(
                recite.getReciteData().getTime()), recite.getReciteData().getType(), sqlRecite);
        return new ReciteFutureHourWord(list, recite.getMode(), recite.getReciteData(), sqlRecite);
    }

    public static Recite getReciteWrongWord(Recite recite, SQLRecite sqlRecite) {
        List<GroupWord> list = SQLHelper.queryGWT(WGW, recite.getReciteData().getType(), sqlRecite);
        return new ReciteWrongWord(list, recite.getMode(), recite.getReciteData(), sqlRecite);
    }

    public static Recite getTmpReciteWord(Recite recite, SQLRecite sqlRecite) {
        List<GroupWord> list = SQLHelper.queryGWT(TMPGW, recite.getReciteData().getType(), sqlRecite);
        return new ReciteWord(list, recite.getMode(), recite.getReciteData(), sqlRecite);
    }

    public static Recite getSQLReciteWord(Recite recite, SQLRecite sqlRecite) {
        List<GroupWord> list = SQLHelper.getGroupWords(sqlRecite);
        ReciteWordController.saveTemporary(list, sqlRecite);
        return new ReciteWord(list, recite.getMode(), recite.getReciteData(), sqlRecite);
    }
}
