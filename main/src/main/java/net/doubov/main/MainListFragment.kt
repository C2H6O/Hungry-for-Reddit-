package net.doubov.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_main_list.*
import kotlinx.coroutines.*
import net.doubov.api.RedditApi
import net.doubov.core.network.ApiResponse
import net.doubov.core.network.ApiResponseException
import net.doubov.main.views.headerView
import javax.inject.Inject

class MainListFragment @Inject constructor(
    private val listener: Listener,
    private val redditApi: RedditApi
) : Fragment(), CoroutineScope by MainScope() {

    interface Listener {
        fun onEventReceived(event: Event)
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
                is ApiResponse.Success -> recyclerView.withModels {
                    newsResponse.data.data.children.map { it.data }.forEachIndexed { index, childResponse ->
                        headerView {
                            id(childResponse.id)
                            title(childResponse.title)
                            ups(childResponse.ups.toString())
                            clickListener(View.OnClickListener {
                                listener.onEventReceived(Event.OnItemSelected(index))
                            })
                        }
                    }
                }
                is ApiResponse.Failure -> showErrorToast(
                    "Failed to fetch NewsReponse",
                    newsResponse.exception
                )
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