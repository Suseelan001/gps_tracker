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

    fun setArea(userId: String) {
        editor.putString("AREA_NAME", userId)
        editor.commit()
    }

    fun getArea(): String? {
        return sharedPreference.getString("AREA_NAME", null)
    }


    fun setImportList(value: Boolean) {
        editor.putBoolean("IMPORT_LIST", value)
        editor.commit()
    }



    fun getImportList(): Boolean? {
        return sharedPreference.getBoolean("IMPORT_LIST", false)
    }

    fun getNewUser(): Boolean? {
        return sharedPreference.getBoolean("IS_NEW_USER", true)
    }

    fun setNewUser(value: Boolean) {
        editor.putBoolean("IS_NEW_USER", value)
        editor.commit()
    }

    fun clearAll() {
        editor.putString("USER-ID", null)
        editor.putString("USER_NAME", null)
        editor.putBoolean("IMPORT_LIST", false)
        editor.commit()
    }

}