package cn.flandre.review.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.flandre.review.IReciteAidlInterface;
import cn.flandre.review.R;
import cn.flandre.review.activity.ReviewActivity;
import cn.flandre.review.controller.ReciteController;
import cn.flandre.review.controller.ReciteWordController;
import cn.flandre.review.enumerate.ChoiceMode;

public class ReviewWordFragment extends AttachFragment {
    public final static String TAG = "ReviewWordFragment";

    private ReciteController controller;

    @BindView(R.id.root)
    FrameLayout layout;
    @BindView(R.id.finish)
    TextView finish;

    @BindView(R.id.recite)
    RelativeLayout wrapRecite;

    @BindView(R.id.word)
    TextView word;
    @BindView(R.id.accent)
    TextView accent;
    @BindView(R.id.means)
    TextView means;
    @BindView(R.id.remainder)
    TextView remainder;

    @BindView(R.id.wrong)
    Button wrong;
    @BindView(R.id.right)
    Button right;
    private ChoiceMode mode;

    @OnClick(R.id.wrong)
    public void clickWrong(View view){
        controller.onWrong(view);
    }

    @OnClick(R.id.right)
    public void clickRight(View view){
        controller.onRight(view);
    }

    public static ReviewWordFragment newInstance(ChoiceMode choice) {
        ReviewWordFragment reviewWordFragment = new ReviewWordFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("Choice", choice);
        reviewWordFragment.setArguments(bundle);
        return reviewWordFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null)
            mode = (ChoiceMode) arguments.getSerializable("Choice");
        else
            mode = ChoiceMode.CONTINUE;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_word_review, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        controller = new ReciteWordController(mode, this);
        controller.init();
    }

    public void setVisible(boolean visible){
        layout.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public TextView getFinish() {
        return finish;
    }

    public RelativeLayout getWrapRecite() {
        return wrapRecite;
    }

    public TextView getWord() {
        return word;
    }

    public TextView getAccent() {
        return accent;
    }

    public TextView getMeans() {
        return means;
    }

    public TextView getRemainder() {
        return remainder;
    }

    public Button getWrong() {
        return wrong;
    }

    public Button getRight() {
        return right;
    }

    public IReciteAidlInterface getService(){
        return ((ReviewActivity)mContext).getService();
    }
}
