package cn.flandre.review.ui.fragment;

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
import cn.flandre.review.logic.controller.ReciteUI;
import cn.flandre.review.ui.activity.ReviewActivity;
import cn.flandre.review.logic.controller.ReciteController;
import cn.flandre.review.logic.controller.ReciteWordController;
import cn.flandre.review.logic.enumerate.ChoiceMode;

/**
 * @author RmxhbmRyZQ 2021.8.30
 */
public class ReviewWordFragment extends AttachFragment implements ReciteUI {
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
    @BindView(R.id.sentence)
    TextView sentence;
    @BindView(R.id.sentenceMeans)
    TextView sentenceMeans;
    @BindView(R.id.explanation)
    TextView explanation;
    @BindView(R.id.wordRoot)
    TextView wordRoot;
    @BindView(R.id.remainder)
    TextView remainder;

    @BindView(R.id.wordImage)
    ImageView wordImage;
    @BindView(R.id.sentenceImage)
    ImageView sentenceImage;

    @BindView(R.id.suspect)
    Button suspect;
    @BindView(R.id.wrong)
    Button wrong;
    @BindView(R.id.right)
    Button right;
    private ChoiceMode mode;

    @OnClick(R.id.suspect)
    public void clickSuspect(View view){
        controller.onSuspect(view);
    }

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

    @Override
    public TextView getWord() {
        return word;
    }

    @Override
    public TextView getAccent() {
        return accent;
    }

    @Override
    public TextView getMeans() {
        return means;
    }

    @Override
    public TextView getRemainder() {
        return remainder;
    }

    @Override
    public Button getWrong() {
        return wrong;
    }

    @Override
    public Button getRight() {
        return right;
    }

    @Override
    public TextView getSentence() {
        return sentence;
    }

    @Override
    public TextView getSentenceMeans() {
        return sentenceMeans;
    }

    @Override
    public TextView getWordRoot() {
        return wordRoot;
    }

    @Override
    public ImageView getWordImage() {
        return wordImage;
    }

    @Override
    public ImageView getSentenceImage() {
        return sentenceImage;
    }

    @Override
    public TextView getExplanation() {
        return explanation;
    }

    @Override
    public Button getSuspect() {
        return suspect;
    }

    public IReciteAidlInterface getService(){
        return ((ReviewActivity)mContext).getService();
    }
}
