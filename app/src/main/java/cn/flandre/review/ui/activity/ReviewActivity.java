package cn.flandre.review.ui.activity;

import androidx.appcompat.app.ActionBar;
import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;
import cn.flandre.review.IReciteAidlInterface;
import cn.flandre.review.R;
import cn.flandre.review.logic.callback.OnChoice;
import cn.flandre.review.logic.enumerate.ChoiceMode;
import cn.flandre.review.ui.fragment.ReviewWordChoiceFragment;
import cn.flandre.review.ui.fragment.ReviewWordFragment;

/**
 * @author RmxhbmRyZQ 2021.8.30
 */
public class ReviewActivity extends BaseActivity implements OnChoice {
    private ReviewWordChoiceFragment reviewWordChoiceFragment;
    private ReviewWordFragment reviewWordFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        setOnConnectService(IReciteAidlInterface::cancelNotify);

        setupToolBar();
        setupFragment(savedInstanceState);
    }

    private void setupToolBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void setupFragment(Bundle savedInstanceState) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (savedInstanceState == null) {
            reviewWordChoiceFragment = new ReviewWordChoiceFragment();
            fragmentTransaction.add(R.id.root, reviewWordChoiceFragment, ReviewWordChoiceFragment.TAG);
            fragmentTransaction.commit();
        } else {
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
}
