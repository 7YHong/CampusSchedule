package cn.qiyanghong.campusschedule.receiver;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import cn.qiyanghong.campusschedule.utils.L;
import cn.qiyanghong.campusschedule.utils.TodoManager;

/**
 * 运行在主进程中
 * 通过创建前台进程
 * 重新注册提醒
 */
public class WakeReceiver extends BroadcastReceiver {

    private final static String TAG = WakeReceiver.class.getSimpleName();
    Context context;

    /**
     * 灰色保活手段唤醒广播的action
     */
    public final static String GRAY_WAKE_ACTION = "com.wake.gray";

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context=context;
        String action = intent.getAction();
        if (GRAY_WAKE_ACTION.equals(action)) {
            L.i(TAG, "wake !! wake !! ");

            Intent wakeIntent = new Intent(context, WakeNotifyService.class);
            context.startService(wakeIntent);
        }
    }

    /**
     * 用于其他进程来唤醒UI进程用的Service
     */
    public static class WakeNotifyService extends Service {

        @Override
        public void onCreate() {
            L.i(TAG, "WakeNotifyService->onCreate");
            TodoManager.getInstance(getApplicationContext());
            super.onCreate();
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            L.i(TAG, "WakeNotifyService->onStartCommand");
            return super.onStartCommand(intent, flags, startId);
        }

        @Override
        public IBinder onBind(Intent intent) {
            // TODO: Return the communication channel to the service.
            //throw new UnsupportedOperationException("Not yet implemented");
            return null;
        }

        @Override
        public void onDestroy() {
            L.i(TAG, "WakeNotifyService->onDestroy");
            super.onDestroy();
        }
    }
}
