package net.doubov.main

import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_main_list.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import net.doubov.api.RedditApi
import net.doubov.core.network.ApiResponse
import net.doubov.core.network.ApiResponseException
import net.doubov.hungryforreddit.R
import net.doubov.hungryforreddit.views.headerView
import javax.inject.Inject

fun ensureMainThread() {
    if (Thread.currentThread() != Looper.getMainLooper().thread) {
        throw IllegalStateException("Must be executed on the Main Thread")
    }
}

class MainListFragment : Fragment(), CoroutineScope by MainScope() {

    @Inject
    lateinit var eventsChannel: MainListChannel
    @Inject
    lateinit var redditApi: RedditApi

    class MainListChannel {
        val channel = Channel<Event>()
    }

    sealed class Event {
        data class OnItemSelected(val position: Int) : Event()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.fragment_main_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        launch {
            when (val newsResponse = withContext(Dispatchers.IO) { redditApi.fetchFrontPage() }) {
                is ApiResponse.Success -> {
                    ensureMainThread()
                    recyclerView.withModels {
                        newsResponse.data.data.children.map { it.data }.forEachIndexed { index, childResponse ->
                            headerView {
                                id(childResponse.id)
                                title(childResponse.title)
                                ups(childResponse.ups.toString())
                                clickListener(
                                    // TODO: fix this with more idiomatic code
                                    View.OnClickListener {
                                        GlobalScope.launch(Dispatchers.Main) {
                                            eventsChannel.channel.send(Event.OnItemSelected(index))
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
                is ApiResponse.Failure -> {
                    ensureMainThread()
                    showErrorToast(
                        "Failed to fetch NewsReponse",
                        newsResponse.exception
                    )
                }
            }
        }
    }

    override fun onDestroyView() {
        coroutineContext[Job]?.cancel()
        super.onDestroyView()
    }

    private fun showErrorToast(message: String, exception: ApiResponseException) {
        println("LX___ Error: $message $exception")
        Toast
            .makeText(requireContext(), "$message: $exception", Toast.LENGTH_LONG)
            .show()
    }

}