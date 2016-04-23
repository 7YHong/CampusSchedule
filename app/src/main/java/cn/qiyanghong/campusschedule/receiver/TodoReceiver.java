package cn.qiyanghong.campusschedule.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import cn.qiyanghong.campusschedule.MainActivity;
import cn.qiyanghong.campusschedule.R;
import cn.qiyanghong.campusschedule.utils.L;

/**
 * Created by QYH on 2016/4/23.
 */
public class TodoReceiver extends BroadcastReceiver{
    public static final String WAKE_ACTION="campusschedule.todo";
    public static final String TAG=TodoReceiver.class.getName();
    @Override
    public void onReceive(Context context, Intent intent) {
        L.i(TAG,"onReceive");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("Received");
        builder.setContentText("I had receive the alarm");
        builder.setContentInfo("Content Info");
        builder.setWhen(System.currentTimeMillis());
        Intent activityIntent = new Intent(context, MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();
        NotificationManager manager= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0,notification);
    }
}
