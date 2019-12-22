package net.doubov.core

import android.content.Context

abstract class Preferences(context: Context, name: String) {

    protected val prefs = context.getSharedPreferences(name, Context.MODE_PRIVATE)

    companion object {
        const val APP = "preferences_app"
        const val USER = "preferences_user"
    }
}