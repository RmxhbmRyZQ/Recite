package cn.flandre.review.service;

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.RemoteException;
import cn.flandre.review.IReciteAidlInterface;
import cn.flandre.review.tools.NotificationHelper;

import java.lang.ref.WeakReference;

/**
 * @author RmxhbmRyZQ 2021.8.30
 */
public class ReciteService extends Service {
    public static final String NOTIFICATION_BROADCAST = "review.flandre.cn.notification";

    private IBinder mBinder;
    private boolean hasForeground = false;
    private ReciteReceiver receiver;
    private Notification notification;

    public ReciteService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mBinder = new ServiceStub(this);
        receiver = new ReciteReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(NOTIFICATION_BROADCAST);
        registerReceiver(receiver, filter);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    private void delayNotify(long time) throws RemoteException {
//        NotificationHelper.notify(this);
//        startForeground(NotificationHelper.NOTIFICATION_ID, NotificationHelper.getNotification(ReciteService.this));
//        AlarmHelper.scheduleTimeout(this, time);
    }

    private void cancelNotify() throws RemoteException {
        if (hasForeground) {
            stopForeground(true);
            hasForeground = false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null)
            unregisterReceiver(receiver);
        stopForeground(true);
    }

    private static final class ServiceStub extends IReciteAidlInterface.Stub {
        private final WeakReference<ReciteService> mService;

        public ServiceStub(ReciteService mService) {
            this.mService = new WeakReference<>(mService);
        }

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public void delayNotify(long time) throws RemoteException {
            mService.get().delayNotify(time);
        }

        @Override
        public void cancelNotify() throws RemoteException {
            mService.get().cancelNotify();
        }
    }

    private class ReciteReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case NOTIFICATION_BROADCAST:
                    if (notification == null)
                        notification = NotificationHelper.getNotification(ReciteService.this);
                    NotificationHelper.notify(ReciteService.this, notification);
                    hasForeground = true;
                    break;
            }
        }
    }
}