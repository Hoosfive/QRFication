package ru.qrfication.QRFication.model

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import ru.qrfication.QRFication.domain.LocationBody
import ru.qrfication.QRFication.model.response.UserInfoResponseBody

class Preferences {
    companion object {
        private const val USER_DATA_PREFERENCES = "UserData"
        private const val UID_PREFERENCES = "id"
        private const val EMAIL_PREFERENCES = "email"
        private const val FIRST_NAME_PREFERENCES = "firstName"
        private const val LAST_NAME_PREFERENCES = "lastName"
        private const val LAST_LONGITUDE = "lastLongitude"
        private const val LAST_LATITUDE = "lastLatitude"
        const val DEF_VALUE = "no_data_found"
        private lateinit var editor: SharedPreferences.Editor

        private fun getPref(context: Context): SharedPreferences {
            return context.getSharedPreferences(USER_DATA_PREFERENCES, MODE_PRIVATE)
        }

        fun editUIDPref(context: Context, id: String) {
            editor = getPref(
                context
            ).edit()
            editor.putString(UID_PREFERENCES, id)
            editor.apply()
        }

        fun editUserInfoPrefs(
            context: Context,
            uid: String,
            email: String,
            firstName: String,
            lastName: String
        ) {
            editor = getPref(
                context
            ).edit()
            editor.putString(UID_PREFERENCES, uid)
            editor.putString(EMAIL_PREFERENCES, email)
            editor.putString(FIRST_NAME_PREFERENCES, firstName)
            editor.putString(LAST_NAME_PREFERENCES, lastName)
            editor.apply()
        }

        fun getUIDPref(context: Context): String? {
            return getPref(context).getString(
                UID_PREFERENCES,
                DEF_VALUE
            )
        }

        fun getUserInfoPrefs(context: Context): UserInfoResponseBody {
            val pref = getPref(context)
            return UserInfoResponseBody(
                pref.getString(
                    UID_PREFERENCES,
                    DEF_VALUE
                ).toString(),
                pref.getString(
                    EMAIL_PREFERENCES,
                    DEF_VALUE
                ).toString(),
                pref.getString(
                    FIRST_NAME_PREFERENCES,
                    DEF_VALUE
                ).toString(),
                pref.getString(
                    LAST_NAME_PREFERENCES,
                    DEF_VALUE
                ).toString()
            )
        }

        fun saveLastLocationPref(context: Context, lon: Double, lan: Double) {
            editor = getPref(
                context
            ).edit()
            editor.putFloat(LAST_LONGITUDE, lon.toFloat())
            editor.putFloat(LAST_LATITUDE, lan.toFloat())
            editor.apply()
        }

        fun getLastLocationPref(context: Context): LocationBody {
            val pref = getPref(context)
            return LocationBody(
                pref.getFloat(LAST_LONGITUDE, 55.30771F).toDouble(),
                pref.getFloat(LAST_LATITUDE, 25.20314F).toDouble()
            )
        }
    }
}