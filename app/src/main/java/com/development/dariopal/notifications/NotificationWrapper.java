package com.development.dariopal.notifications;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.development.dariopal.DarioGlucoConst;
import com.development.dariopal.MainActivity;
import com.development.dariopal.R;

public class NotificationWrapper {

  private Context context;
  private int NOTIFICATION_ID = 1234;
  Notification.Builder builder;
  Notification notification;
  NotificationManager nManager;

  public NotificationWrapper(Context context) {
    this.context = context;
  }

  public void buildNotification() {

    Intent intent = new Intent(context, Class.forName(DarioGlucoConst.DARIO_LAUNCHER_ACTIVITY));
    nManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

    PendingIntent pendingIntent = PendingIntent.getActivity(context,
        NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);

     builder = new Notification.Builder(context)
        .setContentTitle("SugarPal")
        .setContentText("All good! Sugar keeps an eye on you... ")
        .setContentIntent(pendingIntent)
        .setSmallIcon(R.mipmap.ic_launcher);

    notification = builder.build();

    notification.flags |= Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;

    nManager.notify(NOTIFICATION_ID, notification);
  }

  public void updateNotification(String title, String body) {
    builder.setContentTitle(title)
        .setContentText(body);

    //update icon as well
    // add action of "ok done"

    notification = builder.build();

    nManager.notify(NOTIFICATION_ID,notification);
  }

}
