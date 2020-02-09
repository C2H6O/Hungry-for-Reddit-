package net.doubov.main.workflows.detail

import android.view.View
import android.widget.TextView
import com.squareup.workflow.ui.ContainerHints
import com.squareup.workflow.ui.LayoutRunner
import com.squareup.workflow.ui.LayoutRunner.Companion.bind
import com.squareup.workflow.ui.ViewBinding
import com.squareup.workflow.ui.backPressedHandler
import net.doubov.main.R

class ListingDetailLayoutRunner(private val view: View) : LayoutRunner<ListingDetailWorklow.Rendering> {

    companion object : ViewBinding<ListingDetailWorklow.Rendering> by bind(
        R.layout.fragment_detail,
        ::ListingDetailLayoutRunner
    )

    private val title = view.findViewById<TextView>(R.id.titleView)

    override fun showRendering(rendering: ListingDetailWorklow.Rendering, containerHints: ContainerHints) {
        title.text = rendering.title
        view.backPressedHandler = {
            rendering.onBackPressed()
        }
    }
}