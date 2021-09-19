package cn.flandre.review.tools;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.widget.EditText;
import cn.flandre.review.data.database.ShareHelper;
import cn.flandre.review.logic.callback.OnClickRadioDialog;

/**
 * @author RmxhbmRyZQ 2021.8.30
 */
public class DialogHelper {
    public static ProgressDialog getProcessDialog(Context context, String title) {
        ProgressDialog waitingDialog = new ProgressDialog(context);
        waitingDialog.setTitle(title);
        waitingDialog.setIndeterminate(true);  // 设置为不定进度条
        waitingDialog.setCancelable(false);
        return waitingDialog;
    }

    public static AlertDialog getSingleChoiceDialog(Context context, String[] items, String title
            , int checkPos, OnClickRadioDialog callback) {
        TmpData choice = new TmpData(checkPos);
        AlertDialog.Builder singleChoiceDialog = new AlertDialog.Builder(context);
        singleChoiceDialog.setTitle(title);
        // 给他加个输入框，虽然有点丑，但没有关系
        String reviewNumber = ShareHelper.getReviewNumber(context);
        final EditText editText = new EditText(context);
        editText.setHint("输入要背的单词组数，当前 " + reviewNumber);
        singleChoiceDialog.setView(editText);
        // 第二个参数是默认选项, 此处设置为0
        singleChoiceDialog.setSingleChoiceItems(items, checkPos, (dialog, which) -> choice.object = which);
        singleChoiceDialog.setPositiveButton("确定", (dialog, which) -> callback.onConfirm(
                (Integer) choice.object, editText.getText().toString()));

        return singleChoiceDialog.create();
    }

    private static class TmpData {
        Object object;

        public TmpData(Object object) {
            this.object = object;
        }
    }
}
