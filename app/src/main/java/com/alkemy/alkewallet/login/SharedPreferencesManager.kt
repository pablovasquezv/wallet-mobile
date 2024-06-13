package com.alkemy.alkewallet.login

import android.content.Context
import android.content.SharedPreferences
import com.alkemy.alkewallet.R

class SessionManager(private val context: Context) {

    fun saveAuthToken(token: String) {
        saveString(USER_TOKEN, token)
    }

    fun getToken(): String? {
        return getString(USER_TOKEN)
    }

    fun saveString(key: String, value: String) {
        val prefs: SharedPreferences =
            context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString(key, value)
        editor.apply()

    }

    fun getString(key: String): String? {
        val prefs: SharedPreferences =
            context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
        return prefs.getString(Companion.USER_TOKEN, null)
    }

    fun clearData() {
        val editor =
            context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
                .edit()
        editor.clear()
        editor.apply()
    }

    companion object {
        const val USER_TOKEN = "user_token"
    }
}