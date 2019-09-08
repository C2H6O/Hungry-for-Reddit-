package net.doubov.core

import android.content.Context
import androidx.core.content.edit

class UserPreferences(
    context: Context
) : Preferences(context, USER_PREFERENCES_NAME) {

    object Keys {
        const val USERNAME = "USERNAME"
    }

    var username: String?
        get() = prefs.getString(Keys.USERNAME, null)
        set(value) = prefs.edit { putString(Keys.USERNAME, value) }

    val isLoggedIn: Boolean get() = username.isNullOrBlank()
}