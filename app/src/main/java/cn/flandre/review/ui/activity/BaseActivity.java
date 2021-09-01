package cn.flandre.review.ui.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import cn.flandre.review.IReciteAidlInterface;
import cn.flandre.review.logic.callback.OnConnectService;
import cn.flandre.review.data.database.SQLRecite;
import cn.flandre.review.service.ReciteService;

/**
 * @author RmxhbmRyZQ 2021.8.30
 */
public class BaseActivity  extends AppCompatActivity {
    private OnConnectService onConnectService;

    public void setOnConnectService(OnConnectService onConnectService) {
        this.onConnectService = onConnectService;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupService();
        SQLRecite.getSQLRecite(this);  // 初始化 SQLRecite 单例
    }

    private void setupService(){
        startService(new Intent(this, ReciteService.class));

        reciteConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                reciteService = IReciteAidlInterface.Stub.asInterface(service);
                try {
                    if (onConnectService != null)
                        onConnectService.onConnect(reciteService);
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

    private IReciteAidlInterface reciteService;
    private ServiceConnection reciteConnection;

    public IReciteAidlInterface getService() {
        return reciteService;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(reciteConnection);
    }
}
