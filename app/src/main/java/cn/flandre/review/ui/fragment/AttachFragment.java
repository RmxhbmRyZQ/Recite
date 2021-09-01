package cn.flandre.review.ui.fragment;

import android.app.Activity;
import android.content.Context;
import androidx.fragment.app.Fragment;
import org.jetbrains.annotations.NotNull;

/**
 * @author RmxhbmRyZQ 2021.8.30
 */
public abstract class AttachFragment extends Fragment {
    Context mContext;

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onAttach(@NotNull Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }
}
