package cn.flandre.review.fragment;

import android.app.Activity;
import android.content.Context;
import androidx.fragment.app.Fragment;

public abstract class AttachFragment extends Fragment {
    Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }
}
