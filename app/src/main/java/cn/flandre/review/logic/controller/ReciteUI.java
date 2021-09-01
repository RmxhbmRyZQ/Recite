package cn.flandre.review.logic.controller;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public interface ReciteUI {
    public Button getRight();

    public Button getWrong();

    public Button getSuspect();

    public TextView getWord();

    public TextView getAccent();

    public TextView getMeans();

    public TextView getRemainder();

    public TextView getSentence();

    public TextView getSentenceMeans();

    public TextView getWordRoot();

    public ImageView getWordImage();

    public ImageView getSentenceImage();

    public TextView getExplanation();
}
