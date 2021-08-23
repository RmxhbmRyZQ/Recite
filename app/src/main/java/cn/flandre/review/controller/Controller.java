package cn.flandre.review.controller;

import cn.flandre.review.data.Word;
import cn.flandre.review.recite.Recite;

abstract public class Controller {
    public static final String MAX_REVIEW_GROUP_COUNT = "20";

    ReciteController controller;
    boolean clickJudge;

    public Controller(ReciteController controller) {
        this.controller = controller;
    }

    protected Word solveNull(Word word){
        if (word == null){
            Recite lastRecite = controller.getRecite();;
            Recite recite = controller.nextRecite();
            if (recite == null){
                controller.finish(lastRecite);
                return null;
            }else {
                word = recite.nextWord();
                return solveNull(word);
            }
        }
        return word;
    }

    public void clickWrong(){
        if (clickJudge){
            controller.getRecite().setWrong();
            controller.getWrong().setText("不认识");
            controller.getRight().setText("我认识");
        }else {
            controller.getWrong().setText("猜错了");
            controller.getRight().setText("我对了");
        }
        onClickWrong(clickJudge);
        clickJudge = !clickJudge;
    }

    public void clickRight(){
        if (clickJudge){
            controller.getRecite().setRight();
            controller.getWrong().setText("不认识");
            controller.getRight().setText("我认识");
        }else {
            controller.getWrong().setText("猜错了");
            controller.getRight().setText("我对了");
        }
        onClickRight(clickJudge);
        clickJudge = !clickJudge;
    }

    abstract public void init(Word word);

    abstract public void onClickWrong(boolean clickJudge);

    abstract public void onClickRight(boolean clickJudge);
}
