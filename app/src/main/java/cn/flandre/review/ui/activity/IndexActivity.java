package cn.flandre.review.ui.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.flandre.review.R;
import cn.flandre.review.data.bean.GroupWord;
import cn.flandre.review.data.database.SQLHelper;
import cn.flandre.review.data.database.SQLRecite;
import cn.flandre.review.data.database.ShareHelper;
import cn.flandre.review.logic.enumerate.ReviewMode;
import cn.flandre.review.logic.recite.ReciteTimeManager;
import cn.flandre.review.tools.DialogHelper;
import cn.flandre.review.tools.GroupWordHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import static cn.flandre.review.logic.enumerate.ReviewMode.ENGLISH_MODE;
import static cn.flandre.review.logic.enumerate.ReviewMode.ITEMS;

/**
 * @author RmxhbmRyZQ 2021.8.30
 */
public class IndexActivity extends BaseActivity {
    private AlertDialog dialog;

    @OnClick(R.id.reciteWord)
    public void reciteWord(View view) {

    }

    @OnClick(R.id.reviewWord)
    public void reviewWord(View view) {
        startActivity(new Intent(this, ReviewActivity.class));
    }

    @OnClick(R.id.recitePhrase)
    public void recitePhrase(View view) {

    }

    @OnClick(R.id.reviewPhrase)
    public void reviewPhrase(View view) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        ButterKnife.bind(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 防止以后APP要重装导致数据丢失，特意添加导出菜单
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_index_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                try {
                    long l = ReciteTimeManager.turnSystemTimeToDetailTime(System.currentTimeMillis());
                    File file = new File(getExternalFilesDir(null), "database" + l + ".txt");
                    OutputStreamWriter outputStream = new OutputStreamWriter(new FileOutputStream(file));
                    List<GroupWord> allGroupWords = SQLHelper.getAllGroupWords(SQLRecite.getSQLRecite());
                    StringBuilder builder = new StringBuilder();
                    for (GroupWord groupWord : allGroupWords) {
                        builder.append(GroupWordHelper.getWordId(groupWord)).append(":")
                                .append(GroupWordHelper.getWrongIndex(groupWord)).append(":")
                                .append(GroupWordHelper.getCorrectIndex(groupWord)).append(":")
                                .append(GroupWordHelper.getWrongTimes(groupWord)).append(":")
                                .append(GroupWordHelper.getRightTimes(groupWord)).append(":")
                                .append(groupWord.getReciteTime()).append(":")
                                .append(groupWord.getLastReciteTime()).append(":")
                                .append(groupWord.getNextReciteTime()).append(":")
                                .append(groupWord.getReciteTimes()).append("\n");
                    }
                    outputStream.write(builder.toString());
                    outputStream.close();
                    Toast.makeText(this, "背诵信息保存成功", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.setting:
                ReviewMode reviewMode = ShareHelper.getReviewMode(this, ENGLISH_MODE);
                dialog = DialogHelper.getSingleChoiceDialog(this, ITEMS, "选择背诵模式",
                        reviewMode.getIndex(), pos -> {
                            ShareHelper.setReviewMode(IndexActivity.this, ReviewMode.parseInt(pos));
                            dialog.cancel();
                        });
                dialog.show();
                break;
        }
        return true;
    }
}