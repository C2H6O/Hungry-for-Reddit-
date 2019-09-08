package net.doubov.core

import android.content.Context

abstract class Preferences(context: Context, name: String) {

    protected val prefs = context.getSharedPreferences(name, Context.MODE_PRIVATE)

    companion object {
        const val APP_PREFERENCES_NAME = "app_references"
        const val USER_PREFERENCES_NAME = "user_preferences"
    }
}