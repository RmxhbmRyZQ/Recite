package cn.flandre.review.logic.controller;

import android.view.View;
import cn.flandre.review.data.bean.DetailData;
import cn.flandre.review.data.bean.GroupWord;
import cn.flandre.review.data.bean.Word;
import cn.flandre.review.data.database.SQLHelper;
import cn.flandre.review.data.database.SQLRecite;

import java.util.List;

import static cn.flandre.review.data.database.SQLRecite.TMPGW;

/**
 * @author RmxhbmRyZQ 2021.8.30
 * 英译中控制台
 */
public class ETCController extends Controller {
    public static final int TYPE = 0;
    private Word word;

    public ETCController(ReciteController controller) {
        super(controller);
    }

    @Override
    public int getType() {
        return TYPE;
    }

    @Override
    public void init(Word word) {
        ReciteUI reciteUI = controller.getReciteUI();
        if ((this.word = word = solveNull(word)) == null) {
            return;
        }
        reciteUI.getMeans().setVisibility(View.INVISIBLE);
        setExtraVisible(View.INVISIBLE);
        initUI(word);
    }

    @Override
    public void onGetDetailData(DetailData data) {
        controller.speakWord();
    }

    @Override
    public void onClickSuspect(boolean clickJudge) {
        setUI(clickJudge);
    }

    @Override
    public void onClickWrong(boolean clickJudge) {
        setUI(clickJudge);
    }

    @Override
    public void onClickRight(boolean clickJudge) {
        setUI(clickJudge);
    }

    @Override
    public Word getWord() {
        return word;
    }

    private void setUI(boolean clickJudge){
        ReciteUI reciteUI = controller.getReciteUI();
        if (clickJudge){
            word = controller.getRecite().nextWord();
            if ((word = solveNull(word)) == null) {
                return;
            }
            reciteUI.getMeans().setVisibility(View.INVISIBLE);
            setExtraVisible(View.INVISIBLE);
            initUI(word);
        }else {
            reciteUI.getMeans().setVisibility(View.VISIBLE);
            setExtraVisible(View.VISIBLE);
        }
    }

    public static void saveTemporary(List<GroupWord> list, SQLRecite sqlRecite){
        SQLHelper.clearGWT(TMPGW, TYPE, sqlRecite);
        for (GroupWord groupWord : list){
            SQLHelper.insertGWT(TMPGW, groupWord, TYPE, sqlRecite);
        }
    }
}
