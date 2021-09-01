package cn.flandre.review.logic.callback;

import android.os.RemoteException;
import cn.flandre.review.IReciteAidlInterface;

public interface OnConnectService {
    public void onConnect(IReciteAidlInterface service) throws RemoteException;
}
