package cn.flandre.review.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import cn.flandre.review.R;
import cn.flandre.review.data.database.SQLInit;
import cn.flandre.review.tools.DialogHelper;

/**
 * @author RmxhbmRyZQ 2021.8.30
 */
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (SQLInit.available(this)) {
            ProgressDialog processDialog = DialogHelper.getProcessDialog(this, "数据加载中~");
            processDialog.show();
            // 延迟发让进度弹窗显示
            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(() -> {
                SQLInit.init(this);
                processDialog.cancel();

                startActivity(new Intent(this, IndexActivity.class));
                finish();
            }, 50);
        } else {
            startActivity(new Intent(this, IndexActivity.class));
            finish();
        }
    }
}
