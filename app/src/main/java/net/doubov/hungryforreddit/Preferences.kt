package net.doubov.hungryforreddit

import android.content.SharedPreferences
import androidx.core.content.edit

class Preferences(private val sharedPreferences: SharedPreferences) {

    companion object {
        const val NAME = "Preferences"
    }

    object Keys {
        const val ACCESS_TOKEN = "access_token"
    }

    var accessToken: String?
        get() = sharedPreferences.getString(Keys.ACCESS_TOKEN, null)
        set(value) = sharedPreferences.edit { putString(Keys.ACCESS_TOKEN, value) }
}