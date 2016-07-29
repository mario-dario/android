package com.development.dariopal.notifications;


import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.development.dariopal.DarioGlucoConst;
import com.development.dariopal.MainActivity;
import com.development.dariopal.R;

public class NotificationWrapper {

  private Context context;
  public static int NOTIFICATION_ID = 1234;
  Notification.Builder builder;
  Notification notification;
  NotificationManager nManager;

  public NotificationWrapper(Context context) {
    this.context = context;
  }

  @TargetApi(Build.VERSION_CODES.M)
  public void buildNotification() {

    nManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

    Intent intent = new Intent();
    intent.setComponent(new ComponentName(DarioGlucoConst.DARIO_LAUNCHER_ACTIVITY,"WelcomeActivity.class"));

//    Intent intent = context.getPackageManager().getLaunchIntentForPackage(DarioGlucoConst.DARIO_LAUNCHER_ACTIVITY);

    PendingIntent pendingIntent = PendingIntent.getActivity(context,
        NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    Icon icon;
    Notification.Action action = new Notification.Action(R.drawable.actionbuttongood, "open", pendingIntent);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
      builder = new Notification.Builder(context)
          .setContentTitle("SugarPal")
          .setContentText("All good! Sugar keeps an eye on you... ")
         .setContentIntent(pendingIntent)
          .addAction(action)
          .setSmallIcon(R.drawable.pushbigicon)
          .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.pushbigicon))
          .setPriority(Notification.PRIORITY_MAX);

    }

    notification = builder.build();

    notification.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;

    nManager.notify(NOTIFICATION_ID, notification);
  }

  public void updateNotification(String title, String body) {
//    NotificationCompat.Action newAction = new NotificationCompat.Action(R.mipmap.ic_launcher, "done")

    Intent intent = new Intent(context, MainActivity.class);

    PendingIntent pendingIntent = PendingIntent.getActivity(context,
        NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);


    builder.setContentTitle(title)
        .setContentText(body);


    //update icon as well
    // add action of "ok done"

    notification = builder.build();

    nManager.notify(NOTIFICATION_ID, notification);
  }

}
