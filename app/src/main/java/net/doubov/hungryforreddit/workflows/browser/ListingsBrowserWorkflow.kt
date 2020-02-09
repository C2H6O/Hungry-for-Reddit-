package net.doubov.hungryforreddit.workflows.browser

import com.squareup.workflow.RenderContext
import com.squareup.workflow.Snapshot
import com.squareup.workflow.StatefulWorkflow
import com.squareup.workflow.WorkflowAction
import com.squareup.workflow.ui.backstack.BackStackScreen
import net.doubov.api.models.NewsDataResponse
import net.doubov.core.containers.masterdetail.MasterDetailScreen
import net.doubov.hungryforreddit.workflows.detail.ListingDetailWorklow
import net.doubov.hungryforreddit.workflows.list.ListingsProps
import net.doubov.hungryforreddit.workflows.list.ListingsWorkflow

data class ListingBrowserProps(
    val listings: List<NewsDataResponse>
)

data class SelectedListing(val index: Int)

object ListingsBrowserWorkflow : StatefulWorkflow<ListingBrowserProps, SelectedListing, Nothing, MasterDetailScreen>() {

    sealed class Action : WorkflowAction<SelectedListing, Nothing> {
        data class SetSelected(val selectedListing: SelectedListing) : Action() {
            override fun WorkflowAction.Updater<SelectedListing, Nothing>.apply() {
                nextState = selectedListing
            }
        }
    }

    override fun initialState(props: ListingBrowserProps, snapshot: Snapshot?): SelectedListing =
        SelectedListing(-1)

    override fun render(
        props: ListingBrowserProps,
        state: SelectedListing,
        context: RenderContext<SelectedListing, Nothing>
    ): MasterDetailScreen {

        val listingsRendering = context.renderChild(
            ListingsWorkflow,
            ListingsProps(state.index, props.listings)
        ) {
            Action.SetSelected(it)
        }

        val masterScreen: BackStackScreen<Any> = BackStackScreen(listingsRendering)

        return if (state.index == -1) {
            MasterDetailScreen(masterScreen)
        } else {
            val detailScreen = context.renderChild(ListingDetailWorklow, props.listings[state.index]) {
                Action.SetSelected(SelectedListing(-1))
            }
            MasterDetailScreen(masterScreen, BackStackScreen(detailScreen) as BackStackScreen<Any>)
        }
    }

    override fun snapshotState(state: SelectedListing): Snapshot = Snapshot.EMPTY
}