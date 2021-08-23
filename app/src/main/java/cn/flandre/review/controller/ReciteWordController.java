package cn.flandre.review.controller;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import cn.flandre.review.data.GroupWord;
import cn.flandre.review.database.SQLRecite;
import cn.flandre.review.enumerate.ChoiceMode;
import cn.flandre.review.fragment.ReviewWordFragment;
import cn.flandre.review.recite.Recite;
import cn.flandre.review.recite.ReciteWordCreator;
import cn.flandre.review.tools.AlarmHelper;

import java.util.List;

public class ReciteWordController implements ReciteController {
    private final ChoiceMode mode;
    private Recite recite;
    private Controller controller;
    private final ReviewWordFragment fragment;

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
        checkReciteAvailable();
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
//            try {
//                fragment.getService().delayNotify(10000);
//            } catch (RemoteException e) {
//                e.printStackTrace();
//            }
            AlarmHelper.scheduleTimeout(fragment.getContext(), 10 * 1000);
        }
        fragment.getFinish().setVisibility(View.VISIBLE);
        fragment.getWrapRecite().setVisibility(View.GONE);
    }

    @Override
    public Recite getRecite() {
        return recite;
    }

    @Override
    public Button getRight() {
        return fragment.getRight();
    }

    @Override
    public Button getWrong() {
        return fragment.getWrong();
    }

    @Override
    public TextView getWord() {
        return fragment.getWord();
    }

    @Override
    public TextView getAccent() {
        return fragment.getAccent();
    }

    @Override
    public TextView getMeans() {
        return fragment.getMeans();
    }

    @Override
    public TextView getRemainder() {
        return fragment.getRemainder();
    }
}
