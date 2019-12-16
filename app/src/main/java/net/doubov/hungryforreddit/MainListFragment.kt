package net.doubov.main

import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_main_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.doubov.api.RedditApi
import net.doubov.api.models.NewsDataResponse
import net.doubov.core.network.ApiResponse
import net.doubov.core.network.ApiResponseException
import net.doubov.hungryforreddit.BaseFragment
import net.doubov.hungryforreddit.R
import net.doubov.hungryforreddit.views.headerView
import javax.inject.Inject

class MainListFragment : BaseFragment(R.layout.fragment_main_list) {

    @Inject
    lateinit var eventsChannel: MainListChannel

    @Inject
    lateinit var redditApi: RedditApi

    class MainListChannel {
        val channel = Channel<Event>()
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
                                clickListener(
                                    // TODO: fix this with more idiomatic code
                                    View.OnClickListener {
                                        GlobalScope.launch(Dispatchers.Main) {
                                            eventsChannel.channel.send(Event.OnItemSelected(childResponse))
                                        }
                                    }
                                )
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