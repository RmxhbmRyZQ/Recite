package cn.flandre.review.logic.recite;

import cn.flandre.review.data.bean.GroupWord;
import cn.flandre.review.data.bean.ReciteData;
import cn.flandre.review.data.database.SQLHelper;
import cn.flandre.review.data.database.SQLRecite;
import cn.flandre.review.logic.enumerate.ChoiceMode;

import java.util.List;

import static cn.flandre.review.data.database.SQLRecite.*;
import static cn.flandre.review.logic.recite.ReciteTimeManager.DEFAULT_RECITE_TIME;

/**
 * @author RmxhbmRyZQ 2021.8.30
 */
public class ReciteWrongWord extends ReciteSuspectWord {
    public ReciteWrongWord(List<GroupWord> list, ChoiceMode mode, ReciteData data, SQLRecite sqlRecite) {
        super(list, mode, data, sqlRecite);
    }

    @Override
    protected void saveData() {
        SQLHelper.updateGroupWord(groupWord, DEFAULT_RECITE_TIME, DEFAULT_RECITE_TIME, SQLRecite.getSQLRecite());
        // 如果还有错误就修改错误的单词位置，否则就删掉
        deleteGW(WGW);
    }

    protected void deleteGW(String tableName){
        boolean d = false;
        for (boolean b : groupWord.getWrong()) {
            if (b) {
                SQLHelper.updateGWT(tableName, groupWord, sqlRecite);
                d = true;
                break;
            }
        }

        if (!d) {
            SQLHelper.deleteGWT(tableName, groupWord, sqlRecite);
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
    public void setSuspect() {
        // 如果已经再错误表里面就没必要设模糊了，因为已经错误了
        setWrong();
    }

    @Override
    public String getTotalText() {
        return "还剩 " + (total + 1) + " 个错单词";
    }
}
