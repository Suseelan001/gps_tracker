package com.example.crmmainproject.model.localStorage

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import javax.inject.Inject

class MySharedPreference @Inject constructor(context: Context) {

    private val sharedPreference: SharedPreferences =
        context.getSharedPreferences("CRM", MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreference.edit()

    fun saveAppRegisterCompletedStatus() {
        editor.putBoolean("APP-REGISTRATION", true)
        editor.commit()
    }

    fun getAppRegisterCompletedStatus(): Boolean {
        return sharedPreference.getBoolean("APP-REGISTRATION", false)
    }

    fun saveAccessToken(strAccessToke: String) {
        editor.putString("ACCESS-TOKEN", strAccessToke)
        editor.commit()
    }

    fun getAccessToken(): String? {
        return sharedPreference.getString("ACCESS-TOKEN", null)
    }

    fun saveRoleKey(userId: String) {
        editor.putString("ROLE-KEY", userId)
        editor.commit()
    }
    fun saveConnectedAccountPaymentStatus(status: String) {
        editor.putString("CONNECTED-PAYMENT-STATUS", status)
        editor.commit()
    }
    fun getConnectedAccountPaymentStatus(): String? {
        return sharedPreference.getString("CONNECTED-PAYMENT-STATUS", "not-done")
    }
    fun getRoleKey(): String? {
        sharedPreference.getString("ROLE-KEY", null).let {
            if (it != null) {
                return it.lowercase()
            } else {
                return null
            }
        }
    }

    fun saveUserId(userId: String) {
        editor.putString("USER-ID", userId)
        editor.commit()
    }

    fun getUserId(): String? {
        return sharedPreference.getString("USER-ID", null)
    }

    fun saveSelectedCompanyId(userId: Int) {
        editor.putInt("SELECTED-COMPANY-ID", userId)
        editor.commit()
    }
    fun saveSelectedForeteeId(foreteeId: String) {
        editor.putString("SELECTED-COMPANY-FORETEE-ID", foreteeId)
        editor.commit()
    }

    fun saveSourceType(sourceType: String?) {
        editor.putString("SOURCE-TYPE", sourceType)
        editor.commit()
    }

    fun isConnectedAccount(): Boolean {

        return true
    }

    fun getSelectedCompanyId(): Int {
        return sharedPreference.getInt("SELECTED-COMPANY-ID", 0)
    }
    fun getSelectedCompanyForeteeId(): String? {
        return sharedPreference.getString("SELECTED-COMPANY-FORETEE-ID", "")
    }

    fun saveSelectedCompanyImage(image: String?) {
        editor.putString("SELECTED-COMPANY-IMAGE", image)
        editor.commit()
    }

    fun getSelectedCompanyImage(): String? {
        return sharedPreference.getString("SELECTED-COMPANY-IMAGE", null)
    }
    fun saveSelectedUserId(userId: Int) {
        editor.putInt("SELECTED_USER_ID", userId)
        editor.apply()
    }

    fun getSelectedUserId(): Int {
        return sharedPreference.getInt("SELECTED_USER_ID", 0)
    }

    fun saveStoreType(storeType: String?) {
        editor.putString("SELECTED-COMPANY-TYPE", storeType)
        editor.commit()
    }

    fun getStoreType(): String? {
        return sharedPreference.getString("SELECTED-COMPANY-TYPE", null)
    }

    fun saveMapMarkerUrl(url: String?) {
        editor.putString("MAP-MARKER-URL", url)
        editor.commit()
    }

    fun getMapMarkerUrl(): String? {
        return sharedPreference.getString("MAP-MARKER-URL", null)
    }

    fun clearAllForSyncing(){
        editor.putString("CONNECTED-PAYMENT-STATUS", null)
        editor.commit()
    }
    fun clearAll() {
        editor.putString("ACCESS-TOKEN", null)
        editor.putString("USER-ID", null)
        editor.putString("CONNECTED-PAYMENT-STATUS", null)
        editor.putInt("SELECTED-COMPANY-ID", 0)
        editor.putInt("SELECTED_USER_ID", 0)
        editor.putString("SELECTED-COMPANY-FORETEE-ID", null)
        editor.putString("SELECTED-COMPANY-IMAGE", null)
        editor.putString("SELECTED-COMPANY-TYPE", null)
        editor.putString("MAP-MARKER-URL", null)
        editor.putString("ROLE-KEY", null)
        editor.commit()
    }

    fun saveNewPasswordFlag(newPassword: Boolean) {
        editor.putBoolean("NEW-PASSWORD-FLAG", newPassword)
        editor.commit()
    }

    fun getNewPasswordFlag(): Boolean {
        return sharedPreference.getBoolean("NEW-PASSWORD-FLAG", false)
    }
}