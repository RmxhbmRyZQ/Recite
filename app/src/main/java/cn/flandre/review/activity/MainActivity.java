package cn.flandre.review.activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import cn.flandre.review.R;
import cn.flandre.review.database.SQLHelper;
import cn.flandre.review.database.SQLRecite;
import cn.flandre.review.service.ReciteService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SQLRecite sqlRecite = SQLRecite.getSQLRecite(getApplicationContext());
        SQLHelper.addAllWord(this, sqlRecite);
        SQLHelper.addAllGroupData(this, sqlRecite);

        startService(new Intent(this, ReciteService.class));
        startActivity(new Intent(this, IndexActivity.class));
        finish();
    }
}
