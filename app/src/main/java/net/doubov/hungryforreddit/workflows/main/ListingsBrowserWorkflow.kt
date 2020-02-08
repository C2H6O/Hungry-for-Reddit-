package net.doubov.hungryforreddit.workflows.main

import com.squareup.workflow.RenderContext
import com.squareup.workflow.Snapshot
import com.squareup.workflow.StatefulWorkflow
import com.squareup.workflow.WorkflowAction
import com.squareup.workflow.ui.backstack.BackStackScreen
import net.doubov.api.models.NewsDataResponse
import net.doubov.hungryforreddit.containers.masterdetail.MasterDetailScreen

data class ListingBrowserProps(
    val listings: List<NewsDataResponse>
)

data class SelectedListing(val index: Int)

object ListingsBrowserWorkflow : StatefulWorkflow<ListingBrowserProps, SelectedListing, Nothing, MasterDetailScreen>() {

    sealed class Action : WorkflowAction<SelectedListing, Nothing> {
        data class SetSelected(val selectedListing: SelectedListing) : Action()
    }

    override fun initialState(props: ListingBrowserProps, snapshot: Snapshot?): SelectedListing = SelectedListing(-1)

    override fun render(
        props: ListingBrowserProps,
        state: SelectedListing,
        context: RenderContext<SelectedListing, Nothing>
    ): MasterDetailScreen {

        val listingsRendering = context.renderChild(ListingsWorkflow, ListingsProps(state.index, props.listings)) {
            Action.SetSelected(it)
        }

        val masterDetailScreen = MasterDetailScreen(BackStackScreen(listingsRendering))

        return if (state.index == -1) {
            masterDetailScreen
        } else {
            TODO()
        }
    }

    override fun snapshotState(state: SelectedListing): Snapshot = Snapshot.EMPTY
}