package net.doubov.core

import android.content.Context
import android.util.Log
import androidx.core.content.edit
import net.doubov.core.di.AppContext
import net.doubov.core.di.AppScope
import javax.inject.Inject

@AppScope
class AppPreferences @Inject constructor(@AppContext context: Context) : Preferences(context, APP) {

    object Keys {
        const val ANONYMOUS_ACCESS_TOKEN = "ANONYMOUS_ACCESS_TOKEN"
    }

    var anonymousAccessToken: String?
        get() = prefs.getString(Keys.ANONYMOUS_ACCESS_TOKEN, null)
        set(value) {
            Log.i("AUTH_TOKEN", "Refreshed ${Keys.ANONYMOUS_ACCESS_TOKEN}: $value")
            prefs.edit { putString(Keys.ANONYMOUS_ACCESS_TOKEN, value) }
        }
}