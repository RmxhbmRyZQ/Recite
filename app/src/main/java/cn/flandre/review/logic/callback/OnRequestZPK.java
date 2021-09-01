package cn.flandre.review.logic.callback;

import java.io.IOException;

public interface OnRequestZPK {
    public void onSuccess(byte[] zpk);

    public void onFail(IOException e);
}
