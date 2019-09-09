package net.doubov.hungryforreddit

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import dagger.android.support.AndroidSupportInjection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.doubov.api.ApiResponse
import net.doubov.api.ApiResponseException
import net.doubov.api.RedditApi
import net.doubov.core.AppPreferences
import net.doubov.core.data.responses.AccessTokenResponse
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
            val apiResponse: ApiResponse<AccessTokenResponse> = withContext(Dispatchers.IO) {
                try {
                    redditApi.fetchAnonymousAccessToken()
                } catch (e: Exception) {
                    ApiResponse.Failure<AccessTokenResponse>(ApiResponseException(e.message))
                }
            }

            when (apiResponse) {
                is ApiResponse.Success -> appPreferences.anonymousAccessToken = apiResponse.data.access_token
                is ApiResponse.Failure -> Toast
                    .makeText(requireContext(), "Failed to fetch anonymous access token: ${apiResponse.exception}", Toast.LENGTH_LONG)
                    .show()
            }

            withContext(Dispatchers.IO) { redditApi.fetchFrontPage() }
        }
    }

    override fun onDestroyView() {
        coroutineContext[Job]?.cancel()
        super.onDestroyView()
    }
}