package net.doubov.hungryforreddit.workflows

import com.squareup.workflow.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import net.doubov.api.RedditApi
import net.doubov.api.models.NewsDataResponse
import net.doubov.api.models.NewsResponse
import net.doubov.api.models.toListings
import net.doubov.core.containers.masterdetail.MasterDetailScreen
import net.doubov.core.di.ActivityScope
import net.doubov.core.network.ApiResponse
import net.doubov.core.network.ApiResponseException
import net.doubov.main.workflows.browser.ListingBrowserProps
import net.doubov.main.workflows.browser.ListingsBrowserWorkflow
import javax.inject.Inject

@ActivityScope
@ExperimentalCoroutinesApi
class RootWorkflow @Inject constructor(
    private val redditApi: RedditApi
) :
    StatefulWorkflow<Unit, RootWorkflow.State, Nothing, RootWorkflow.Rendering>() {

    sealed class Action : WorkflowAction<State, Nothing> {
        data class SetNewsResponse(val apiResponse: ApiResponse<NewsResponse>) : Action() {
            override fun WorkflowAction.Updater<State, Nothing>.apply() {
                nextState = when (apiResponse) {
                    is ApiResponse.Success -> {
                        val listings = apiResponse.toListings()
                        when (val oldState = nextState) {
                            State.Init, is State.Error -> State.Data(listings)
                            is State.Data -> oldState.copy(listings = listings)
                        }
                    }
                    is ApiResponse.Failure -> State.Error(apiResponse.exception)
                }
            }
        }
    }

    sealed class State {
        object Init : State()
        data class Error(val exception: ApiResponseException) : State()
        data class Data(val listings: List<NewsDataResponse>) : State()
    }

    data class Rendering(
        val isLoading: Boolean = false,
        val isError: Boolean = false,
        val listingsBrowserRendering: MasterDetailScreen? = null
    )

    override fun initialState(props: Unit, snapshot: Snapshot?): State = State.Init
    override fun snapshotState(state: State): Snapshot = Snapshot.EMPTY

    override fun render(props: Unit, state: State, context: RenderContext<State, Nothing>): Rendering {
        return when (state) {
            State.Init -> {
                context.runningWorker(FetchFrontPageWorker(redditApi)) {
                    Action.SetNewsResponse(it)
                }
                Rendering(isLoading = true)
            }
            is State.Data -> {
                Rendering(
                    listingsBrowserRendering = context.renderChild(
                        ListingsBrowserWorkflow,
                        ListingBrowserProps(state.listings)
                    )
                )
            }
            is State.Error -> {
                Rendering(isError = true)
            }
        }
    }

    @ExperimentalCoroutinesApi
    class FetchFrontPageWorker(val redditApi: RedditApi) : Worker<ApiResponse<NewsResponse>> {
        override fun run(): Flow<ApiResponse<NewsResponse>> = flow { emit(redditApi.fetchFrontPage()) }
            .flowOn(Dispatchers.IO)
    }

}