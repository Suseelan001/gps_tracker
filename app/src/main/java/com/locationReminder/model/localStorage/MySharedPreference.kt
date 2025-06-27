package com.locationReminder.model.localStorage

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import javax.inject.Inject

class MySharedPreference @Inject constructor(context: Context) {

    private val sharedPreference: SharedPreferences =
        context.getSharedPreferences("LOCATION_REMINDER", MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreference.edit()



    fun saveUserId(userId: String) {
        editor.putString("USER-ID", userId)
        editor.commit()
    }

    fun getUserId(): String? {
        return sharedPreference.getString("USER-ID", null)
    }

    fun saveUserName(userId: String) {
        editor.putString("USER_NAME", userId)
        editor.commit()
    }

    fun getUserName(): String? {
        return sharedPreference.getString("USER_NAME", null)
    }

    fun setExitListExists(value: Boolean) {
        editor.putBoolean("EXIT-LIST-EXISTS", value)
        editor.commit()
    }

    fun getExitListExists(): Boolean? {
        return sharedPreference.getBoolean("EXIT-LIST-EXISTS", false)
    }





    fun clearAll() {
        editor.putString("USER-ID", null)
        editor.putString("USER_NAME", null)
        editor.putBoolean("EXIT-LIST-EXISTS", false)
        editor.commit()
    }

}