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
 * 中译英控制台
 */
public class CTEController extends Controller {
    public static final int TYPE = 1;
    private Word word;

    public CTEController(ReciteController controller) {
        super(controller);
    }

    @Override
    public void init(Word word) {
        if ((this.word = word = solveNull(word)) == null) {
            return;
        }
        ReciteUI reciteUI = controller.getReciteUI();
        reciteUI.getWord().setVisibility(View.INVISIBLE);
        reciteUI.getAccent().setVisibility(View.INVISIBLE);
        initUI(word);
    }

    @Override
    public void onGetDetailData(DetailData data) {

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
            reciteUI.getAccent().setVisibility(View.INVISIBLE);
            reciteUI.getWord().setVisibility(View.INVISIBLE);
            initUI(word);
        }else {
            reciteUI.getWord().setVisibility(View.VISIBLE);
            reciteUI.getAccent().setVisibility(View.VISIBLE);
        }
    }

    public static void saveTemporary(List<GroupWord> list, SQLRecite sqlRecite){
        SQLHelper.clearGWT(TMPGW, TYPE, sqlRecite);
        for (GroupWord groupWord : list){
            SQLHelper.insertGWT(TMPGW, groupWord, TYPE, sqlRecite);
        }
    }
}
