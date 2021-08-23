package cn.flandre.review.controller;

import android.view.View;
import cn.flandre.review.data.GroupWord;
import cn.flandre.review.data.Word;
import cn.flandre.review.database.SQLHelper;
import cn.flandre.review.database.SQLRecite;

import java.util.List;

import static cn.flandre.review.database.SQLRecite.TMPGW;

public class ETCController extends Controller {
    public static final int TYPE = 0;

    public ETCController(ReciteController controller) {
        super(controller);
    }

    @Override
    public void init(Word word) {
        if ((word = solveNull(word)) == null) {
            return;
        }
        controller.getMeans().setVisibility(View.INVISIBLE);
        controller.getWord().setText(word.getWord());
        controller.getAccent().setText(word.getAccent());
        controller.getMeans().setText(word.getMeans());
        controller.getRemainder().setText(controller.getRecite().getTotalText());
    }

    @Override
    public void onClickWrong(boolean clickJudge) {
        setUI(clickJudge);
    }

    @Override
    public void onClickRight(boolean clickJudge) {
        setUI(clickJudge);
    }

    private void setUI(boolean clickJudge){
        if (clickJudge){
            Word word = controller.getRecite().nextWord();
            if ((word = solveNull(word)) == null) {
                return;
            }
            controller.getWord().setText(word.getWord());
            controller.getAccent().setText(word.getAccent());
            controller.getMeans().setText(word.getMeans());
            controller.getRemainder().setText(controller.getRecite().getTotalText());
            controller.getMeans().setVisibility(View.INVISIBLE);
        }else {
            controller.getMeans().setVisibility(View.VISIBLE);
        }
    }

    public static void saveTemporary(List<GroupWord> list, SQLRecite sqlRecite){
        SQLHelper.clearGWT(TMPGW, TYPE, sqlRecite);
        for (GroupWord groupWord : list){
            SQLHelper.insertGWT(TMPGW, groupWord, TYPE, sqlRecite);
        }
    }
}
