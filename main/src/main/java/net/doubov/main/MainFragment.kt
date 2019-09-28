package net.doubov.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.*
import net.doubov.api.ApiResponse
import net.doubov.api.ApiResponseException
import net.doubov.api.RedditApi
import net.doubov.core.AppPreferences
import net.doubov.core.data.responses.AccessTokenResponse
import net.doubov.main.views.headerView
import javax.inject.Inject

class MainFragment : Fragment(), CoroutineScope by MainScope() {

    @Inject
    lateinit var appPreferences: AppPreferences

    @Inject
    lateinit var redditApi: RedditApi

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return layoutInflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        launch {

            fetchToken()

            when (val newsResponse = withContext(Dispatchers.IO) { redditApi.fetchFrontPage() }) {
                is ApiResponse.Success -> recyclerView.withModels {
                    newsResponse.data.data.children.forEach { childResponse ->
                        headerView {
                            id(childResponse.data.id)
                            title(childResponse.data.title)
                            ups(childResponse.data.ups.toString())
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

    private suspend fun fetchToken() {
        if (appPreferences.anonymousAccessToken == null) {
            println("LX___ requesting anonymousAccessToken!!")
            val apiResponse: ApiResponse<AccessTokenResponse> =
                withContext(Dispatchers.IO) { redditApi.fetchAnonymousAccessToken() }

            when (apiResponse) {
                is ApiResponse.Success -> appPreferences.anonymousAccessToken = apiResponse.data.access_token
                is ApiResponse.Failure -> showErrorToast(
                    "Failed to fetch anonymous access token",
                    apiResponse.exception
                )
            }
        }
    }

    override fun onDestroyView() {
        coroutineContext[Job]?.cancel()
        super.onDestroyView()
    }

    private fun showErrorToast(message: String, exception: ApiResponseException) {
        Toast
            .makeText(requireContext(), "$message: $exception", Toast.LENGTH_LONG)
            .show()
    }
}