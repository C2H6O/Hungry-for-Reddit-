package net.doubov.hungryforreddit.ribs

import com.uber.rib.core.Bundle
import com.uber.rib.core.Interactor
import com.uber.rib.core.RibInteractor
import kotlinx.coroutines.*
import net.doubov.api.RedditApi
import net.doubov.api.models.NewsResponse
import net.doubov.core.network.ApiResponse
import javax.inject.Inject

/**
 * Coordinates Business Logic for [RootRibScope].
 *
 * TODO describe the logic of this scope.
 */
@RibInteractor
class RootInteractor
    :
    Interactor<RootInteractor.RootRibPresenter, RootRouter>(),
    CoroutineScope by MainScope() {

    @Inject
    lateinit var presenter: RootRibPresenter

    @Inject
    lateinit var redditApi: RedditApi

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)
        println("LX___ didBecomeActive")

        launch {
            when (val newsResponse = withContext(Dispatchers.IO) { redditApi.fetchFrontPage() }) {
                is ApiResponse.Success -> presenter.setData(newsResponse.data)
                is ApiResponse.Failure -> println("LX___ ERROR! ${newsResponse.exception}")
            }
        }
    }

    override fun willResignActive() {
        println("LX___ willResignActive")
        coroutineContext[Job]?.cancel()
        super.willResignActive()
    }

    /**
     * Presenter interface implemented by this RIB's view.
     */
    interface RootRibPresenter {
        fun setListener(listener: RootView.Listener)
        fun setData(data: NewsResponse)
    }
}
