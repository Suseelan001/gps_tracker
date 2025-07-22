package com.locationReminder.alarmModel

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import com.locationReminder.roomDatabase.dao.LocationDAO
import com.locationReminder.R
import com.locationReminder.model.localStorage.MySharedPreference
import com.locationReminder.roomDatabase.dao.ContactDAO
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AlarmActivity : ComponentActivity() {

    @Inject
    lateinit var locationDAO: LocationDAO
    @Inject
    lateinit var contactDAO: ContactDAO
    @Inject
    lateinit var sharedPreference: MySharedPreference

    @Inject lateinit var alarmHelper: AlarmHelper


    private var locationId: Int? = null
    private lateinit var leftCircle: View
    private lateinit var rightCircle: TextView
    private var dX = 0f

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)
        alarmHelper.setCurrentActivity(this)

        locationId = intent.getIntExtra("location_id", -1).takeIf { it != -1 }
        locationId?.let {
            val location = locationDAO.getSingleRecord(it.toInt())

            val alarmIcon = findViewById<ImageView>(R.id.iv_alarm_icon)
            val locationAddress = findViewById<TextView>(R.id.tv_location_name)
            val locationTitle = findViewById<TextView>(R.id.tv_title)
            locationAddress.text = location.address
            when (location.entryType) {
                "Entry" -> alarmIcon.setImageResource(R.drawable.onentry)
                "Exit" -> alarmIcon.setImageResource(R.drawable.onexit)
                "Marker" -> alarmIcon.setImageResource(R.drawable.marker)
                "ImportedMarker" -> alarmIcon.setImageResource(R.drawable.marker)
            }
            locationTitle.text = when (location.entryType) {
                "Exit" -> "Location Exited"
                else -> "Location Reached"
            }


        }


        leftCircle = findViewById(R.id.leftCircle)
        rightCircle = findViewById(R.id.rightCircle)

        val arrowContainer = findViewById<LinearLayout>(R.id.arrowContainer)

        leftCircle.post {
            val rightX = rightCircle.x
            val rightRadius = rightCircle.width / 2f
            val leftRadius = leftCircle.width / 2f
            val centerToCenterX = rightX + rightRadius - leftRadius

            leftCircle.setOnTouchListener { v, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        dX = v.x - event.rawX
                        v.performClick()
                        true
                    }

                    MotionEvent.ACTION_MOVE -> {
                        var newX = event.rawX + dX
                        newX = newX.coerceIn(0f, centerToCenterX)
                        v.x = newX

                        if (newX >= centerToCenterX - 5) {
                            v.x = centerToCenterX
                            arrowContainer.visibility = View.GONE
                            v.isEnabled = false

                            alarmHelper.stopAlarm(locationId)
                            finish()
                        }

                        true
                    }

                    else -> false
                }
            }
        }

        findViewById<Button>(R.id.btn_snooze).setOnClickListener {
            alarmHelper.snoozeAlarm(1)
            alarmHelper.stopAlarm(locationId)
            finish()
        }
        animateArrows()
    }

    fun animateArrows() {
        val arrowIds = listOf(R.id.arrow1, R.id.arrow2, R.id.arrow3, R.id.arrow4)
        arrowIds.forEachIndexed { index, id ->
            val arrow = findViewById<ImageView>(id)
            arrow.alpha = 0f
            val delay = index * 200L
            arrow.animate()
                .alpha(1f)
                .setStartDelay(delay)
                .setDuration(300)
                .withEndAction {
                    arrow.animate()
                        .alpha(0f)
                        .setStartDelay(200)
                        .setDuration(300)
                        .start()
                }
                .start()
        }

        Handler(Looper.getMainLooper()).postDelayed({
            animateArrows()
        }, 1200)
    }
}

