package net.doubov.hungryforreddit.workflows

import android.view.View
import com.squareup.workflow.ui.LayoutRunner
import com.squareup.workflow.ui.ViewEnvironment
import com.squareup.workflow.ui.ViewFactory
import com.squareup.workflow.ui.WorkflowViewStub
import net.doubov.hungryforreddit.R

class RootLayoutRunner(
    view: View
) : LayoutRunner<RootWorkflow.Rendering> {

    companion object : ViewFactory<RootWorkflow.Rendering> by LayoutRunner.bind(
        R.layout.activity_main,
        ::RootLayoutRunner
    )

    private val listingsBrowserWorkflowStub = view.findViewById<WorkflowViewStub>(R.id.listingsBrowserWorkflow)

    override fun showRendering(rendering: RootWorkflow.Rendering, viewEnvironment: ViewEnvironment) {
        if (!rendering.isLoading && !rendering.isError) {
            listingsBrowserWorkflowStub.update(rendering.listingsBrowserRendering!!, viewEnvironment)
        }
    }
}