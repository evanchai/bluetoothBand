/*
 * Copyright (C) 2010 Moduad Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.androidpn.client;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.ble_boombandui.dao.AndroidpnMsgDao;
import com.example.ble_boombandui.model.AndroidpnMsg;
import com.example.ble_boombandui.msgbox.MsgBox;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/** 
 * Broadcast receiver that handles push notification messages from the server.
 * This should be registered as receiver in AndroidManifest.xml. 
 * 
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public final class NotificationReceiver extends BroadcastReceiver {

    private static final String LOGTAG = LogUtil
            .makeLogTag(NotificationReceiver.class);

    //    private NotificationService notificationService;
    private AndroidpnMsgDao dao;
    
    public NotificationReceiver() {
    }

    //    public NotificationReceiver(NotificationService notificationService) {
    //        this.notificationService = notificationService;
    //    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(LOGTAG, "NotificationReceiver.onReceive()...");
        String action = intent.getAction();
        Log.d(LOGTAG, "action=" + action);

        if (Constants.ACTION_SHOW_NOTIFICATION.equals(action)) {
            String notificationId = intent
                    .getStringExtra(Constants.NOTIFICATION_ID);
            String notificationApiKey = intent
                    .getStringExtra(Constants.NOTIFICATION_API_KEY);
            String notificationTitle = intent
                    .getStringExtra(Constants.NOTIFICATION_TITLE);
            String notificationMessage = intent
                    .getStringExtra(Constants.NOTIFICATION_MESSAGE);
            String notificationUri = intent
                    .getStringExtra(Constants.NOTIFICATION_URI);
            String notificationFrom = intent
            		.getStringExtra(Constants.NOTIFICATION_FROM);
            String packetId = intent
    				.getStringExtra(Constants.PACKET_ID);
            
            Log.d(LOGTAG, "notificationId=" + notificationId);
            Log.d(LOGTAG, "notificationApiKey=" + notificationApiKey);
            Log.d(LOGTAG, "notificationTitle=" + notificationTitle);
            Log.d(LOGTAG, "notificationMessage=" + notificationMessage);
            Log.d(LOGTAG, "notificationUri=" + notificationUri);

           
        
//        -----------收到消息存到手机本地------------
            dao=new AndroidpnMsgDao(context);
            Date date=new Date(); 
            SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
            String date_str=df.format(date);
            AndroidpnMsg androidpnMsg=new AndroidpnMsg();
            androidpnMsg.setDate(date_str);
            androidpnMsg.setMsg(notificationMessage);
            androidpnMsg.setTitle(notificationTitle);
            androidpnMsg.setIsread(0);
            int msgid=dao.save(androidpnMsg);
            
//         --------------------------------
            
            Notifier notifier = new Notifier(context);
            notifier.notify(notificationId, notificationApiKey,
                    notificationTitle, notificationMessage, notificationUri,notificationFrom,packetId,msgid);
        }
        

    }

}
