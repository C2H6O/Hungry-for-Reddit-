package net.doubov.main

import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_main_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.doubov.api.RedditApi
import net.doubov.api.models.NewsDataResponse
import net.doubov.core.network.ApiResponse
import net.doubov.core.network.ApiResponseException
import net.doubov.core.ui.BaseFragment
import net.doubov.main.views.headerView
import javax.inject.Inject

class ListFragment : BaseFragment(R.layout.fragment_main_list) {

    @Inject
    lateinit var eventsChannel: ListChannel

    @Inject
    lateinit var redditApi: RedditApi

    private val clickListener = { item: NewsDataResponse ->
        launch { eventsChannel.channel.send(Event.OnItemSelected(item)) }
    }

    class ListChannel {
        val channel = Channel<Event>(Channel.BUFFERED)
    }

    sealed class Event {
        data class OnItemSelected(val newsDataResponse: NewsDataResponse) : Event()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        launch {
            when (val newsResponse = withContext(Dispatchers.IO) { redditApi.fetchFrontPage() }) {
                is ApiResponse.Success -> {
                    recyclerView.withModels {
                        newsResponse.data.data.children.map { it.data }.forEachIndexed { index, childResponse ->
                            headerView {
                                id(childResponse.id)
                                title(childResponse.title)
                                ups(childResponse.ups.toString())
                                clickListener(View.OnClickListener { clickListener(childResponse) })
                            }
                        }
                    }
                }
                is ApiResponse.Failure -> {
                    showErrorToast(
                        "Failed to fetch NewsReponse",
                        newsResponse.exception
                    )
                }
            }
        }

    }

    override fun onDestroyView() {
        recyclerView.adapter = null
        super.onDestroyView()
    }

    private fun showErrorToast(message: String, exception: ApiResponseException) {
        println("LX___ Error: $message $exception")
        Toast
            .makeText(requireContext(), "$message: $exception", Toast.LENGTH_LONG)
            .show()
    }

}