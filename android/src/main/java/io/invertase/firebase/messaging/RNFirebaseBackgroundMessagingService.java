package io.invertase.firebase.messaging;

import android.app.Notification;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.facebook.react.HeadlessJsTaskService;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.jstasks.HeadlessJsTaskConfig;
import com.google.firebase.messaging.RemoteMessage;

import javax.annotation.Nullable;

public class RNFirebaseBackgroundMessagingService extends HeadlessJsTaskService {
  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    HeadlessJsTaskConfig taskConfig = getTaskConfig(intent);
    if (taskConfig != null) {

      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        Notification notification = new Notification();
        startForeground(1, notification);
        startTask(taskConfig);
      } else {
        startTask(taskConfig);
      }

      return START_REDELIVER_INTENT;
    }
    return START_NOT_STICKY;
  }

  @Override
  protected @Nullable HeadlessJsTaskConfig getTaskConfig(Intent intent) {
    Bundle extras = intent.getExtras();
    if (extras != null) {
      RemoteMessage message = intent.getParcelableExtra("message");
      WritableMap messageMap = MessagingSerializer.parseRemoteMessage(message);
      return new HeadlessJsTaskConfig(
        "RNFirebaseBackgroundMessage",
        messageMap,
        60000,
        false
      );
    }
    return null;
  }
}
