package com.locationReminder.view

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.locationReminder.MainActivity
import com.locationReminder.model.apiUtil.serviceModel.BaseNetworkSyncClass
import com.locationReminder.reponseModel.FCMDataModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Random
import javax.inject.Inject
import com.locationReminder.R



@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val channelId = "AlertGo_channel_id"



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.data.isNotEmpty()) {
            val messageMap = remoteMessage.data
            val data=Gson().toJson(messageMap)

            Log.d("FCM", "Message data payload: ${remoteMessage.data}")


            val listType = object : TypeToken<FCMDataModel>() {}.type
            val modelData= Gson().fromJson<FCMDataModel>(data, listType)
            if (modelData!=null){
                sendNotification(modelData)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendNotification(modelData: FCMDataModel) {
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra("notification_data", Gson().toJson(modelData))
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            101,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.notification)
            .setContentTitle(modelData.title)
            .setContentText(modelData.body)
            .setAutoCancel(true)
            .setSound(soundUri)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(
            channelId,
            "Channel to receive updates.",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)

        val notificationId = getRandomNotificationId()
        notificationManager.notify(notificationId, notificationBuilder.build())
    }


    override fun onNewToken(token: String) {
        println("CHECK_TAG_NEW_TOKEN_ " + token)
       // updatePushToken(token)
    }


    private fun getRandomNotificationId(): Int {
        val random = Random()
        return random.nextInt(10000)
    }
}