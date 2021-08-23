package cn.flandre.review.activity;

import android.content.Intent;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.flandre.review.R;

public class IndexActivity extends AppCompatActivity {

    @OnClick(R.id.reciteWord)
    public void reciteWord(View view){

    }

    @OnClick(R.id.reviewWord)
    public void reviewWord(View view){
        startActivity(new Intent(this, ReviewActivity.class));
    }

    @OnClick(R.id.recitePhrase)
    public void recitePhrase(View view){

    }

    @OnClick(R.id.reviewPhrase)
    public void reviewPhrase(View view){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        ButterKnife.bind(this);
    }
}