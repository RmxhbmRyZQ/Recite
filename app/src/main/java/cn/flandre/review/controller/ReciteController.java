package cn.flandre.review.controller;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import cn.flandre.review.recite.Recite;

public interface ReciteController {
    public void init();

    public void finish(Recite lastRecite);

    public void onRight(View view);

    public void onWrong(View view);

    public Recite nextRecite();

    public Recite getRecite();

    public Button getRight();

    public Button getWrong();

    public TextView getWord();

    public TextView getAccent();

    public TextView getMeans();

    public TextView getRemainder();
}
