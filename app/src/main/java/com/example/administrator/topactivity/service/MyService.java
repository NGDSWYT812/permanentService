package com.example.administrator.topactivity.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.example.administrator.topactivity.utils.FileUtil;
import com.example.administrator.topactivity.utils.Utils;

/**
 * Created by wangyt on 2015/11/30.
 * :
 */
public class MyService extends Service {
    private static final String TAG = "MyService";

    private static final int MSG_HANDLE_CHECK = 1;

    private static Handler mHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        FileUtil.writeLogtoSdcard(TAG, "onCreate");
        mHandler = new HandleRun();
        mHandler.sendEmptyMessage(MSG_HANDLE_CHECK);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        FileUtil.writeLogtoSdcard(TAG, "onStartCommand");
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeMessages(MSG_HANDLE_CHECK);
        FileUtil.writeLogtoSdcard(TAG, "onDestroy");
    }

    private void handleCheck() {
        FileUtil.writeLogtoSdcard(TAG, "check");
        if (!Utils.isServiceRunning(this, DaemonService.class.getName()))
            startService(new Intent(this, DaemonService.class));
        mHandler.sendEmptyMessageDelayed(MSG_HANDLE_CHECK, 1000 * 60);
    }

    class HandleRun extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_HANDLE_CHECK:
                    handleCheck();
                    break;
            }
        }
    }
}
