package net.doubov.hungryforreddit.workflows

import android.view.View
import com.squareup.workflow.ui.ContainerHints
import com.squareup.workflow.ui.LayoutRunner
import com.squareup.workflow.ui.ViewBinding
import com.squareup.workflow.ui.WorkflowViewStub
import net.doubov.hungryforreddit.R

class RootLayoutRunner(
    view: View
) : LayoutRunner<RootWorkflow.Rendering> {

    companion object : ViewBinding<RootWorkflow.Rendering> by LayoutRunner.bind(
        R.layout.activity_main,
        ::RootLayoutRunner
    )

    private val listingsBrowserWorkflowStub = view.findViewById<WorkflowViewStub>(R.id.listingsBrowserWorkflow)

    override fun showRendering(rendering: RootWorkflow.Rendering, containerHints: ContainerHints) {
        if (!rendering.isLoading && !rendering.isError) {
            listingsBrowserWorkflowStub.update(rendering.listingsBrowserRendering!!, containerHints)
        }
    }
}