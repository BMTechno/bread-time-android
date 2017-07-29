package com.bread.time.breadtime.firebaseMessaging;

import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFcmListenerService extends  MyFirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage message) {
        String from = message.getFrom();
        Map data = message.getData();
    }
}
