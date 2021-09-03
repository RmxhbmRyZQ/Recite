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
 * @author RmxhbmRyZQ 2021.9.2
 */
public class LNController extends Controller {
    public static final int TYPE = 2;
    private Word word;

    public LNController(ReciteController controller) {
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
        reciteUI.getWord().setVisibility(View.INVISIBLE);
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

    private void setUI(boolean clickJudge) {
        ReciteUI reciteUI = controller.getReciteUI();
        if (clickJudge){
            word = controller.getRecite().nextWord();
            if ((word = solveNull(word)) == null) {
                return;
            }
            reciteUI.getWord().setVisibility(View.INVISIBLE);
            reciteUI.getMeans().setVisibility(View.INVISIBLE);
            setExtraVisible(View.INVISIBLE);
            initUI(word);
        }else {
            reciteUI.getWord().setVisibility(View.VISIBLE);
            reciteUI.getMeans().setVisibility(View.VISIBLE);
            setExtraVisible(View.VISIBLE);
        }
    }

    @Override
    public Word getWord() {
        return word;
    }

    public static void saveTemporary(List<GroupWord> list, SQLRecite sqlRecite){
        SQLHelper.clearGWT(TMPGW, TYPE, sqlRecite);
        for (GroupWord groupWord : list){
            SQLHelper.insertGWT(TMPGW, groupWord, TYPE, sqlRecite);
        }
    }
}
