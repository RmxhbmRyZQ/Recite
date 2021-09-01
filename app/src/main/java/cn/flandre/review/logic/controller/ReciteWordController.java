package cn.flandre.review.logic.controller;

import android.view.View;
import android.widget.Toast;
import cn.flandre.review.logic.callback.OnRequestDetailData;
import cn.flandre.review.data.bean.DetailData;
import cn.flandre.review.data.bean.GroupWord;
import cn.flandre.review.data.database.SQLRecite;
import cn.flandre.review.logic.enumerate.ChoiceMode;
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
    private final ChoiceMode mode;
    private Recite recite;
    private Controller controller;
    private DetailDataSpeaker speaker;
    private final ReviewWordFragment fragment;
    private DetailData data;

    public static void saveTemporary(List<GroupWord> list, SQLRecite sqlRecite) {
        ETCController.saveTemporary(list, sqlRecite);
        CTEController.saveTemporary(list, sqlRecite);
    }

    public ReciteWordController(ChoiceMode mode, ReviewWordFragment fragment) {
        this.mode = mode;
        this.fragment = fragment;
    }

    @Override
    public void init() {
        recite = ReciteWordCreator.getReciteWord(mode, 0, SQLRecite.getSQLRecite());
        controller = new ETCController(this);
        speaker = new DetailDataSpeaker(() -> {
            Toast.makeText(fragment.getContext(), "该单词没有音频文件~", Toast.LENGTH_SHORT).show();
        });
        fragment.getWord().setOnClickListener(this::clickWord);
        fragment.getSentence().setOnClickListener(this::clickSentence);
        checkReciteAvailable();
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
        oneFinish(lastRecite);
//        twoFinish(lastRecite);
    }

    private void twoFinish(Recite lastRecite) {
        if (controller instanceof CTEController) {
            setComplete(lastRecite);
        } else {
            controller = new CTEController(this);
            recite = ReciteWordCreator.getReciteWord(ChoiceMode.CONTINUE,
                    lastRecite.getReciteData().isHasWrongWord(), 1, SQLRecite.getSQLRecite());
            checkReciteAvailable();
        }
    }

    private void oneFinish(Recite lastRecite) {
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
