package cn.flandre.review.logic.controller;

import android.content.Context;
import android.view.View;
import android.widget.Toast;
import cn.flandre.review.data.database.ShareHelper;
import cn.flandre.review.logic.callback.OnRequestDetailData;
import cn.flandre.review.data.bean.DetailData;
import cn.flandre.review.data.bean.GroupWord;
import cn.flandre.review.data.database.SQLRecite;
import cn.flandre.review.logic.enumerate.ChoiceMode;
import cn.flandre.review.logic.enumerate.ReviewMode;
import cn.flandre.review.logic.extradata.DetailDataFactory;
import cn.flandre.review.logic.extradata.DetailDataSpeaker;
import cn.flandre.review.ui.fragment.ReviewWordFragment;
import cn.flandre.review.logic.recite.Recite;
import cn.flandre.review.logic.recite.ReciteTimeManager;
import cn.flandre.review.logic.recite.ReciteWordCreator;
import cn.flandre.review.tools.AlarmHelper;

import java.util.List;

/**
 * @author RmxhbmRyZQ 2021.8.30
 * 背单词流程控制台，应该创建个新的包但想不到名字
 */
public class ReciteWordController implements ReciteController {
    // private static ReviewMode reviewMode;  // 歪门邪道
    private final ChoiceMode mode;
    private Recite recite;
    private Controller controller;
    private DetailDataSpeaker speaker;
    private final ReviewWordFragment fragment;
    private DetailData data;

    public static void saveTemporary(Context context, List<GroupWord> list, SQLRecite sqlRecite) {
        switch (ShareHelper.getReviewMode(context, ReviewMode.ENGLISH_MODE)){
            case LISTEN_MODE:
                LNController.saveTemporary(list, sqlRecite);
                break;
            case ENGLISH_MODE:
                ETCController.saveTemporary(list, sqlRecite);
                break;
            case CHINESE_MODE:
                CTEController.saveTemporary(list, sqlRecite);
                break;
            case ENGLISH_CHINESE_MODE:
                ETCController.saveTemporary(list, sqlRecite);
                CTEController.saveTemporary(list, sqlRecite);
                break;
        }
    }

    public ReciteWordController(ChoiceMode mode, ReviewWordFragment fragment) {
        this.mode = mode;
        this.fragment = fragment;
    }

    @Override
    public void init() {
        controller = createController();
        recite = ReciteWordCreator.getReciteWord(fragment.getContext(), mode, controller.getType(), SQLRecite.getSQLRecite());
        speaker = new DetailDataSpeaker(() -> {
            Toast.makeText(fragment.getContext(), "该单词没有音频文件~", Toast.LENGTH_SHORT).show();
        });
        fragment.getWord().setOnClickListener(this::clickWord);
        fragment.getAccent().setOnClickListener(this::clickWord);
        fragment.getSentence().setOnClickListener(this::clickSentence);
        checkReciteAvailable();
    }

    private Controller createController(){
        Controller controller = null;
        switch (ShareHelper.getReviewMode(fragment.getContext(), ReviewMode.ENGLISH_MODE)){
            case LISTEN_MODE:
                controller = new LNController(this);
                break;
            case ENGLISH_MODE:
            case ENGLISH_CHINESE_MODE:
                controller = new ETCController(this);
                break;
            case CHINESE_MODE:
                controller = new CTEController(this);
                break;
        }
        return controller;
    }

    private void clickWord(View view) {
        getDetailData(new OnRequestDetailData() {
            @Override
            public void onSuccess(DetailData data) {
                ReciteWordController.this.data = data;
                speaker.setData(data);
                speaker.playWordAudio();
            }

            @Override
            public void onFail(Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void clickSentence(View view) {
        getDetailData(new OnRequestDetailData() {
            @Override
            public void onSuccess(DetailData data) {
                ReciteWordController.this.data = data;
                speaker.setData(data);
                speaker.playSentenceAudio();
            }

            @Override
            public void onFail(Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void checkReciteAvailable() {
        Recite lastRecite = recite;
        if (!recite.available()) {
            while ((recite = nextRecite()) != null) {
                if (recite.available())
                    break;
            }
        }

        if (recite != null && recite.available()) {
            controller.init(recite.nextWord());
        } else {
            setComplete(lastRecite);
        }
    }

    @Override
    public void finish(Recite lastRecite) {
        switch (ShareHelper.getReviewMode(fragment.getContext(), ReviewMode.ENGLISH_MODE)){
            case LISTEN_MODE:
            case ENGLISH_MODE:
            case CHINESE_MODE:
                normalFinish(lastRecite);
                break;
            case ENGLISH_CHINESE_MODE:
                doubleFinish(lastRecite);
                break;
        }
    }

    private void doubleFinish(Recite lastRecite) {
        if (controller instanceof CTEController) {
            setComplete(lastRecite);
        } else {
            controller = new CTEController(this);
            recite = ReciteWordCreator.getReciteWord(fragment.getContext(), ChoiceMode.CONTINUE,
                    lastRecite.getReciteData().isHasWrongWord(), 1, SQLRecite.getSQLRecite());
            checkReciteAvailable();
        }
    }

    private void normalFinish(Recite lastRecite) {
        setComplete(lastRecite);
    }

    @Override
    public void onSuspect(View view) {
        controller.clickSuspect();
    }

    @Override
    public void onRight(View view) {
        controller.clickRight();
    }

    @Override
    public void onWrong(View view) {
        controller.clickWrong();
    }

    @Override
    public Recite nextRecite() {
        if (recite == null) return null;
        recite = recite.getNext();
        return recite;
    }

    public void setComplete(Recite lastRecite) {
        if (lastRecite.getReciteData().isHasWrongWord()) {
            AlarmHelper.scheduleTimeout(fragment.getContext(), ReciteTimeManager.HOUR / 1000);
        }
        fragment.getFinish().setVisibility(View.VISIBLE);
        fragment.getWrapRecite().setVisibility(View.GONE);
    }

    @Override
    public Recite getRecite() {
        return recite;
    }

    @Override
    public ReciteUI getReciteUI() {
        return fragment;
    }

    @Override
    public void getDetailData(OnRequestDetailData detailData) {
        if (data != null && controller.getWord().getId() == Integer.parseInt(this.data.getWordId())){
            detailData.onSuccess(data);
            return;
        }
        DetailDataFactory.requestDetailData(fragment.getContext(), controller.getWord(), detailData);
    }

    @Override
    public void speakWord() {
        clickWord(null);
    }

    @Override
    public void speakSentence() {
        clickSentence(null);
    }
}
