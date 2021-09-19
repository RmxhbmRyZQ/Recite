package cn.flandre.review.logic.controller;

import android.graphics.BitmapFactory;
import cn.flandre.review.data.bean.DetailData;
import cn.flandre.review.data.bean.Word;
import cn.flandre.review.logic.callback.OnRequestDetailData;
import cn.flandre.review.logic.recite.Recite;

/**
 * @author RmxhbmRyZQ 2021.8.30
 * UI 显示的控制台
 */
abstract public class Controller {
    ReciteController controller;
    boolean clickJudge;

    public Controller(ReciteController controller) {
        this.controller = controller;
    }

    protected Word solveNull(Word word) {
        if (word == null) {
            Recite lastRecite = controller.getRecite();
            ;
            Recite recite = controller.nextRecite();
            if (recite == null) {
                controller.finish(lastRecite);
                return null;
            } else {
                word = recite.nextWord();
                return solveNull(word);
            }
        }
        return word;
    }

    public void clickSuspect() {
        ReciteUI reciteUI = controller.getReciteUI();
        if (clickJudge) {
            controller.getRecite().setSuspect();
            reciteUI.getWrong().setText("不认识");
            reciteUI.getRight().setText("我认识");
        } else {
            reciteUI.getWrong().setText("猜错了");
            reciteUI.getRight().setText("我对了");
        }
        onClickSuspect(clickJudge);
        clickJudge = !clickJudge;
    }

    public void clickWrong() {
        ReciteUI reciteUI = controller.getReciteUI();
        if (clickJudge) {
            controller.getRecite().setWrong();
            reciteUI.getWrong().setText("不认识");
            reciteUI.getRight().setText("我认识");
        } else {
            reciteUI.getWrong().setText("猜错了");
            reciteUI.getRight().setText("我对了");
        }
        onClickWrong(clickJudge);
        clickJudge = !clickJudge;
    }

    public void clickRight() {
        ReciteUI reciteUI = controller.getReciteUI();
        if (clickJudge) {
            controller.getRecite().setRight();
            reciteUI.getWrong().setText("不认识");
            reciteUI.getRight().setText("我认识");
        } else {
            reciteUI.getWrong().setText("猜错了");
            reciteUI.getRight().setText("我对了");
        }
        onClickRight(clickJudge);
        clickJudge = !clickJudge;
    }

    protected void initUI(Word word) {
        ReciteUI reciteUI = controller.getReciteUI();
        reciteUI.getWord().setText(word.getWord());
        reciteUI.getAccent().setText(word.getAccent());
        reciteUI.getMeans().setText(word.getMeans());
        reciteUI.getRemainder().setText(controller.getRecite().getTotalText());
        controller.getDetailData(new OnRequestDetailData() {
            @Override
            public void onSuccess(DetailData data) {
                if (data.getWordImagePath() != null)
                    reciteUI.getWordImage().setImageBitmap(BitmapFactory.decodeFile(data.getWordImagePath()));
                else
                    reciteUI.getWordImage().setImageBitmap(null);
                if (data.getSentenceImagePath() != null)
                    reciteUI.getSentenceImage().setImageBitmap(BitmapFactory.decodeFile(data.getSentenceImagePath()));
                else
                    reciteUI.getSentenceImage().setImageBitmap(null);

                reciteUI.getSentence().setText(data.getSentence());
                reciteUI.getSentenceMeans().setText(data.getSentenceMeans());
                reciteUI.getExplanation().setText(data.getWordExplanation());
                reciteUI.getWordRoot().setText(data.getWordRoot());
                onGetDetailData(data);
            }

            @Override
            public void onFail(Exception e) {
                e.printStackTrace();
            }
        });
    }

    protected void setExtraVisible(int visibility) {
        ReciteUI reciteUI = controller.getReciteUI();
        reciteUI.getWordImage().setVisibility(visibility);
        reciteUI.getSentenceImage().setVisibility(visibility);
        reciteUI.getSentence().setVisibility(visibility);
        reciteUI.getSentenceMeans().setVisibility(visibility);
        reciteUI.getExplanation().setVisibility(visibility);
        reciteUI.getWordRoot().setVisibility(visibility);
    }

    abstract public int getType();

    abstract public void init(Word word);

    abstract public void onGetDetailData(DetailData data);

    abstract public void onClickSuspect(boolean clickJudge);

    abstract public void onClickWrong(boolean clickJudge);

    abstract public void onClickRight(boolean clickJudge);

    abstract public Word getWord();
}
