package net.doubov.hungryforreddit.workflows.list

import com.squareup.workflow.RenderContext
import com.squareup.workflow.StatelessWorkflow
import com.squareup.workflow.makeEventSink
import net.doubov.api.models.NewsDataResponse
import net.doubov.hungryforreddit.workflows.browser.SelectedListing

data class ListingsProps(
    val selected: Int,
    val listings: List<NewsDataResponse>
)

object ListingsWorkflow : StatelessWorkflow<ListingsProps, SelectedListing, ListingsWorkflow.Rendering>() {

    data class Rendering(
        val selected: Int,
        val newsResponse: List<NewsDataResponse>,
        val onListingSelected: (Int) -> Unit
    )

    override fun render(props: ListingsProps, context: RenderContext<Nothing, SelectedListing>): Rendering {

        val sink = context.makeEventSink { index: Int ->
            setOutput(SelectedListing(index))
        }

        return Rendering(
            props.selected,
            props.listings,
            onListingSelected = sink::send
        )
    }
}