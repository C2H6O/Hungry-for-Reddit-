package net.doubov.hungryforreddit.workflows.main

import com.squareup.workflow.RenderContext
import com.squareup.workflow.StatelessWorkflow
import net.doubov.api.models.NewsDataResponse

data class ListingsProps(
    val selected: Int,
    val listings: List<NewsDataResponse>
)

object ListingsWorkflow : StatelessWorkflow<ListingsProps, Nothing, ListingsWorkflow.Rendering>() {

    data class Rendering(
        val selected: Int,
        val newsResponse: List<NewsDataResponse>
    )

    override fun render(props: ListingsProps, context: RenderContext<Nothing, Nothing>): Rendering {
        return Rendering(props.selected, props.listings)
    }
}