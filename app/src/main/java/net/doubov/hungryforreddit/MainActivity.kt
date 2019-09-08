package net.doubov.hungryforreddit

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import dagger.android.AndroidInjection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.doubov.api.ApiResponse
import net.doubov.api.RedditApi
import net.doubov.core.AppPreferences
import javax.inject.Inject

class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    @Inject
    lateinit var appPreferences: AppPreferences

    @Inject
    lateinit var redditApi: RedditApi

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try {
            launch {
                withContext(Dispatchers.IO) {
                    when (val apiResponse = redditApi.fetchAnonymousAccessToken()) {
                        is ApiResponse.Success -> appPreferences.anonymousAccessToken = apiResponse.data.access_token
                        is ApiResponse.Failure -> Toast
                            .makeText(
                                this@MainActivity, "Failed to fetch anonymous access token", Toast
                                    .LENGTH_LONG
                            ).show()
                    }
                }
            }
        } catch (e: Exception) {
            println("LX___ got an exception: $e")
        }
    }
}
