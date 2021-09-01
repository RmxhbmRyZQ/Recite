package cn.flandre.review.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.flandre.review.R;
import cn.flandre.review.logic.callback.OnChoice;
import cn.flandre.review.logic.enumerate.ChoiceMode;

/**
 * @author RmxhbmRyZQ 2021.8.30
 */
public class ReviewWordChoiceFragment extends AttachFragment {
    public static final String TAG = "ReviewWordChoiceFragment";
    private OnChoice onChoice;

    @BindView(R.id.root)
    LinearLayout layout;

    public void setOnChoice(OnChoice onChoice) {
        this.onChoice = onChoice;
    }

    @OnClick({R.id.reviewContinue, R.id.reviewTotal, R.id.reviewPortion, R.id.reviewWrong})
    public void choice(View view){
        if (onChoice != null){
            switch (view.getId()){
                case R.id.reviewContinue:
                    onChoice.onChoice(ChoiceMode.CONTINUE);
                    break;
                case R.id.reviewTotal:
                    onChoice.onChoice(ChoiceMode.TOTAL);
                    break;
                case R.id.reviewPortion:
                    onChoice.onChoice(ChoiceMode.PORTION);
                    break;
                case R.id.reviewWrong:
                    onChoice.onChoice(ChoiceMode.WRONG);
                    break;
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choice_word_review, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void setVisible(boolean visible){
        layout.setVisibility(visible ? View.VISIBLE : View.GONE);
    }
}
