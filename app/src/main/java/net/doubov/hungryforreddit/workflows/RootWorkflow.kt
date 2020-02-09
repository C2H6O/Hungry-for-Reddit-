package net.doubov.hungryforreddit.workflows

import com.squareup.workflow.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import net.doubov.api.RedditApi
import net.doubov.api.models.NewsDataResponse
import net.doubov.api.models.NewsResponse
import net.doubov.core.network.ApiResponse
import net.doubov.core.network.ApiResponseException
import net.doubov.hungryforreddit.containers.masterdetail.MasterDetailScreen
import net.doubov.hungryforreddit.workflows.browser.ListingBrowserProps
import net.doubov.hungryforreddit.workflows.browser.ListingsBrowserWorkflow

class RootWorkflow(
    val redditApi: RedditApi
) :
    StatefulWorkflow<Unit, RootWorkflow.State, Nothing, RootWorkflow.Rendering>() {

    sealed class Action : WorkflowAction<State, Nothing> {
        data class SetNewsResponse(val newsResponse: ApiResponse<NewsResponse>) : Action() {
            override fun WorkflowAction.Updater<State, Nothing>.apply() {
                nextState = when (val oldState = nextState) {
                    State.Init, is State.Error -> when (newsResponse) {
                        is ApiResponse.Success -> State.Data(newsResponse.data.data.children.map { it.data })
                        is ApiResponse.Failure -> State.Error(newsResponse.exception)
                    }
                    is State.Data -> when (newsResponse) {
                        is ApiResponse.Success -> oldState.copy(listings = newsResponse.data.data.children.map { it.data })
                        is ApiResponse.Failure -> State.Error(newsResponse.exception)
                    }
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
                context.runningWorker(LadingFrontPageWorker(redditApi)) {
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

    class LadingFrontPageWorker(val redditApi: RedditApi) : Worker<ApiResponse<NewsResponse>> {
        override fun run(): Flow<ApiResponse<NewsResponse>> = flow {
            val response = redditApi.fetchFrontPage()
            println("LX___ emitting the response $response")
            emit(response)
        }
            .flowOn(Dispatchers.IO)
    }

}