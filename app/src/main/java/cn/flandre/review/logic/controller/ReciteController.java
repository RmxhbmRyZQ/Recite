package cn.flandre.review.logic.controller;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import cn.flandre.review.data.bean.DetailData;
import cn.flandre.review.logic.callback.OnRequestDetailData;
import cn.flandre.review.logic.recite.Recite;

/**
 * @author RmxhbmRyZQ 2021.8.30
 */
public interface ReciteController {
    public void init();

    public void finish(Recite lastRecite);

    void onSuspect(View view);

    public void onRight(View view);

    public void onWrong(View view);

    public Recite nextRecite();

    public Recite getRecite();

    public ReciteUI getReciteUI();

    public void getDetailData(OnRequestDetailData detailData);

    public void speakWord();

    public void speakSentence();
}
