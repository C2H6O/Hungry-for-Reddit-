package net.doubov.hungryforreddit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.android.AndroidInjection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.doubov.core.AppPreferences
import javax.inject.Inject

class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    @Inject
    lateinit var appPreferences: AppPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try {
            launch {
                withContext(Dispatchers.IO) {
                    val apiResponse = (applicationContext as App).getAppComponent().redditApi().fetchAnonymousAccessToken()
                    println("apiResponse $apiResponse")

                }
            }
        } catch (e: Exception) {
            println("LX___ got an exception: $e")
        }

    }
}
