package net.doubov.main.workflows.detail

import com.squareup.workflow.RenderContext
import com.squareup.workflow.StatelessWorkflow
import com.squareup.workflow.makeEventSink
import net.doubov.api.models.NewsDataResponse

object ListingDetailWorklow :
    StatelessWorkflow<NewsDataResponse, ListingDetailWorklow.Output, ListingDetailWorklow.Rendering>() {

    sealed class Output {
        object OnBackPressed : Output()
    }

    data class Rendering(
        val id: String,
        val author: String,
        val title: String,
        val num_comments: Int,
        val created: Float,
        val thumbnail: String,
        val url: String,
        val ups: Int,
        val onBackPressed: () -> Unit
    )

    override fun render(props: NewsDataResponse, context: RenderContext<Nothing, Output>): Rendering {

        val sink = context.makeEventSink { int: Int ->
            setOutput(Output.OnBackPressed)
        }

        return Rendering(
            id = props.id,
            author = props.author,
            title = props.title,
            num_comments = props.num_comments,
            created = props.created,
            thumbnail = props.thumbnail,
            url = props.url,
            ups = props.ups,
            onBackPressed = { sink.send(-1) }
        )
    }
}