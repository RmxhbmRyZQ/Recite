package cn.flandre.review.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;
import cn.flandre.review.IReciteAidlInterface;
import cn.flandre.review.R;
import cn.flandre.review.callback.OnChoice;
import cn.flandre.review.enumerate.ChoiceMode;
import cn.flandre.review.fragment.ReviewWordChoiceFragment;
import cn.flandre.review.fragment.ReviewWordFragment;
import cn.flandre.review.service.ReciteService;

public class ReviewActivity extends AppCompatActivity implements OnChoice {
    private ReviewWordChoiceFragment reviewWordChoiceFragment;
    private ReviewWordFragment reviewWordFragment;
    private IReciteAidlInterface reciteService;
    private ServiceConnection reciteConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        setupService();
        setupToolBar();
        setupFragment(savedInstanceState);
    }

    private void setupService(){
        startService(new Intent(this, ReciteService.class));

        reciteConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                reciteService = IReciteAidlInterface.Stub.asInterface(service);
                try {
                    reciteService.cancelNotify();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        Intent intent = new Intent(this, ReciteService.class);
        bindService(intent, reciteConnection, Context.BIND_AUTO_CREATE);
    }

    private void setupToolBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void setupFragment(Bundle savedInstanceState){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (savedInstanceState == null){
            reviewWordChoiceFragment = new ReviewWordChoiceFragment();
            fragmentTransaction.add(R.id.root,  reviewWordChoiceFragment, ReviewWordChoiceFragment.TAG);
            fragmentTransaction.commit();
        }else {
            reviewWordChoiceFragment = (ReviewWordChoiceFragment) getSupportFragmentManager().findFragmentByTag(ReviewWordChoiceFragment.TAG);
            reviewWordFragment = (ReviewWordFragment) getSupportFragmentManager().findFragmentByTag(ReviewWordFragment.TAG);
            fragmentTransaction.attach(reviewWordChoiceFragment);
            if (reviewWordFragment != null)
                fragmentTransaction.attach(reviewWordFragment);
        }
        reviewWordChoiceFragment.setOnChoice(this);
    }

    @Override
    public void onChoice(ChoiceMode choice) {
        reviewWordFragment = ReviewWordFragment.newInstance(choice);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.root, reviewWordFragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(reciteConnection);
    }

    public IReciteAidlInterface getService() {
        return reciteService;
    }
}
