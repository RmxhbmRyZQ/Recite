package cn.flandre.review.logic.callback;

import cn.flandre.review.data.bean.DetailData;

public interface OnRequestDetailData {
    public void onSuccess(DetailData data);

    public void onFail(Exception e);
}
