package cn.flandre.review.tools;

import android.app.ProgressDialog;
import android.content.Context;

public class DialogHelper {
    public static ProgressDialog getProcessDialog(Context context){
        ProgressDialog waitingDialog = new ProgressDialog(context);
        waitingDialog.setTitle("数据加载中~");
        waitingDialog.setIndeterminate(true);  // 设置为不定进度条
        waitingDialog.setCancelable(false);
        waitingDialog.show();
        return waitingDialog;
    }
}
