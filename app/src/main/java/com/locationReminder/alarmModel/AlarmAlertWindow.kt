package com.locationReminder.alarmModel

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PixelFormat
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.locationReminder.R
import com.locationReminder.databinding.ActivityAlarmBinding
import com.locationReminder.roomDatabase.dao.LocationDAO
import javax.inject.Inject
import javax.inject.Singleton
import dagger.Lazy



@Singleton
class AlarmAlertWindow @Inject constructor(
    private val locationDAO: LocationDAO,
    private val alarmHelper: Lazy<AlarmHelper>
)
{

    private var windowManager: WindowManager? = null
    private var windowLayout: ViewGroup? = null
    private var dX = 0f

    @SuppressLint("InflateParams")
    fun showWindow(context: Context, locationId: Int) {
        if (windowLayout != null) return

        windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val location = locationDAO.getSingleRecord(locationId)
        val inflater = LayoutInflater.from(context)
        val binding = ActivityAlarmBinding.inflate(inflater)

        val layoutParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.CENTER
        }

        binding.tvLocationName.text = location.address
        binding.tvTitle.text = if (location.entryType == "Exit") "Location Exited" else "Location Reached"

        val icon = when (location.entryType) {
            "Entry" -> R.drawable.onentry
            "Exit" -> R.drawable.onexit
            "Marker", "ImportedMarker" -> R.drawable.marker
            else -> R.drawable.ic_launcher_foreground
        }
        binding.ivAlarmIcon.setImageResource(icon)

        val leftCircle = binding.leftCircle
        val rightCircle = binding.rightCircle
        val arrowContainer = binding.arrowContainer

        Handler(Looper.getMainLooper()).post { animateArrows(binding) }

        leftCircle.post {
            val rightX = rightCircle.x
            val rightRadius = rightCircle.width / 2f
            val leftRadius = leftCircle.width / 2f
            val centerToCenterX = rightX + rightRadius - leftRadius

            leftCircle.setOnTouchListener { v, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        dX = v.x - event.rawX
                        v.performClick() // <-- Add this
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
                            alarmHelper.get().stopAlarm(locationId)
                            closeWindow()
                        }

                        true
                    }

                    else -> false
                }
            }
        }

        binding.btnSnooze.setOnClickListener {
            alarmHelper.get().snoozeAlarm(1)
            alarmHelper.get().stopAlarm(locationId)
            closeWindow()
        }

        windowLayout = binding.root
        windowManager?.addView(windowLayout, layoutParams)
    }

    private fun animateArrows(binding: ActivityAlarmBinding) {
        val arrows = listOf(binding.arrow1, binding.arrow2, binding.arrow3, binding.arrow4)
        arrows.forEachIndexed { index, arrow ->
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
            animateArrows(binding)
        }, 1200)
    }

    fun closeWindow() {
        windowLayout?.let {
            windowManager?.removeView(it)
            windowLayout = null
            windowManager = null
        }
    }
}

