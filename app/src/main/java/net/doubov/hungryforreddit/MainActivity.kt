package net.doubov.hungryforreddit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try {
            launch {
                withContext(Dispatchers.IO) {
                    val apiResponse = (applicationContext as App).appComponent.redditApi().fetchAnonymousAccessToken()
                    println("apiResponse $apiResponse")
                }
            }
        } catch (e: Exception) {
            println("LX___ got an exception: $e")
        }

    }
}
