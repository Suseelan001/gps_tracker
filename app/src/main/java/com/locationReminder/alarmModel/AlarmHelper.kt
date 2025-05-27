package com.locationReminder.alarmModel

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.telephony.SmsManager
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.gson.Gson
import com.locationReminder.MainActivity
import com.locationReminder.roomDatabase.dao.LocationDAO
import com.locationReminder.R
import com.locationReminder.model.localStorage.MySharedPreference
import com.locationReminder.roomDatabase.dao.ContactDAO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class AlarmHelper @Inject constructor(
    private val context: Context,
    private val locationDAO: LocationDAO,
    private val contactDAO: ContactDAO,
    private val mySharedPreference: MySharedPreference

) {
    private var currentLocationId: Int? = null


    companion object {
        private var instance: AlarmHelper? = null
        private const val NOTIFICATION_ID = 1001

        fun getInstance(context: Context, locationDAO: LocationDAO, contactDAO: ContactDAO,mySharedPreference: MySharedPreference): AlarmHelper {
            if (instance == null) {
                instance = AlarmHelper(context.applicationContext, locationDAO,contactDAO,mySharedPreference)
            }
            return instance!!
        }

        private var ringtone: Ringtone? = null
        private var vibrator: Vibrator? = null
    }

    private var alarmIntent: PendingIntent? = null

    @RequiresApi(Build.VERSION_CODES.O)
    fun playAlarm(locationId: Int) {

        currentLocationId?.let {
            if (it != locationId) {
                stopAlarm(it)
            }
        }

        val location = locationDAO.getSingleRecord(locationId)
        stopAlarm()

        val alarmUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        ringtone = RingtoneManager.getRingtone(context, alarmUri).apply {
            audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ALARM)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()
            isLooping = true
            play()
        }

        CoroutineScope(Dispatchers.IO).launch {
            if (location.vibration == true) {
                vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                val pattern = longArrayOf(0, 1000, 1000)
                vibrator?.vibrate(VibrationEffect.createWaveform(pattern, 0))
            }
        }

        println("CHECK_TAG_sendNotification " + location.sendNotification)

        if (location.sendNotification) {
            println("CHECK_TAG_SEND_NOTIFICATION")
            sendSmsToAllContacts(mySharedPreference,contactDAO)
        }

        val fullScreenIntent = Intent(context, AlarmActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("location_id", locationId)
        }

        alarmIntent = PendingIntent.getActivity(
            context,
            0,
            fullScreenIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        showAlarmNotification(locationId)

        currentLocationId = locationId
    }


    fun stopAlarm(locationId: Int? = null) {
        ringtone?.stop()
        ringtone = null

        vibrator?.cancel()
        vibrator = null

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(NOTIFICATION_ID)

        if (locationId != null) {
            launchMainActivity()
            updateLocationStatusToFalse(locationId)
        }

        val intent = Intent("ACTION_STOP_LOCATION_UPDATES")
        context.sendBroadcast(intent)


        currentLocationId = null
    }




    private fun launchMainActivity() {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        context.startActivity(intent)
    }

    private fun updateLocationStatusToFalse(locationId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            locationDAO.updateCurrentStatus(locationId, false)
        }
    }

    fun snoozeAlarm(minutes: Int) {


        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val triggerTime = System.currentTimeMillis() + (minutes * 60 * 1000)

        val snoozeIntent = Intent(context, AlarmReceiver::class.java).apply {
            action = "ALARM_SNOOZE"
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            1,
            snoozeIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerTime,
            pendingIntent
        )
        stopAlarm()
    }

    private fun showAlarmNotification(locationId: Int) {
        val location = locationDAO.getSingleRecord(locationId)

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "alarm_channel",
                "Alarm Channel",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Used for alarm notifications"
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            }
            notificationManager.createNotificationChannel(channel)
        }

        val stopIntent = Intent(context, AlarmReceiver::class.java).apply {
            action = "STOP_ALARM"
            putExtra("location_id", locationId)
        }
        val stopPendingIntent = PendingIntent.getBroadcast(
            context,
            2,
            stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val (title, text) = when (location.entryType) {
            "Exit" -> "Location Exited" to "You have exited the location"
            else -> "Location Reached" to "You have arrived at your destination"
        }

        val notification = NotificationCompat.Builder(context, "alarm_channel")
            .setSmallIcon(R.drawable.notification)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setFullScreenIntent(alarmIntent, true)
            .setAutoCancel(true)
            .addAction(R.drawable.notification, "Stop", stopPendingIntent)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    } }

fun sendSmsToAllContacts( mySharedPreference: MySharedPreference,contactDAO: ContactDAO) {
    CoroutineScope(Dispatchers.IO).launch {
        val contacts = contactDAO.getAllContacts()
        val smsManager = SmsManager.getDefault()


        contacts.forEach { contact ->
            val message = "Hi ${mySharedPreference.getUserName()}, ${contact.name} has just crossed the predefined location boundary at [Address] at [Time]. If this movement is unexpected or unauthorized, we recommend immediate action"

            try {
                val parts = smsManager.divideMessage(message)
                smsManager.sendMultipartTextMessage(
                    contact.mobileNumber,
                    null,
                    parts,
                    null,
                    null
                )
            } catch (e: Exception) {
                Log.e("SMS", "Failed to send SMS to ${contact.mobileNumber}", e)
            }
        }
    }
}
